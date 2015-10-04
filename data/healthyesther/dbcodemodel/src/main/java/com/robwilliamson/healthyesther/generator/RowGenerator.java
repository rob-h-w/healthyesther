package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.semantic.BaseField;
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
import com.sun.codemodel.JOp;
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
    private final JMethod mCursorConstructor;
    private final JMethod mJoinConstructor;
    private final JMethod mValueConstructor;
    private PrimaryColumns mPrimaryKeyColumns;
    private BasicColumns mBasicColumns;
    private JFieldVar mColumnNames;
    private JFieldVar mInsertNames;
    private JFieldVar mUpdateNames;
    private JMethod mApplyToRows;
    private Map<Column, JMethod> mSetters = new HashMap<>();

    public RowGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        mTableGenerator = tableGenerator;

        JDefinedClass clazz = mTableGenerator.getJClass()._class(
                JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                getName());

        setJClass(clazz);

        mCursorConstructor = clazz.constructor(JMod.PUBLIC);
        mValueConstructor = clazz.constructor(JMod.PUBLIC);

        if (getTableGenerator().getTable().hasDependencies()) {
            mJoinConstructor = clazz.constructor(JMod.PUBLIC);
        } else {
            mJoinConstructor = null;
        }
    }

    public void init() {
        mPrimaryKeyColumns = new PrimaryColumns();
        mBasicColumns = new BasicColumns();
        // Create a static list of field names.
        JClass stringListType = model().ref(ArrayList.class);
        stringListType = stringListType.narrow(String.class);
        mColumnNames = getJClass().field(
                JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                stringListType,
                "COLUMN_NAMES");

        mColumnNames.init(JExpr._new(stringListType).arg(JExpr.lit(mTableGenerator.getTable().getColumns().length)));

        if (hasRowIdPrimaryKey()) {
            mInsertNames = getJClass().field(
                    JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                    stringListType,
                    "COLUMN_NAMES_FOR_INSERTION");
            mInsertNames.init(JExpr._new(stringListType).arg(JExpr.lit(mTableGenerator.getTable().getColumns().length - 1)));
        }

        mUpdateNames = getJClass().field(
                JMod.PUBLIC | JMod.STATIC | JMod.FINAL,
                stringListType,
                "COLUMN_NAMES_FOR_UPDATE");
        mUpdateNames.init(JExpr._new(stringListType).arg(JExpr.lit(mBasicColumns.columns.size())));

        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                asyncInit();
                makeAccessors();
                makeConstructors();
                makeApplyToRows();
                makeInsert();
                makeUpdate();
                makeRemove();
                makeEquals();
            }
        });
    }

    private void makeApplyToRows() {
        if (!mTableGenerator.getTable().hasDependencies()) {
            return;
        }

        mApplyToRows = getJClass().method(JMod.PRIVATE, model().VOID, "applyToRows");
        final JVar transaction = mApplyToRows.param(Transaction.class, "transaction");
        final JBlock body = mApplyToRows.body();

        // Ensure row dependencies are up to date.
        Column.Visitor<BaseColumns> rowDependencyUpdater = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                RowField rowField = context.rowFieldFor(column);
                if (rowField == null) {
                    return;
                }

                JBlock rowPresent = body._if(rowField.fieldVar.ne(JExpr._null()))._then();
                rowPresent.invoke(rowField.fieldVar, "applyTo").arg(transaction);
                ColumnField columnField = context.columnFieldFor(column);
                if (columnField != null && rowField.rowGenerator.hasRowIdPrimaryKey()) {
                    rowPresent.assign(columnField.fieldVar, callGetNextPrimaryKey(null, rowField.fieldVar));
                }
            }
        };

        mPrimaryKeyColumns.forEach(rowDependencyUpdater);
        mBasicColumns.forEach(rowDependencyUpdater);
    }

    private void asyncInit() {
        JDefinedClass clazz = getJClass();
        JClass baseClass = model().ref(BaseRow.class).narrow(mTableGenerator.getPrimaryKeyGenerator().getJClass());
        clazz._extends(baseClass);

        // Sort the columns
        List<Column> allColumns = Arrays.asList(getTableGenerator().getTable().getColumns());
        Collections.sort(allColumns, new Column.Comparator());

        // Get a class constructor body.
        JBlock classConstructor = clazz.init();
        for (Column column : allColumns) {
            // Populate the field names.
            classConstructor
                    .invoke(mColumnNames, "add")
                    .arg(JExpr.lit(column.getName()));

            if (mInsertNames != null && !column.isRowId()) {
                classConstructor.invoke(mInsertNames, "add")
                        .arg(JExpr.lit(column.getName()));
            }

            if (!column.isPrimaryKey()) {
                classConstructor.invoke(mUpdateNames, "add").arg(JExpr.lit(column.getName()));
            }
        }
    }

    private void makeUpdate() {
        JMethod update = getJClass().method(JMod.PROTECTED, model().VOID, "update");
        update.annotate(Override.class);
        JVar transaction = update.param(Transaction.class, "transaction");
        JBlock body = update.body();

        if (mPrimaryKeyColumns.columns.isEmpty() || mBasicColumns.columns.isEmpty()) {
            body._throw(JExpr._new(model()._ref(UnsupportedOperationException.class)));
            return;
        }

        body._if(JExpr.invoke("isInDatabase").not())._then()._throw(JExpr._new(model()._ref(BaseTransactable.UpdateFailed.class)).arg(JExpr.lit("Could not update because the row is not in the database.")));

        if (mApplyToRows != null) {
            body.invoke(mApplyToRows).arg(transaction);
        }

        JInvocation where = callGetConcretePrimaryKey(null, null);

        final JInvocation updateInvocation = transaction.invoke(update).arg(where).arg(mUpdateNames);

        Column.Visitor<BaseColumns> addUpdateArg = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                if (column.isForeignKey()) {
                    PrimaryKeyGenerator foreignKeyGenerator = context.rowFieldFor(column).rowGenerator.getTableGenerator().getPrimaryKeyGenerator();
                    updateInvocation.arg(context.columnFieldFor(column).fieldVar.invoke(foreignKeyGenerator.getRowIdGetter()));
                } else {
                    updateInvocation.arg(context.columnFieldFor(column).fieldVar);
                }
            }
        };
        mBasicColumns.forEach(addUpdateArg);

        JVar actual = body.decl(model().INT, "actual", updateInvocation);

        JExpression expected = JExpr.lit(1);
        body._if(actual.ne(expected))._then()._throw(JExpr._new(model()._ref(BaseTransactable.UpdateFailed.class)).arg(expected).arg(actual));

        setIsModifiedCall(body, false);
    }

    private void makeRemove() {
        JMethod remove = getJClass().method(JMod.PROTECTED, model().VOID, "remove");
        remove.annotate(Override.class);
        JVar transaction = remove.param(Transaction.class, "transaction");
        JBlock body = remove.body();

        if (mPrimaryKeyColumns.columns.isEmpty()) {
            body._throw(JExpr._new(model()._ref(UnsupportedOperationException.class)));
            return;
        }

        body._if(JExpr.invoke("isInDatabase").not().cor(JExpr.invoke("isDeleted")))._then()._return();

        JInvocation where = JExpr.invoke(null, "getConcretePrimaryKey");

        JVar actual = body.decl(model().INT, "actual", transaction.invoke(remove).arg(where));

        JExpression expected = JExpr.lit(1);
        body._if(actual.ne(expected))._then()._throw(JExpr._new(model()._ref(BaseTransactable.RemoveFailed.class)).arg(expected).arg(actual));
        setIsDeletedCall(body, true);

        JDefinedClass anonymousType = model().anonymousClass(
                Transaction.CompletionHandler.class);
        JBlock callback = anonymousType.method(
                JMod.PUBLIC,
                model().VOID,
                "onCompleted").body();

        setIsInDatabaseCall(callback, false);

        body.invoke(transaction, "addCompletionHandler").arg(JExpr._new(anonymousType));
    }

    private void setIsDeletedCall(JBlock block, boolean isDeleted) {
        block.invoke("setIsDeleted").arg(JExpr.lit(isDeleted));
    }

    private void makeEquals() {
        JMethod eqMethod = makeBasicEquals();
        JDefinedClass theClass = getJClass();
        final JBlock body = eqMethod.body();
        final JVar other = eqMethod.listParams()[0];
        final JVar otherType = body.decl(theClass, "the" + theClass.name(), JExpr.cast(theClass, other));

        Column.Visitor<BaseColumns> makeEqualsCheck = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                ColumnField field = context.columnFieldFor(column);
                JBlock ifBlock = body._if(makeEquals(field.fieldVar, otherType.ref(field.fieldVar)).not())._then();
                ifBlock._return(JExpr.lit(false));
            }
        };
        mBasicColumns.forEach(makeEqualsCheck);

        JVar nextPrimaryKey = body.decl(getTableGenerator().getPrimaryKeyGenerator().getJClass(), "nextPrimaryKey", callGetNextPrimaryKey(null, null));
        JVar oNextPrimaryKey = body.decl(getTableGenerator().getPrimaryKeyGenerator().getJClass(), "otherNextPrimaryKey", callGetNextPrimaryKey(null, otherType));
        body._if(makeEquals(nextPrimaryKey, oNextPrimaryKey).not())._then()._return(JExpr.lit(false));

        JVar primaryKey = body.decl(getTableGenerator().getPrimaryKeyGenerator().getJClass(), "primaryKey", callGetConcretePrimaryKey(null, null));
        JVar oPrimaryKey = body.decl(getTableGenerator().getPrimaryKeyGenerator().getJClass(), "otherPrimaryKey", callGetConcretePrimaryKey(null, otherType));
        body._if(makeEquals(primaryKey, oPrimaryKey).not())._then()._return(JExpr.lit(false));

        body._return(JExpr.lit(true));
    }

    private void makeInsert() {
        TableGenerator generator = getTableGenerator();
        final PrimaryKeyGenerator keyGenerator = generator.getPrimaryKeyGenerator();
        JDefinedClass primaryKeyType = keyGenerator.getJClass();
        JMethod insert = getJClass().method(JMod.PROTECTED, Object.class, "insert");
        insert.annotate(Override.class);
        final JVar transaction = insert.param(Transaction.class, "transaction");
        final JBlock body = insert.body();

        // Ensure row dependencies are up to date.
        if (mApplyToRows != null) {
            body.directStatement("// Ensure all keys are updated from any rows passed.");
            body.invoke(mApplyToRows).arg(transaction);
        }

        if (keyGenerator.hasPrimaryKeys()) {
            // If we need to create a primary key upfront:
            final JVar nextPrimaryKey;
            final JBlock doConstruction;
            final JBlock updateRowId;

            final JInvocation newPrimaryKey = JExpr._new(primaryKeyType);

            final JInvocation insertionCall;

            if (hasRowIdPrimaryKey()) {
                nextPrimaryKey = body.decl(primaryKeyType, "nextPrimaryKey", callGetNextPrimaryKey(null, null));
                JConditional ifConstruct = body._if(nextPrimaryKey.eq(JExpr._null()));
                doConstruction = ifConstruct._then();
                updateRowId = ifConstruct._else();
                insertionCall = JExpr.invoke(transaction, "insert");
            } else {
                doConstruction = null;
                updateRowId = null;
                body.directStatement("// This table does not use a row ID as a primary key.");

                Column.Visitor<BaseColumns> addConstructorArgument = new Column.Visitor<BaseColumns>() {
                    @Override
                    public void visit(Column column, BaseColumns context) {
                        newPrimaryKey.arg(context.columnFieldFor(column).fieldVar);
                    }
                };

                mPrimaryKeyColumns.forEach(addConstructorArgument);
                body.invoke(null, "setNextPrimaryKey").arg(newPrimaryKey);
                nextPrimaryKey = body.decl(primaryKeyType, "nextPrimaryKey", callGetNextPrimaryKey(null, null));
                insertionCall = body.invoke(transaction, "insert");
            }

            if (mInsertNames == null) {
                insertionCall.arg(mColumnNames);
            } else {
                insertionCall.arg(mInsertNames);
            }
            Column.Visitor<BaseColumns> addInsertionArgument = new Column.Visitor<BaseColumns>() {
                @Override
                public void visit(Column column, BaseColumns context) {
                    if (column.isRowId()) {
                        return;
                    }

                    ColumnField field = context.columnFieldFor(column);
                    if (column.isPrimaryKey()) {

                        if (column.isForeignKey()) {
                            PrimaryKeyGenerator foreignKeyGenerator = context.rowFieldFor(column).rowGenerator.getTableGenerator().getPrimaryKeyGenerator();
                            insertionCall.arg(nextPrimaryKey.invoke(keyGenerator.getGetterFor(column)).invoke(foreignKeyGenerator.getRowIdGetter()));
                        } else {
                            insertionCall.arg(nextPrimaryKey.invoke(keyGenerator.getGetterFor(column)));
                        }
                    } else {
                        if (column.isForeignKey()) {
                            insertionCall.arg(field.fieldVar.invoke("getId"));
                        } else {
                            insertionCall.arg(field.fieldVar);
                        }
                    }
                }
            };

            mPrimaryKeyColumns.forEach(addInsertionArgument);
            mBasicColumns.forEach(addInsertionArgument);

            JDefinedClass anonymousType = model().anonymousClass(
                    Transaction.CompletionHandler.class);
            JBlock callback = anonymousType.method(
                    JMod.PUBLIC,
                    model().VOID,
                    "onCompleted").body();

            if (hasRowIdPrimaryKey() && doConstruction != null) { // Null check added to remove warning.
                body.directStatement("// This table uses a row ID as a primary key.");

                // If we have to create a primary key after insertion:
                for (Column column : mPrimaryKeyColumns.columns) {
                    if (column.isRowId()) {
                        newPrimaryKey.arg(insertionCall);
                    } else {
                        newPrimaryKey.arg(mPrimaryKeyColumns.columnFieldFor(column).fieldVar);
                    }
                }
                doConstruction.invoke(null, "setNextPrimaryKey").arg(newPrimaryKey);
                if (updateRowId != null) {
                    updateRowId.invoke(nextPrimaryKey, "setId").arg(insertionCall);
                }
            }

            setIsModifiedCall(body, false);
            setIsInDatabaseCall(callback, true);
            callMethod("updatePrimaryKeyFromNext", callback, null);

            body.invoke(transaction, "addCompletionHandler").arg(JExpr._new(anonymousType));
            body._return(nextPrimaryKey);
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

        for (Column column : mBasicColumns.columns) {
            if (column.isPrimaryKey()) {
                continue;
            }

            invocation = invocation.arg(mBasicColumns.columnFieldFor(column).fieldVar);
        }

        return invocation;
    }

    private void makeConstructors() {
        Column.Picker picker = new Column.Picker() {
            @Override
            public boolean pick(Column column) {
                return !(column.isPrimaryKey() && !column.isForeignKey() && column.getName().equals("_id"));

            }
        };

        makeCursorConstructor();
        makeJoinConstructor(picker);
        makeValueConstructor(picker);
    }

    private
    @Nonnull
    String getCursorMethodFor(Column column) {
        return "get" + Strings.capitalize(column.getPrimitiveType(model()).name());
    }

    private void makeCursorConstructor() {
        final JVar cursor = mCursorConstructor.param(Cursor.class, "cursor");
        cursor.annotate(Nonnull.class);

        final JBlock body = mCursorConstructor.body();
        final JInvocation newPrimaryKey = JExpr._new(getTableGenerator().getPrimaryKeyGenerator().getJClass());

        Column.Visitor<RowGenerator.BaseColumns> populateConstruction = new Column.Visitor<RowGenerator.BaseColumns>() {
            @Override
            public void visit(Column column, RowGenerator.BaseColumns context) {
                String columnName = column.getName();
                JInvocation getValue = cursor.invoke(getCursorMethodFor(column)).arg(columnName);
                if (column.isPrimaryKey()) {
                    if (column.isForeignKey()) {
                        PrimaryKeyGenerator keyGenerator = column.getColumnDependency().getDependency().getTable().getGenerator().getPrimaryKeyGenerator();
                        JType primaryKeyType = keyGenerator.getJClass();
                        newPrimaryKey.arg(JOp.cond(getValue.ne(JExpr._null()), JExpr._new(primaryKeyType).arg(getValue), JExpr._null()));
                    } else {
                        newPrimaryKey.arg(getValue);
                    }
                } else {
                    if (column.isForeignKey()) {
                        JMethod setter = mSetters.get(column);
                        if (setter == null) {
                            return;
                        }

                        PrimaryKeyGenerator keyGenerator = column.getColumnDependency().getDependency().getTable().getGenerator().getPrimaryKeyGenerator();
                        JType primaryKeyType = keyGenerator.getJClass();
                        JInvocation getLong = cursor.invoke("getLong").arg(columnName);
                        body.invoke(setter).arg(JOp.cond(getLong.ne(JExpr._null()), JExpr._new(primaryKeyType).arg(getLong), JExpr._null()));
                    } else {
                        JMethod setter = mSetters.get(column);
                        if (setter == null) {
                            return;
                        }
                        body.invoke(setter).arg(getValue);
                    }
                }
            }
        };

        mPrimaryKeyColumns.forEach(populateConstruction);
        mBasicColumns.forEach(populateConstruction);

        callSetPrimaryKey(body, null).arg(newPrimaryKey);
    }

    private void makeJoinConstructor(Column.Picker picker) {
        if (mJoinConstructor == null) {
            return;
        }

        Map<Column, JVar> primaryParams = mPrimaryKeyColumns.makeRowParamsFor(mJoinConstructor, picker);
        Map<Column, JVar> basicParams = mBasicColumns.makeRowParamsFor(mJoinConstructor, picker);

        for (Column column : mPrimaryKeyColumns.columns) {
            RowField rowField = mPrimaryKeyColumns.rowFieldFor(column);
            JVar param = primaryParams.get(column);
            if (param == null) {
                continue;
            }

            if (rowField != null) {
                mJoinConstructor.body().assign(rowField.fieldVar, param);
            } else {
                mJoinConstructor.body().assign(mPrimaryKeyColumns.columnFieldFor(column).fieldVar, param);
            }
        }

        for (Column column : mBasicColumns.columns) {
            RowField rowField = mBasicColumns.rowFieldFor(column);
            JVar param = basicParams.get(column);
            if (param == null) {
                continue;
            }

            if (rowField != null) {
                mJoinConstructor.body().assign(rowField.fieldVar, param);
            } else {
                mJoinConstructor.body().assign(mBasicColumns.columnFieldFor(column).fieldVar, param);
            }
        }
    }

    private void makeValueConstructor(Column.Picker picker) {
        Map<Column, JVar> primaryParams = mPrimaryKeyColumns.makeValueParamsFor(mValueConstructor, picker);
        Map<Column, JVar> basicParams = mBasicColumns.makeValueParamsFor(mValueConstructor, picker);
        PrimaryKeyGenerator primaryKeyGenerator = mTableGenerator.getPrimaryKeyGenerator();

        if (!hasRowIdPrimaryKey()) {
            JInvocation primaryKeyConstruction = JExpr._new(primaryKeyGenerator.getJClass());
            for (Column column : mPrimaryKeyColumns.columns) {
                JVar param = primaryParams.get(column);
                if (param != null) {
                    primaryKeyConstruction.arg(primaryParams.get(column));
                }
            }
            callSetPrimaryKey(mValueConstructor.body(), null).arg(primaryKeyConstruction);
        }

        for (Column column : mBasicColumns.columns) {
            JVar param = basicParams.get(column);
            if (param != null) {
                ColumnField field = mBasicColumns.columnFieldFor(column);
                mValueConstructor.body().assign(field.fieldVar, basicParams.get(column));
            }
        }
    }

    private JInvocation callSetPrimaryKey(JBlock body, JExpression expression) {
        return body.invoke(expression, "setPrimaryKey");
    }

    private JInvocation callGetConcretePrimaryKey(JBlock body, JExpression expression) {
        return callMethod("getConcretePrimaryKey", body, expression);
    }

    private JInvocation callGetNextPrimaryKey(JBlock body, JExpression expression) {
        return callMethod("getNextPrimaryKey", body, expression);
    }

    private JInvocation callMethod(String name, JBlock body, JExpression expression) {
        if (body == null) {
            return JExpr.invoke(expression, name);
        }

        return body.invoke(expression, name);
    }

    private void annotateNonull(JVar param, boolean nonnull) {
        if (nonnull) {
            param.annotate(Nonnull.class);
        }
    }

    private void makeAccessors() {
        Column.Visitor<BaseColumns> makeAccessor = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                ColumnField field = context.columnFieldFor(column);
                JMethod setter = getJClass().method(
                        JMod.PUBLIC,
                        model().VOID,
                        "set" + Strings.capitalize(field.name));
                JVar value = setter.param(field.fieldVar.type(), field.name);
                JBlock body = setter.body();
                body._if(makeEquals(field.fieldVar, value))._then()._return();
                body.assign(field.fieldVar, value);
                setIsModifiedCall(body, true);

                mSetters.put(column, setter);

                JMethod getter = getJClass().method(
                        JMod.PUBLIC,
                        field.fieldVar.type(),
                        "get" + Strings.capitalize(field.name));
                getter.body()._return(field.fieldVar);
            }
        };

        mBasicColumns.forEach(makeAccessor);
    }

    public TableGenerator getTableGenerator() {
        return mTableGenerator;
    }

    private boolean hasRowIdPrimaryKey() {
        return mPrimaryKeyColumns.columns.size() == 1 && mPrimaryKeyColumns.columns.get(0).isRowId();
    }

    private abstract class BaseColumns {
        public final List<Column> columns = new ArrayList<>();
        private Map<Column, RowField> mColumnToRowFieldMap = new HashMap<>();
        private Map<Column, ColumnField> mColumnToColumnFieldMap = new HashMap<>();

        public BaseColumns() {
            Column.Picker picker = getColumnPicker();
            for (Column column : mTableGenerator.getTable().getColumns()) {
                if (picker.pick(column)) {
                    this.columns.add(column);
                    if (!column.isRowId()) {
                        mColumnToColumnFieldMap.put(column, makeColumnFieldFor(column));
                    }
                    if (column.isForeignKey()) {
                        mColumnToRowFieldMap.put(column, makeRowFieldFor(column));
                    }
                }
            }
            Collections.sort(this.columns, new Column.Comparator());
        }

        public void forEach(Column.Visitor<BaseColumns> visitor) {
            for (Column column : columns) {
                visitor.visit(column, this);
            }
        }

        public RowField rowFieldFor(Column column) {
            return mColumnToRowFieldMap.get(column);
        }

        public ColumnField columnFieldFor(Column column) {
            return mColumnToColumnFieldMap.get(column);
        }

        public Map<Column, JVar> makeValueParamsFor(JMethod method, Column.Picker picker) {
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
            return new RowField(getJClass(), column, row, BaseField.name(column.getName()) + "Row");
        }

        protected abstract Column.Picker getColumnPicker();

        private JType getValueParamType(Column column) {
            if (column.isForeignKey()) {
                return column.getColumnDependency().getDependency().getTable().getGenerator().getPrimaryKeyGenerator().getJClass();
            } else {
                return column.getPrimitiveType(model());
            }
        }

        public Map<Column, JVar> makeRowParamsFor(JMethod method, Column.Picker picker) {
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
        protected Column.Picker getColumnPicker() {
            return new Column.BasicPicker();
        }
    }

    private class PrimaryColumns extends BaseColumns {

        @Override
        protected Column.Picker getColumnPicker() {
            return new Column.PrimaryPicker();
        }
    }
}
