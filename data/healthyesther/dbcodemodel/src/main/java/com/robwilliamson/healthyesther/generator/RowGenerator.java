package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.semantic.ColumnField;
import com.robwilliamson.healthyesther.semantic.RowField;
import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

@ClassGeneratorFeatures(name = "Row", parameterName = "row")
public class RowGenerator extends BaseClassGenerator {

    private final TableGenerator mTableGenerator;
    private final JMethod mRowConstructor;
    private final JMethod mJoinConstructor;
    private List<ColumnField> mSortedFields;
    private JFieldVar mColumnNames;
    private List<ColumnField> mPrimaryKeyFields = new ArrayList<>();
    private Map<ColumnField, RowField> mPrimaryKeyFieldToRowMap = new HashMap<>();

    public RowGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        mTableGenerator = tableGenerator;

        JDefinedClass clazz = mTableGenerator.getJClass()._class(
                JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                getName());
        clazz._extends(BaseTransactable.class);

        setJClass(clazz);

        mRowConstructor = getJClass().constructor(JMod.PUBLIC);

        if (getTableGenerator().getTable().hasDependencies()) {
            mJoinConstructor = getJClass().constructor(JMod.PUBLIC);
        } else {
            mJoinConstructor = null;
        }
    }

    private static String name(RowGenerator rowGenerator) {
        return Strings.lowerCase(rowGenerator.getTableGenerator().getPreferredParameterName()) +
                "Row";
    }

    public void init() {
        mSortedFields = ColumnField.makeSortedList(getJClass(),
                Arrays.asList(getTableGenerator().getTable().getColumns()));
        // Create a static list of field names.
        JClass stringListType = model().ref(ArrayList.class);
        stringListType.narrow(String.class);
        mColumnNames = getJClass().field(
                JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                stringListType,
                "COLUMN_NAMES");
        mColumnNames.init(JExpr._new(stringListType).arg(JExpr.lit(mSortedFields.size())));

        // Get a class constructor body.
        JBlock classConstructor = getJClass().init();
        for (ColumnField columnField : mSortedFields) {
            // Populate the field names.
            classConstructor
                    .invoke(mColumnNames, "add")
                    .arg(JExpr.lit(columnField.column.getName()));

            // Collect primary key(s).
            if (columnField.column.isPrimaryKey()) {
                mPrimaryKeyFields.add(columnField);
            }
        }

        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                makeConstructors();
                makeAccessors();
                makeInsert();
                //makeModify();
                //makeRemove();
            }
        });
    }

    public List<ColumnField> getPrimaryKeyFields() {
        return mPrimaryKeyFields;
    }

    private void makeInsert() {
        TableGenerator generator = getTableGenerator();
        PrimaryKeyGenerator keyGenerator = generator.getPrimaryKeyGenerator();
        JDefinedClass primaryKeyType = keyGenerator.getJClass();
        JMethod insert = getJClass().method(JMod.PUBLIC, Object.class, "insert");
        insert.annotate(Override.class);
        JVar transaction = insert.param(Transaction.class, "transaction");
        JBlock body = insert.body();

        if (keyGenerator.hasPrimaryKeys()) {
            Map<ColumnField, JVar> temporaryPrimaryKeys = new HashMap<>();
            JInvocation insertionCall = JExpr.invoke(transaction, "insert");
            JInvocation newPrimaryKeyTypeCall = JExpr._new(primaryKeyType);

            insertionCall.arg(mColumnNames);

            for (ColumnField field : mSortedFields) {
                if (!field.column.isForeignKey() || !field.column.isPrimaryKey()) {
                    insertionCall.arg(field.fieldVar);

                    if (field.column.isPrimaryKey() && !hasSingleRowIdPrimaryKey()) {
                        newPrimaryKeyTypeCall.arg(field.fieldVar);
                    }
                    continue;
                }

                JVar tempKey = body.decl(
                        JMod.FINAL,
                        field.fieldVar.type().array(),
                        field.name,
                        JExpr.newArray(field.fieldVar.type()).add(field.fieldVar));
                JAssignmentTarget tempKeyValue = tempKey.component(JExpr.lit(0));
                temporaryPrimaryKeys.put(field, tempKey);
                JBlock ifBlock = body._if(field.fieldVar.eq(JExpr._null()))._then();
                ifBlock.assign(
                        tempKeyValue,
                        JExpr.cast(
                                field.fieldVar.type(),
                                mPrimaryKeyFieldToRowMap
                                        .get(field).fieldVar.invoke("insert").arg(transaction)));

                insertionCall.arg(tempKeyValue);
                newPrimaryKeyTypeCall.arg(tempKeyValue);
            }

            JVar insertionId = body.decl(JMod.FINAL, model().LONG, "rowId", insertionCall);
            if (hasSingleRowIdPrimaryKey()) {
                newPrimaryKeyTypeCall.arg(insertionId);
            }

            JVar primaryKey = body.decl(
                    JMod.FINAL,
                    primaryKeyType,
                    "primaryKey",
                    newPrimaryKeyTypeCall);
            JDefinedClass anonymousType = model().anonymousClass(
                    Transaction.CompletionHandler.class);
            JBlock callback = anonymousType.method(
                    JMod.PUBLIC,
                    model().VOID,
                    "onCompleted").body();

            for (ColumnField field : mPrimaryKeyFields) {
                if (!field.column.isForeignKey()) {
                    if (field.fieldVar.type().equals(primaryKey.type())) {
                        callback.assign(field.fieldVar, primaryKey);
                    }
                    continue;
                }

                callback.assign(
                        field.fieldVar,
                        temporaryPrimaryKeys.get(field).component(JExpr.lit(0)));
            }

            setIsInDatabaseCall(callback, true);
            setIsModifiedCall(callback, false);

            body.invoke(transaction, "addCompletionHandler").arg(JExpr._new(anonymousType));
            body._return(primaryKey);
        } else {
            body._return(populateArgumentsFor(body.invoke(transaction, "insert")));
        }
    }

    private void setIsModifiedCall(JBlock block, boolean isModified) {
        block.invoke("setIsModified").arg(JExpr.lit(isModified));
    }

    private void setIsInDatabaseCall(JBlock block, boolean isInDatabase) {
        block.invoke("setIsInDatabase").arg(JExpr.lit(isInDatabase));
    }

    private JInvocation populateArgumentsFor(JInvocation invocation) {
        invocation = invocation.arg(mColumnNames);

        for (ColumnField columnField : mSortedFields) {
            if (columnField.column.isPrimaryKey()) {
                continue;
            }

            invocation = invocation.arg(columnField.fieldVar);
        }

        return invocation;
    }

    private void makeConstructors() {
        List<ColumnField> nullable = new ArrayList<>();
        List<ColumnField> nonNull = new ArrayList<>();

        for (ColumnField field : mSortedFields) {
            if (field.column.isNotNull()) {
                nonNull.add(field);
            } else {
                nullable.add(field);
            }
        }

        for (ColumnField field : nonNull) {
            // Handle join constructor.
            implementJoinConstructorFor(field, false);

            // Handle row constructor.
            implementRowConstructorFor(field, false);
        }

        for (ColumnField field : nullable) {
            // Handle join constructor.
            implementJoinConstructorFor(field, true);

            // Handle row constructor.
            implementRowConstructorFor(field, true);
        }
    }

    private void implementJoinConstructorFor(ColumnField field, boolean nullable) {
        if (mJoinConstructor == null) {
            return;
        }

        JVar param;
        if (field.column.isForeignKey()) {
            // Always there is a join constructor if there are foreign keys.
            ColumnDependency dependency = field.column.getColumnDependency();
            param = addJoinConstructorParam(dependency);
            mJoinConstructor.body().assign(field.fieldVar, param);
        } else {
            param = mJoinConstructor.param(field.fieldVar.type(), field.name);
            mJoinConstructor.body().assign(field.fieldVar, param);
        }

        annotateNonull(param, !nullable);
    }

    private void implementRowConstructorFor(ColumnField field, boolean nullable) {
        ColumnDependency dependency = field.column.getColumnDependency();
        JVar param;
        if (field.column.isForeignKey()) {

            RowGenerator row = field.column
                    .getColumnDependency().getDependency().getTable().getGenerator().getRow();
            RowField rowField = new RowField(getJClass(), row, field.name + "Row");
            param = addRowConstructorParam(dependency);
            mRowConstructor.body().assign(rowField.fieldVar, param);
            mPrimaryKeyFieldToRowMap.put(field, rowField);
        } else {
            param = mRowConstructor.param(field.fieldVar.type(), field.name);
            mRowConstructor.body().assign(field.fieldVar, param);
        }

        annotateNonull(param, !nullable);
    }

    private void annotateNonull(JVar param, boolean nonnull) {
        if (nonnull) {
            param.annotate(Nonnull.class);
        }
    }

    private void makeAccessors() {
        for (ColumnField field : mSortedFields) {
            JMethod setter = getJClass().method(
                    JMod.PUBLIC,
                    model().VOID,
                    "set" + Strings.capitalize(field.name));
            JVar value = setter.param(field.fieldVar.type(), field.name);
            JBlock ifChangedBlock = setter.body()._if(field.fieldVar.ne(value))._then();
            ifChangedBlock.assign(field.fieldVar, value);
            setIsModifiedCall(ifChangedBlock, true);

            JMethod getter = getJClass().method(
                    JMod.PUBLIC,
                    field.fieldVar.type(),
                    "get" + Strings.capitalize(field.name));
            getter.body()._return(field.fieldVar);
        }
    }

    public TableGenerator getTableGenerator() {
        return mTableGenerator;
    }

    private JVar addRowConstructorParam(ColumnDependency columnDependency) {
        RowGenerator rowGenerator = columnDependency
                .getDependency().getTable().getGenerator().getRow();
        return mRowConstructor.param(rowGenerator.getJClass(), name(rowGenerator));
    }

    private JVar addJoinConstructorParam(ColumnDependency columnDependency) {
        RowGenerator rowGenerator = columnDependency
                .getDependency().getTable().getGenerator().getRow();

        com.robwilliamson.healthyesther.semantic.Table table = rowGenerator
                .getTableGenerator().getTable();
        PrimaryKeyGenerator primaryKeyGenerator = table.getGenerator().getPrimaryKeyGenerator();
        return mJoinConstructor.param(primaryKeyGenerator.getJClass(), primaryKeyGenerator
                .getPreferredParameterName());
    }

    private boolean hasSingleRowIdPrimaryKey() {
        if (getPrimaryKeyFields().size() != 1) {
            return false;
        }

        ColumnField field = mPrimaryKeyFields.get(0);

        return field.column.getName().equals("_id") && field.column.getPrimitiveType(model()).equals(model().LONG);
    }
}
