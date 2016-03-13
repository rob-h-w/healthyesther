package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.BaseTransactable;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.semantic.BaseField;
import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.semantic.ColumnField;
import com.robwilliamson.healthyesther.semantic.Relation;
import com.robwilliamson.healthyesther.semantic.RowField;
import com.robwilliamson.healthyesther.semantic.Table;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ClassGeneratorFeatures(name = "Row", parameterName = "row")
public class RowGenerator extends BaseClassGenerator {
    @Nonnull
    private final TableGenerator mTableGenerator;

    @Nonnull
    private final JMethod mCursorConstructor;
    private final JMethod mJoinConstructor;

    @Nonnull
    private final JMethod mValueConstructor;
    private PrimaryColumns mPrimaryKeyColumns;
    private BasicColumns mBasicColumns;
    private JFieldVar mColumnNames;
    private JFieldVar mInsertNames;
    private JFieldVar mUpdateNames;
    private JMethod mApplyToRows;
    private Map<Column, JMethod> mSetters = new HashMap<>();

    public RowGenerator(@Nonnull TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        mTableGenerator = tableGenerator;

        JDefinedClass clazz = mTableGenerator.getJClass()._class(
                JMod.PUBLIC | JMod.STATIC,
                getName());

        setJClass(clazz);

        clazz._implements(Serializable.class);

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
        mUpdateNames.init(JExpr._new(stringListType).arg(JExpr.lit(mTableGenerator.getTable().getColumns().length)));

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
                makeIsValid();
                makeLoadRelations();
                makeGetRows();
            }
        });
    }

    private void makeGetRows() {
        if (!hasRelations()) {
            return;
        }

        Column.Visitor<BaseColumns> makeRowAccessor = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                Relation relation = Relation.getRelationOf(column);
                RowField rowField = context.rowFieldFor(column);
                if (!column.isForeignKey() || relation == null || rowField == null) {
                    return;
                }

                Table table = relation.getBaseColumn().getTable();
                RowGenerator row = table.getGenerator().getRow();

                JMethod accessor = getJClass()
                        .method(
                                JMod.PUBLIC | JMod.FINAL,
                                row.getJClass(),
                                "get" + Strings.capitalize(
                                        Strings.underscoresToCamel(table.getName()))
                                        + "Row");
                Utils.annotateNonull(accessor, column.isNotNull());
                JBlock body = accessor.body();

                if (column.isNotNull()) {
                    body
                            ._if(rowField.fieldVar.eq(JExpr._null()))
                            ._then()
                            ._throw(JExpr._new(
                                    model().ref(NullPointerException.class))
                            .arg(table.getName() + " row is not set - call loadRelations first."));
                }

                body._return(rowField.fieldVar);
            }
        };

        mPrimaryKeyColumns.forEach(makeRowAccessor);
        mBasicColumns.forEach(makeRowAccessor);
    }

    private void makeLoadRelations() {
        if (!hasRelations()) {
            return;
        }

        final JMethod loadRelations = getJClass().method(JMod.PUBLIC | JMod.FINAL, model().VOID, "loadRelations");
        final JVar databaseVar = loadRelations.param(com.robwilliamson.healthyesther.db.includes.Database.class, "database");
        final JBlock body = loadRelations.body();
        Column.Visitor<BaseColumns> makeRelation = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                Relation relation = Relation.getRelationOf(column);
                RowField rowField = context.rowFieldFor(column);
                ColumnField columnField = context.columnFieldFor(column);

                if (relation != null && rowField != null) {

                    JVar selectorValue;
                    if (columnField == null) {
                        JMethod primaryKeyGetter = context.getterForPrimaryKeyColumn(column);
                        //noinspection ConstantConditions
                        selectorValue = body.decl(primaryKeyGetter.type(), column.getVarName(),
                                callGetConcretePrimaryKey(null, null).invoke(primaryKeyGetter));
                    } else {
                        selectorValue = columnField.fieldVar;
                    }

                    String methodName;
                    final boolean isNotNull = column.isNotNull();
                    if (isNotNull) {
                        methodName = "select1";
                    } else {
                        methodName = "select0Or1";
                    }

                    Database dbGenerator = getTableGenerator().getDatabaseGenerator();
                    JFieldRef tableField = dbGenerator.getJClass().staticRef(
                            dbGenerator.getTableFieldFor(relation.getBaseColumn()));

                    JBlock bodyForThisColumn = body;

                    if (!isNotNull) {
                        bodyForThisColumn = body._if(selectorValue.ne(JExpr._null()))._then();
                    }

                    JInvocation invocation = tableField.invoke(methodName);
                    invocation.arg(databaseVar);
                    invocation.arg(selectorValue);
                    bodyForThisColumn.assign(
                            rowField.fieldVar,
                            invocation);

                    if (!relation.getBaseColumn().getTable().hasDependencies()) {
                        return;
                    }

                    if (!isNotNull) {
                        bodyForThisColumn = bodyForThisColumn
                                ._if(rowField.fieldVar.ne(JExpr._null()))
                                ._then();
                    }

                    bodyForThisColumn
                            .invoke(
                                    rowField.fieldVar,
                                    loadRelations)
                            .arg(databaseVar);
                }
            }
        };

        mPrimaryKeyColumns.forEach(makeRelation);
        mBasicColumns.forEach(makeRelation);
    }

    private boolean hasRelations() {
        //noinspection unchecked,unchecked
        for (List<Column> columns : new List[]{mPrimaryKeyColumns.columns, mBasicColumns.columns}) {
            for (Column column : columns) {
                if (Relation.getRelationOf(column) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private void makeApplyToRows() {
        if (!mTableGenerator.getTable().hasDependencies()) {
            return;
        }

        final JClass primaryKeyClass = getTableGenerator().getPrimaryKeyGenerator().getJClass();
        mApplyToRows = getJClass().method(JMod.PRIVATE, model().VOID, "applyToRows");
        final JVar transaction = mApplyToRows.param(Transaction.class, "transaction");
        Utils.annotateNonull(transaction, true);
        final JBlock body = mApplyToRows.body();
        final JVar nextPrimaryKey = getTableGenerator().getPrimaryKeyGenerator().hasForeignPrimaryKeys() ? body.decl(
                primaryKeyClass,
                "nextPrimaryKey",
                callGetNextPrimaryKey(null, null)) : null;

        // Ensure row dependencies are up to date.
        Column.Visitor<BaseColumns> updateRowDependency = new Column.Visitor<BaseColumns>() {
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

                if (column.isPrimaryKey() && column.isForeignKey() && nextPrimaryKey != null) {
                    // update the next primary key.
                    rowPresent._if(nextPrimaryKey.ne(JExpr._null()))._then().invoke(
                            nextPrimaryKey,
                            getTableGenerator().getPrimaryKeyGenerator().getSetterFor(column))
                            .arg(rowField.rowGenerator.callGetNextPrimaryKey(null, rowField.fieldVar));
                }
            }
        };

        mPrimaryKeyColumns.forEach(updateRowDependency);
        mBasicColumns.forEach(updateRowDependency);

        if (getTableGenerator().getPrimaryKeyGenerator().hasForeignPrimaryKeys() && nextPrimaryKey != null) {
            final JExpression[] makeNewPrimaryKey = {nextPrimaryKey.eq(JExpr._null())};

            Column.Visitor<BaseColumns> populateMakeNewPrimaryCondition = new Column.Visitor<BaseColumns>() {
                @Override
                public void visit(Column column, BaseColumns context) {
                    RowField rowField = context.rowFieldFor(column);
                    if (rowField != null) {
                        makeNewPrimaryKey[0] = makeNewPrimaryKey[0].cand(rowField.fieldVar.ne(JExpr._null()));
                    }
                }
            };

            mPrimaryKeyColumns.forEach(populateMakeNewPrimaryCondition);

            JBlock primaryKeyAbsent = body._if(makeNewPrimaryKey[0])._then();
            final JVar oldNextPrimaryKey = primaryKeyAbsent.decl(
                    primaryKeyClass,
                    "oldNextPrimaryKey",
                    callGetNextPrimaryKey(null, null));
            final JInvocation newPrimaryKey = JExpr._new(primaryKeyClass);

            Column.Visitor<BaseColumns> populatePrimaryKeyConstructor = new Column.Visitor<BaseColumns>() {
                @Override
                public void visit(Column column, BaseColumns context) {
                    RowField rowField = context.rowFieldFor(column);

                    if (rowField == null) {
                        ColumnField columnField = context.columnFieldFor(column);
                        if (columnField == null) {
                            // Use the value from the existing next primary key.
                            newPrimaryKey.arg(
                                    oldNextPrimaryKey.invoke(getTableGenerator().getPrimaryKeyGenerator().getGetterFor(column)));
                        } else {
                            newPrimaryKey.arg(columnField.fieldVar);
                        }
                    } else {
                        newPrimaryKey.arg(rowField.rowGenerator.callGetNextPrimaryKey(null, rowField.fieldVar));
                    }
                }
            };

            mPrimaryKeyColumns.forEach(populatePrimaryKeyConstructor);

            callSetNextPrimaryKey(primaryKeyAbsent, null).arg(newPrimaryKey);
        }
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
            String name = column.getName();

            // Populate the field names.
            classConstructor
                    .invoke(mColumnNames, "add")
                    .arg(JExpr.lit(name));

            if (name.toLowerCase().equals("when")) {
                name = "[when]";
            }

            if (mInsertNames != null && !column.isRowId()) {
                classConstructor.invoke(mInsertNames, "add")
                        .arg(JExpr.lit(name));
            }

            classConstructor.invoke(mUpdateNames, "add").arg(JExpr.lit(name));
        }
    }

    private void makeUpdate() {
        JMethod update = getJClass().method(JMod.PROTECTED, model().VOID, "update");
        update.annotate(Override.class);
        JVar transaction = update.param(Transaction.class, "transaction");
        Utils.annotateNonull(transaction, true);
        final JBlock body = update.body();

        if (mPrimaryKeyColumns.columns.isEmpty()) {
            body._throw(JExpr._new(model()._ref(UnsupportedOperationException.class)));
            return;
        }

        body._if(JExpr.invoke("isInDatabase").not())._then()._throw(JExpr._new(model()._ref(BaseTransactable.UpdateFailed.class)).arg(JExpr.lit("Could not update because the row is not in the database.")));

        if (mApplyToRows != null) {
            body.invoke(mApplyToRows).arg(transaction);
        }

        final Map<Column, JVar> nullables = objectOrClassFromNullables(body);

        JInvocation where = callGetConcretePrimaryKey(null, null);

        final JInvocation updateInvocation = transaction.invoke("update").arg(mTableGenerator.getTable().getName()).arg(where).arg(mUpdateNames);

        Column.Visitor<BaseColumns> addUpdateArg = new Column.Visitor<BaseColumns>() {
            private JVar mNextPrimaryKey = null;

            @Override
            public void visit(Column column, BaseColumns context) {
                if (nullables.containsKey(column)) {
                    updateInvocation.arg(nullables.get(column));
                } else {
                    if (column.isPrimaryKey()) {
                        // Primary key field.
                        if (column.isForeignKey()) {
                            updateInvocation.arg(getPrimaryKey().invoke(getTableGenerator().getPrimaryKeyGenerator().getGetterFor(column)).invoke("getId"));
                        } else {
                            updateInvocation.arg(getPrimaryKey().invoke(getTableGenerator().getPrimaryKeyGenerator().getGetterFor(column)));
                        }
                    } else {
                        ColumnField columnField = context.columnFieldFor(column);
                        if (columnField == null) {
                            columnField = mPrimaryKeyColumns.columnFieldFor(column);

                            if (columnField == null) {
                                throw new NullPointerException("The column, " + column.getName() + ", in table " + column.getTable().getName() + " should have a row field.");
                            }
                        }

                        if (column.isForeignKey()) {
                            RowField rowField = context.rowFieldFor(column);
                            if (rowField == null) {
                                throw new NullPointerException("Foreign key columns, like " + column.getName() + ", should have row fields.");
                            }

                            PrimaryKeyGenerator foreignKeyGenerator = rowField.rowGenerator.getTableGenerator().getPrimaryKeyGenerator();
                            updateInvocation.arg(columnField.fieldVar.invoke(foreignKeyGenerator.getRowIdGetter()));
                        } else {
                            updateInvocation.arg(columnField.fieldVar);
                        }
                    }
                }
            }

            private JVar getPrimaryKey() {
                if (mNextPrimaryKey == null) {
                    mNextPrimaryKey = body.decl(getTableGenerator().getPrimaryKeyGenerator().getJClass(), "nextPrimaryKey", callGetNextPrimaryKey(null, null));
                }

                return mNextPrimaryKey;
            }
        };
        mPrimaryKeyColumns.forEach(addUpdateArg);
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
        Utils.annotateNonull(transaction, true);
        JBlock body = remove.body();

        if (mPrimaryKeyColumns.columns.isEmpty()) {
            body._throw(JExpr._new(model()._ref(UnsupportedOperationException.class)));
            return;
        }

        body._if(JExpr.invoke("isInDatabase").not().cor(JExpr.invoke("isDeleted")))._then()._return();

        JInvocation where = JExpr.invoke(null, "getConcretePrimaryKey");

        JVar actual = body.decl(model().INT, "actual", transaction.invoke("remove").arg(mTableGenerator.getTable().getName()).arg(where));

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
                if (field == null) {
                    throw new NullPointerException("Column field for " + column.getFullyQualifiedName() + " could not be found.");
                }

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

    private Map<Column, JVar> objectOrClassFromNullables(final JBlock body) {
        final Map<Column, JVar> nullablesToObjectVariables = new HashMap<>();

        Column.Visitor<BaseColumns> getNullable = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                if (!column.isNotNull()) {
                    ColumnField columnField = context.columnFieldFor(column);

                    if (columnField == null) {
                        throw new NullPointerException("Column field for " + column.getFullyQualifiedName() + " could not be found.");
                    }

                    JFieldVar fieldVar = columnField.fieldVar;
                    JExpression value = column.isForeignKey() ? fieldVar.invoke("getId") : fieldVar;
                    JVar object = body.decl(
                            JMod.FINAL,
                            model().ref(Object.class),
                            columnField.name, JOp.cond(
                                    fieldVar.eq(JExpr._null()),
                                    JExpr.dotclass(columnField.fieldVar.type().boxify()),
                                    value));
                    nullablesToObjectVariables.put(column, object);
                }
            }
        };

        mBasicColumns.forEach(getNullable); // Primary keys should always be nonnull.

        return nullablesToObjectVariables;
    }

    private void makeInsert() {
        TableGenerator generator = getTableGenerator();
        final PrimaryKeyGenerator keyGenerator = generator.getPrimaryKeyGenerator();
        JDefinedClass primaryKeyType = keyGenerator.getJClass();
        JMethod insert = getJClass().method(JMod.PROTECTED, Object.class, "insert");
        insert.annotate(Nonnull.class);
        insert.annotate(Override.class);
        final JVar transaction = insert.param(Transaction.class, "transaction");
        Utils.annotateNonull(transaction, true);
        final JBlock body = insert.body();

        // Ensure row dependencies are up to date.
        if (mApplyToRows != null) {
            body.directStatement("// Ensure all keys are updated from any rows passed.");
            body.invoke(mApplyToRows).arg(transaction);
        }

        final Map<Column, JVar> nullables = objectOrClassFromNullables(body);

        if (keyGenerator.hasPrimaryKeys()) {
            // If we need to create a primary key upfront:
            final JVar nextPrimaryKey = body.decl(primaryKeyType, "nextPrimaryKey", callGetNextPrimaryKey(null, null));

            final JInvocation newPrimaryKey = JExpr._new(primaryKeyType);

            final JInvocation insertionCall;
            JConditional ifConstruct = body._if(nextPrimaryKey.eq(JExpr._null()));
            final JBlock doConstruction = ifConstruct._then();
            final JBlock updateRowId;

            if (hasRowIdPrimaryKey()) {
                updateRowId = ifConstruct._else();
                insertionCall = JExpr.invoke(transaction, "insert");
            } else {
                updateRowId = null;
                body.directStatement("// This table does not use a row ID as a primary key.");

                Column.Visitor<BaseColumns> addConstructorArgument = new Column.Visitor<BaseColumns>() {
                    @Override
                    public void visit(Column column, BaseColumns context) {
                        ColumnField columnField = context.columnFieldFor(column);

                        if (columnField == null) {
                            JMethod getter = context.getterForPrimaryKeyColumn(column);
                            if (getter == null) {
                                throw new NullPointerException("Expected column " + column.getName() + " to have a getter on the primary key object.");
                            }

                            newPrimaryKey.arg(callGetConcretePrimaryKey(null, null).invoke(getter));
                        } else {
                            newPrimaryKey.arg(columnField.fieldVar);
                        }
                    }
                };

                mPrimaryKeyColumns.forEach(addConstructorArgument);
                insertionCall = body.invoke(transaction, "insert");
            }

            insertionCall.arg(mTableGenerator.getTable().getName());

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
                            RowField rowField = context.rowFieldFor(column);
                            if (rowField == null) {
                                throw new NullPointerException("Row field for column " + column.getFullyQualifiedName() + " could not be found.");
                            }

                            PrimaryKeyGenerator foreignKeyGenerator = rowField.rowGenerator.getTableGenerator().getPrimaryKeyGenerator();
                            insertionCall.arg(nextPrimaryKey.invoke(keyGenerator.getGetterFor(column)).invoke(foreignKeyGenerator.getRowIdGetter()));
                        } else {
                            insertionCall.arg(nextPrimaryKey.invoke(keyGenerator.getGetterFor(column)));
                        }
                    } else {
                        if (nullables.containsKey(column)) {
                            insertionCall.arg(nullables.get(column));
                        } else {
                            if (field == null) {
                                throw new NullPointerException("Column field for " + column.getFullyQualifiedName() + " could not be found.");
                            }

                            if (column.isForeignKey()) {
                                insertionCall.arg(field.fieldVar.invoke("getId"));
                            } else {
                                insertionCall.arg(field.fieldVar);
                            }
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

            if (hasRowIdPrimaryKey()) {
                body.directStatement("// This table uses a row ID as a primary key.");

                // If we have to create a primary key after insertion:
                for (Column column : mPrimaryKeyColumns.columns) {
                    if (column.isRowId()) {
                        newPrimaryKey.arg(insertionCall);
                    } else {
                        ColumnField columnField = mPrimaryKeyColumns.columnFieldFor(column);
                        if (columnField == null) {
                            throw new NullPointerException("Could not find column field for " + column.getFullyQualifiedName() + ".");
                        }

                        newPrimaryKey.arg(columnField.fieldVar);
                    }
                }
                if (updateRowId != null) {
                    updateRowId.invoke(nextPrimaryKey, "setId").arg(insertionCall);
                }
            }

            callSetNextPrimaryKey(doConstruction, null).arg(newPrimaryKey);
            doConstruction.assign(nextPrimaryKey, callGetNextPrimaryKey(null, null));

            setIsModifiedCall(body, false);
            callMethod("updatePrimaryKeyFromNext", callback, null);

            body.invoke(transaction, "addCompletionHandler").arg(JExpr._new(anonymousType));
            setIsInDatabaseCall(body, true);
            body._return(nextPrimaryKey);
        } else {
            body._return(populateArgumentsFor(body.invoke(transaction, "insert").arg(mTableGenerator.getTable().getName()), nullables));
        }

    }

    private void makeIsValid() {
        JMethod method = getJClass().method(JMod.PUBLIC, model().BOOLEAN, "isValid");
        method.annotate(Override.class);
        final JBlock block = method.body();

        // Check nullness.
        Column.Visitor<BaseColumns> nullCheck = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                if (column.isNotNull() && column.isString()) {
                    ColumnField field = context.columnFieldFor(column);
                    if (field == null) {
                        return;
                    }

                    JExpression nullCheck = field.fieldVar.eq(JExpr._null());

                    if (column.isString()) {
                        block._if(nullCheck.cor(field.fieldVar.invoke("equals").arg(JExpr.lit(""))))._then()._return(JExpr.FALSE);
                    } else {
                        block._if(nullCheck)._then()._return(JExpr.FALSE);
                    }
                }
            }
        };

        mBasicColumns.forEach(nullCheck);
        mPrimaryKeyColumns.forEach(nullCheck);

        // Check string length.
        Column.Visitor<BaseColumns> checkStringLength = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                Integer maxLength = column.getMaxLength();
                if (maxLength != null) {
                    ColumnField field = context.columnFieldFor(column);
                    if (field == null) {
                        return;
                    }

                    JExpression lengthCheck = field.fieldVar.invoke("length").gt(JExpr.lit(maxLength));

                    if (column.isNotNull()) {
                        block._if(lengthCheck)
                                ._then()._return(JExpr.FALSE);
                    } else {
                        block._if(field.fieldVar.ne(JExpr._null()).cand(lengthCheck))
                                ._then()._return(JExpr.FALSE);
                    }
                }
            }
        };

        mBasicColumns.forEach(checkStringLength);
        mPrimaryKeyColumns.forEach(checkStringLength);

        block._return(JExpr.TRUE);
    }

    private void setIsModifiedCall(JBlock block, boolean isModified) {
        block.invoke("setIsModified").arg(JExpr.lit(isModified));
    }

    private void setIsInDatabaseCall(JBlock block, boolean isInDatabase) {
        block.invoke("setIsInDatabase").arg(JExpr.lit(isInDatabase));
    }

    private JInvocation populateArgumentsFor(@Nonnull JInvocation invocation, @Nullable Map<Column, JVar> nullables) {
        invocation = invocation.arg(mColumnNames);

        for (Column column : mBasicColumns.columns) {
            if (column.isPrimaryKey()) {
                continue;
            }

            if (nullables != null) {
                if (nullables.containsKey(column)) {
                    invocation = invocation.arg(nullables.get(column));
                    continue;
                }
            }

            ColumnField columnField = mBasicColumns.columnFieldFor(column);
            if (columnField == null) {
                throw new NullPointerException("Could not get column field for " + column.getFullyQualifiedName() + ".");
            }

            invocation = invocation.arg(columnField.fieldVar);
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

                ColumnDependency columnDependency = column.getColumnDependency();

                if (column.isPrimaryKey()) {
                    if (column.isForeignKey()) {
                        if (columnDependency == null) {
                            throw new NullPointerException("Could not get column dependency for " + column.getFullyQualifiedName() + ".");
                        }

                        PrimaryKeyGenerator keyGenerator = columnDependency.getDependency().getTable().getGenerator().getPrimaryKeyGenerator();
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
                        if (columnDependency == null) {
                            throw new NullPointerException("Could not get column dependency for " + column.getFullyQualifiedName() + ".");
                        }

                        PrimaryKeyGenerator keyGenerator = columnDependency.getDependency().getTable().getGenerator().getPrimaryKeyGenerator();
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
        setIsInDatabaseCall(body, true);
    }

    private void makeJoinConstructor(Column.Picker picker) {
        if (mJoinConstructor == null) {
            return;
        }

        JBlock body = mJoinConstructor.body();

        final Map<Column, JVar> primaryParams = mPrimaryKeyColumns.makeRowParamsFor(mJoinConstructor, picker);
        Map<Column, JVar> basicParams = mBasicColumns.makeRowParamsFor(mJoinConstructor, picker);

        for (Column column : mPrimaryKeyColumns.columns) {
            RowField rowField = mPrimaryKeyColumns.rowFieldFor(column);
            JVar param = primaryParams.get(column);
            if (param == null) {
                continue;
            }

            if (rowField != null) {
                body.assign(rowField.fieldVar, param);
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
                ColumnField columnField = mBasicColumns.columnFieldFor(column);
                if (columnField == null) {
                    throw new NullPointerException("Could not get column field for " + column.getFullyQualifiedName() + ".");
                }

                mJoinConstructor.body().assign(columnField.fieldVar, param);
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
                if (field == null) {
                    throw new NullPointerException("Could not get column field for " + column.getFullyQualifiedName() + ".");
                }

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

    private JInvocation callSetNextPrimaryKey(JBlock body, JExpression expression) {
        return callMethod("setNextPrimaryKey", body, expression);
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

    private void makeAccessors() {
        Column.Visitor<BaseColumns> makeAccessor = new Column.Visitor<BaseColumns>() {
            @Override
            public void visit(Column column, BaseColumns context) {
                ColumnField field = context.columnFieldFor(column);
                if (field == null) {
                    throw new NullPointerException("Could not get column field for " + column.getFullyQualifiedName() + ".");
                }

                JMethod setter = getJClass().method(
                        JMod.PUBLIC,
                        model().VOID,
                        "set" + Strings.capitalize(field.name));
                JVar value = setter.param(field.fieldVar.type(), field.name);
                Utils.annotateNonull(value, column.isNotNull());
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
                Utils.annotateNonull(getter, column.isNotNull());
            }
        };

        mBasicColumns.forEach(makeAccessor);
    }

    @Nonnull
    public TableGenerator getTableGenerator() {
        return mTableGenerator;
    }

    private boolean hasRowIdPrimaryKey() {
        return mPrimaryKeyColumns.columns.size() == 1 && mPrimaryKeyColumns.columns.get(0).isRowId();
    }

    private abstract class BaseColumns {
        @Nonnull
        public final List<Column> columns = new ArrayList<>();

        @Nonnull
        private Map<Column, RowField> mColumnToRowFieldMap = new HashMap<>();

        @Nonnull
        private Map<Column, ColumnField> mColumnToColumnFieldMap = new HashMap<>();

        public BaseColumns() {
            Column.Picker picker = getColumnPicker();
            Column.Picker forField = getColumnForFieldPicker();
            for (Column column : mTableGenerator.getTable().getColumns()) {
                if (picker.pick(column)) {
                    this.columns.add(column);
                    if (forField.pick(column)) {
                        mColumnToColumnFieldMap.put(column, makeColumnFieldFor(column));
                    }
                    if (column.isForeignKey()) {
                        mColumnToRowFieldMap.put(column, makeRowFieldFor(column));
                    }
                }
            }
            Collections.sort(this.columns, new Column.Comparator());
        }

        public void forEach(@Nonnull Column.Visitor<BaseColumns> visitor) {
            for (Column column : columns) {
                visitor.visit(column, this);
            }
        }

        @Nullable
        public RowField rowFieldFor(@Nonnull Column column) {
            return mColumnToRowFieldMap.get(column);
        }

        @Nullable
        public ColumnField columnFieldFor(@Nonnull Column column) {
            return mColumnToColumnFieldMap.get(column);
        }

        @Nullable
        JMethod getterForPrimaryKeyColumn(@Nonnull Column column) {
            return mTableGenerator.getPrimaryKeyGenerator().getGetterFor(column);
        }

        public Map<Column, JVar> makeValueParamsFor(JMethod method, Column.Picker picker) {
            Map<Column, JVar> map = new HashMap<>(columns.size());
            for (Column column : columns) {
                if (!picker.pick(column)) {
                    continue;
                }

                JVar param = method.param(getValueParamType(column), column.getVarName());
                Utils.annotateNonull(param, column.isNotNull());
                map.put(column, param);
            }

            return map;
        }


        protected ColumnField makeColumnFieldFor(Column column) {
            ColumnField field = new ColumnField(getJClass(), column);
            // Foreign keys could be represented as rows - can't be notnull.
            Utils.annotateNonull(field.fieldVar, column.isNotNull() && !column.isForeignKey());
            return field;
        }


        protected RowField makeRowFieldFor(Column column) {
            ColumnDependency columnDependency = column.getColumnDependency();
            if (columnDependency == null) {
                throw new NullPointerException("Could not get column dependency for " + column.getFullyQualifiedName() + ".");
            }

            RowGenerator row = columnDependency.getDependency().getTable().getGenerator().getRow();

            return new RowField(getJClass(), column, row, BaseField.name(column.getName()) + "Row");
        }

        protected abstract Column.Picker getColumnPicker();

        protected abstract Column.Picker getColumnForFieldPicker();

        private JType getValueParamType(@Nonnull Column column) {
            if (column.isForeignKey()) {
                ColumnDependency columnDependency = column.getColumnDependency();
                if (columnDependency == null) {
                    throw new NullPointerException("Could not get column dependency for " + column.getFullyQualifiedName() + ".");
                }

                return columnDependency.getDependency().getTable().getGenerator().getPrimaryKeyGenerator().getJClass();
            } else {
                return getRowParamTypeWrtNullability(column);
            }
        }

        @Nonnull
        public Map<Column, JVar> makeRowParamsFor(@Nonnull JMethod method, @Nonnull Column.Picker picker) {
            Map<Column, JVar> map = new HashMap<>(columns.size());
            for (Column column : columns) {
                if (!picker.pick(column)) {
                    continue;
                }

                JVar param = method.param(getRowParamType(column), column.getVarName());
                Utils.annotateNonull(param, column.isNotNull());
                map.put(column, param);
            }

            return map;
        }

        private JType getRowParamType(@Nonnull Column column) {
            if (column.isForeignKey()) {
                ColumnDependency columnDependency = column.getColumnDependency();
                if (columnDependency == null) {
                    throw new NullPointerException("Could not get column dependency for " + column.getFullyQualifiedName() + ".");
                }

                return columnDependency.getDependency().getTable().getGenerator().getRow().getJClass();
            } else {
                return getRowParamTypeWrtNullability(column);
            }
        }

        private JType getRowParamTypeWrtNullability(@Nonnull Column column) {
            if (column.isNotNull()) {
                return column.getPrimitiveType(model());
            }

            return column.getNullableType(model());
        }
    }

    private class BasicColumns extends BaseColumns {
        @Override
        protected Column.Picker getColumnPicker() {
            return new Column.BasicPicker();
        }

        @Override
        protected Column.Picker getColumnForFieldPicker() {
            return new Column.Picker() {
                @Override
                public boolean pick(Column column) {
                    return true;
                }
            };
        }
    }

    private class PrimaryColumns extends BaseColumns {

        @Override
        protected Column.Picker getColumnPicker() {
            return new Column.PrimaryPicker();
        }

        @Override
        protected Column.Picker getColumnForFieldPicker() {
            return new Column.Picker() {
                @Override
                public boolean pick(Column column) {
                    return false;
                }
            };
        }
    }
}
