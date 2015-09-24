package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.semantic.BaseField;
import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.semantic.ColumnField;
import com.robwilliamson.healthyesther.semantic.RowField;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

@ClassGeneratorFeatures(name = "Row", parameterName = "row")
public class RowGenerator extends BaseClassGenerator {

    private final TableGenerator mTableGenerator;
    private final JMethod mJoinConstructor;
    private final JMethod mValueConstructor;
    private PrimaryColumns mPrimaryKeys;
    private BasicColumns mBasicKeys;
    private List<Column> mAllColumns;
    private JFieldVar mColumnNames;

    public RowGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        mTableGenerator = tableGenerator;

        JDefinedClass clazz = mTableGenerator.getJClass()._class(
                JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                getName());

        setJClass(clazz);

        mValueConstructor = clazz.constructor(JMod.PUBLIC);

        if (getTableGenerator().getTable().hasDependencies()) {
            mJoinConstructor = clazz.constructor(JMod.PUBLIC);
        } else {
            mJoinConstructor = null;
        }
    }

    private static String name(RowGenerator rowGenerator) {
        return Strings.lowerCase(rowGenerator.getTableGenerator().getPreferredParameterName()) +
                "Row";
    }

    public void init() {
        // Create a static list of field names.
        JClass stringListType = model().ref(ArrayList.class);
        stringListType = stringListType.narrow(String.class);
        mColumnNames = getJClass().field(
                JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                stringListType,
                "COLUMN_NAMES");

        mColumnNames.init(JExpr._new(stringListType).arg(JExpr.lit(mTableGenerator.getTable().getColumns().length)));

        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                asyncInit();
                makeConstructors();
                makeAccessors();
                makeInsert();
                makeUpdate();
                makeRemove();
                makeEquals();
            }
        });
    }

    private void asyncInit() {
        mPrimaryKeys = new PrimaryColumns();
        mBasicKeys = new BasicColumns();
        JDefinedClass clazz = getJClass();
        JClass baseClass = model().ref(BaseRow.class).narrow(mTableGenerator.getPrimaryKeyGenerator().getJClass());
        clazz._extends(baseClass);

        // Sort the columns
        mAllColumns = Arrays.asList(getTableGenerator().getTable().getColumns());
        Collections.sort(mAllColumns, new Column.Comparator());

        // Get a class constructor body.
        JBlock classConstructor = clazz.init();
        for (Column column : mAllColumns) {
            // Populate the field names.
            classConstructor
                    .invoke(mColumnNames, "add")
                    .arg(JExpr.lit(column.getName()));
        }
    }

    private void makeUpdate() {
        JMethod update = getJClass().method(JMod.PUBLIC, model().VOID, "update");
        update.annotate(Override.class);
        JVar transaction = update.param(Transaction.class, "transaction");
        JBlock body = update.body();

        if (mPrimaryKeys.columns.isEmpty()) {
            body._throw(JExpr._new(model()._ref(UnsupportedOperationException.class)));
            return;
        }

        body._if(JExpr._this().invoke("isInDatabase").not())._then()._throw(JExpr._new(model()._ref(BaseTransactable.UpdateFailed.class)).arg(JExpr.lit("Could not update because the row is not in the database.")));

        JInvocation where = JExpr.invoke(null, "getConcretePrimaryKey");

        JInvocation updateInvocation = transaction.invoke(update).arg(where);
        updateInvocation = updateInvocation.arg(mColumnNames);
        for (Column column : mBasicKeys.columns) {
            updateInvocation.arg(mBasicKeys.columnFieldFor(column).fieldVar);
        }
        JVar actual = body.decl(model().INT, "actual", updateInvocation);

        JExpression expected = JExpr.lit(1);
        body._if(actual.ne(expected))._then()._throw(JExpr._new(model()._ref(BaseTransactable.UpdateFailed.class)).arg(expected).arg(actual));

        JDefinedClass anonymousType = model().anonymousClass(
                Transaction.CompletionHandler.class);
        JBlock callback = anonymousType.method(
                JMod.PUBLIC,
                model().VOID,
                "onCompleted").body();

        setIsModifiedCall(callback, false);

        body.invoke(transaction, "addCompletionHandler").arg(JExpr._new(anonymousType));
    }

    private void makeRemove() {
        JMethod remove = getJClass().method(JMod.PUBLIC, model().VOID, "remove");
        remove.annotate(Override.class);
        JVar transaction = remove.param(Transaction.class, "transaction");
        JBlock body = remove.body();

        if (mPrimaryKeys.columns.isEmpty()) {
            body._throw(JExpr._new(model()._ref(UnsupportedOperationException.class)));
            return;
        }

        body._if(JExpr._this().invoke("isInDatabase").not())._then()._return();

        JInvocation where = JExpr.invoke(null, "getConcretePrimaryKey");

        JVar actual = body.decl(model().INT, "actual", transaction.invoke(remove).arg(where));

        JExpression expected = JExpr.lit(1);
        body._if(actual.ne(expected))._then()._throw(JExpr._new(model()._ref(BaseTransactable.RemoveFailed.class)).arg(expected).arg(actual));

        JDefinedClass anonymousType = model().anonymousClass(
                Transaction.CompletionHandler.class);
        JBlock callback = anonymousType.method(
                JMod.PUBLIC,
                model().VOID,
                "onCompleted").body();

        setIsInDatabaseCall(callback, false);
        setIsDeletedCall(callback, true);

        body.invoke(transaction, "addCompletionHandler").arg(JExpr._new(anonymousType));
    }

    private void setIsDeletedCall(JBlock block, boolean isDeleted) {
        block.invoke("setIsDeleted").arg(JExpr.lit(isDeleted));
    }

    private void makeEquals() {
        JMethod eqMethod = makeBasicEquals();
        JDefinedClass theClass = getJClass();
        JBlock body = eqMethod.body();
        JVar other = eqMethod.listParams()[0];
        JVar otherType = body.decl(theClass, "the" + theClass.name(), JExpr.cast(theClass, other));

        for (Column column : mBasicKeys.columns) {
            ColumnField field = mBasicKeys.columnFieldFor(column);
            JBlock ifBlock = body._if(makeEquals(field.fieldVar, otherType.ref(field.fieldVar)).not())._then();
            ifBlock._return(JExpr.lit(false));
        }

        body._return(JExpr.lit(true));
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
            JInvocation insertionCall = JExpr.invoke(transaction, "insert");
            JInvocation newPrimaryKey = JExpr._new(primaryKeyType);

            insertionCall.arg(mColumnNames);
            JVar primaryKey = body.decl(primaryKeyType, "primaryKey", body.invoke(null, "getConcretePrimaryKey"));
            JVar doConstructPrimaryKey = body.decl(model().BOOLEAN, "constructPrimaryKey", primaryKey.eq(JExpr._null()).not());

            JConditional ifConstruct = body._if(doConstructPrimaryKey);
            JBlock doConstruction = ifConstruct._then();
            doConstruction.assign(primaryKey, doConstruction.invoke(null, "setPrimaryKey").arg(newPrimaryKey));

            for (Column column : mPrimaryKeys.columns) {
                JInvocation i = primaryKey.invoke(keyGenerator.getGetterFor(column));
                insertionCall.arg(i);
                newPrimaryKey.arg(i);
            }

            for (Column column : mBasicKeys.columns) {
                ColumnField field = mBasicKeys.columnFieldFor(column);
                insertionCall.arg(field.fieldVar);
            }

            JVar insertionId = body.decl(JMod.FINAL, model().LONG, "rowId", insertionCall);
            if (hasSingleRowIdPrimaryKey()) {
                newPrimaryKey.arg(insertionId);
            }

            JVar finalPrimaryKey = body.decl(
                    JMod.FINAL,
                    primaryKeyType,
                    "primaryKey",
                    primaryKey);
            JDefinedClass anonymousType = model().anonymousClass(
                    Transaction.CompletionHandler.class);
            JBlock callback = anonymousType.method(
                    JMod.PUBLIC,
                    model().VOID,
                    "onCompleted").body();

            if (keyGenerator.hasRowId()) {
                callback.invoke(finalPrimaryKey, keyGenerator.getSetterFor(keyGenerator.getRowId().column)).arg(insertionId);
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

        for (Column column : mBasicKeys.columns) {
            if (column.isPrimaryKey()) {
                continue;
            }

            invocation = invocation.arg(mBasicKeys.columnFieldFor(column).fieldVar);
        }

        return invocation;
    }

    private void makeConstructors() {
        ColumnPicker picker = new ColumnPicker() {
            @Override
            public boolean pick(Column column) {
                if (column.isPrimaryKey() && !column.isForeignKey() && column.getName().equals("_id")) {
                    return false;
                }

                return true;
            }
        };

        makeJoinConstructor(picker);
        makeValueConstructor(picker);
    }

    private void makeJoinConstructor(ColumnPicker picker) {
        if (mJoinConstructor == null) {
            return;
        }

        Map<Column, JVar> primaryParams = mPrimaryKeys.makeRowParamsFor(mJoinConstructor, picker);
        Map<Column, JVar> basicParams = mBasicKeys.makeRowParamsFor(mJoinConstructor, picker);

        for (Column column : mPrimaryKeys.columns) {
            RowField rowField = mPrimaryKeys.rowFieldFor(column);
            JVar param = primaryParams.get(column);
            if (param == null) {
                continue;
            }

            if (rowField != null) {
                mJoinConstructor.body().assign(rowField.fieldVar, param);
            } else {
                mJoinConstructor.body().assign(mPrimaryKeys.columnFieldFor(column).fieldVar, param);
            }
        }

        for (Column column : mBasicKeys.columns) {
            RowField rowField = mBasicKeys.rowFieldFor(column);
            JVar param = basicParams.get(column);
            if (param == null) {
                continue;
            }

            if (rowField != null) {
                mJoinConstructor.body().assign(rowField.fieldVar, param);
            } else {
                mJoinConstructor.body().assign(mBasicKeys.columnFieldFor(column).fieldVar, param);
            }
        }
    }

    private void makeValueConstructor(ColumnPicker picker) {
        Map<Column, JVar> primaryParams = mPrimaryKeys.makeValueParamsFor(mValueConstructor, picker);
        Map<Column, JVar> basicParams = mBasicKeys.makeValueParamsFor(mValueConstructor, picker);
        PrimaryKeyGenerator primaryKeyGenerator = mTableGenerator.getPrimaryKeyGenerator();
        JInvocation primaryKeyConstruction = JExpr._new(primaryKeyGenerator.getJClass());
        for (Column column : mPrimaryKeys.columns) {
            JVar param = primaryParams.get(column);
            if (param != null) {
                primaryKeyConstruction.arg(primaryParams.get(column));
            }
        }

        callSetPrimaryKey(mValueConstructor.body(), null).arg(primaryKeyConstruction);

        for (Column column : mBasicKeys.columns) {
            JVar param = basicParams.get(column);
            if (param != null) {
                ColumnField field = mBasicKeys.columnFieldFor(column);
                mValueConstructor.body().assign(field.fieldVar, basicParams.get(column));
            }
        }
    }

    private JInvocation callSetPrimaryKey(JBlock body, JExpression expression) {
        return body.invoke(expression, "setPrimaryKey");
    }

    private void implementValueConstructorFor(ColumnField field) {
        JVar param;
        if (field.column.isForeignKey()) {
            ColumnDependency dependency = field.column.getColumnDependency();
            param = addValueConstructorParam(dependency);
            mValueConstructor.body().assign(field.fieldVar, param);
        } else {
            param = mValueConstructor.param(field.fieldVar.type(), field.name);
            mValueConstructor.body().assign(field.fieldVar, param);
        }

        annotateNonull(param, field.column.isNotNull());
    }

    private void annotateNonull(JVar param, boolean nonnull) {
        if (nonnull) {
            param.annotate(Nonnull.class);
        }
    }

    private void makeAccessors() {
        for (Column column : mBasicKeys.columns) {
            ColumnField field = mBasicKeys.columnFieldFor(column);
            JMethod setter = getJClass().method(
                    JMod.PUBLIC,
                    model().VOID,
                    "set" + Strings.capitalize(field.name));
            JVar value = setter.param(field.fieldVar.type(), field.name);
            JBlock body = setter.body();
            body._if(makeEquals(field.fieldVar, value))._then()._return();
            body.assign(field.fieldVar, value);
            setIsModifiedCall(body, true);

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

    private JVar addValueConstructorParam(ColumnDependency columnDependency) {
        RowGenerator rowGenerator = columnDependency
                .getDependency().getTable().getGenerator().getRow();

        com.robwilliamson.healthyesther.semantic.Table table = rowGenerator
                .getTableGenerator().getTable();
        PrimaryKeyGenerator primaryKeyGenerator = table.getGenerator().getPrimaryKeyGenerator();
        return mValueConstructor.param(
                primaryKeyGenerator.getJClass(),
                Strings.lowerCase(Strings.underscoresToCamel(table.getName()) + Strings.capitalize(primaryKeyGenerator
                        .getPreferredParameterName())));
    }

    private boolean hasSingleRowIdPrimaryKey() {
        return mTableGenerator.getPrimaryKeyGenerator().getValueConstructor().listParams().length == 1;
    }

    private JVar addConstructorParam(JMethod constructor, Column column) {
        JVar param = constructor.param(column.getPrimitiveType(model()), ColumnField.name(column.getName()));
        annotateNonull(param, column.isNotNull());
        return param;
    }

    public interface ColumnPicker {
        boolean pick(Column column);
    }

    private abstract class BaseColumns {
        public final List<Column> columns = new ArrayList<>();
        private Map<Column, RowField> mColumnToRowFieldMap = new HashMap<>();
        private Map<Column, ColumnField> mColumnToColumnFieldMap = new HashMap<>();

        public BaseColumns() {
            for (Column column : mTableGenerator.getTable().getColumns()) {
                if (addToColumns(column)) {
                    this.columns.add(column);
                    mColumnToColumnFieldMap.put(column, makeColumnFieldFor(column));
                    if (column.isForeignKey()) {
                        mColumnToRowFieldMap.put(column, makeRowFieldFor(column));
                    }
                }
            }
            Collections.sort(this.columns, new Column.Comparator());
        }

        public RowField rowFieldFor(Column column) {
            return mColumnToRowFieldMap.get(column);
        }

        public ColumnField columnFieldFor(Column column) {
            return mColumnToColumnFieldMap.get(column);
        }

        public Map<Column, JVar> makeValueParamsFor(JMethod method, ColumnPicker picker) {
            Map<Column, JVar> map = new HashMap<>(columns.size());
            for (Column column : columns) {
                if (!picker.pick(column)) {
                    continue;
                }

                JVar param = method.param(getValueParamType(column), column.getVarName());
                annotateNonull(param, column.isNotNull());
                map.put(column, param);
            }

            return map;
        }


        protected ColumnField makeColumnFieldFor(Column column) {
            ColumnField field = new ColumnField(getJClass(), column);
            // Foreign keys could be represented as rows - can't be notnull.
            annotateNonull(field.fieldVar, column.isNotNull() && !column.isForeignKey());
            return field;
        }


        protected RowField makeRowFieldFor(Column column) {
            RowGenerator row = column
                    .getColumnDependency().getDependency().getTable().getGenerator().getRow();
            RowField rowField = new RowField(getJClass(), column, row, BaseField.name(column.getName()) + "Row");
            return rowField;
        }

        protected abstract boolean addToColumns(Column column);

        private JType getValueParamType(Column column) {
            if (column.isForeignKey()) {
                return column.getColumnDependency().getDependency().getTable().getGenerator().getPrimaryKeyGenerator().getJClass();
            } else {
                return column.getPrimitiveType(model());
            }
        }

        public Map<Column, JVar> makeRowParamsFor(JMethod method, ColumnPicker picker) {
            Map<Column, JVar> map = new HashMap<>(columns.size());
            for (Column column : columns) {
                if (!picker.pick(column)) {
                    continue;
                }

                JVar param = method.param(getRowParamType(column), column.getVarName());
                annotateNonull(param, column.isNotNull());
                map.put(column, param);
            }

            return map;
        }

        private JType getRowParamType(Column column) {
            if (column.isForeignKey()) {
                return column.getColumnDependency().getDependency().getTable().getGenerator().getRow().getJClass();
            } else {
                return column.getPrimitiveType(model());
            }
        }
    }

    private class BasicColumns extends BaseColumns {
        @Override
        protected boolean addToColumns(Column column) {
            return !column.isPrimaryKey();
        }
    }

    private class PrimaryColumns extends BaseColumns {

        @Override
        protected boolean addToColumns(Column column) {
            return column.isPrimaryKey();
        }
    }
}
