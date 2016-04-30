package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.sun.codemodel.JArray;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ClassGeneratorFeatures(name = "Database", parameterName = "Db")
public class Database extends BaseClassGenerator {
    private final com.robwilliamson.healthyesther.type.Database mDb;
    private JFieldVar mTables;

    public Database(
            com.robwilliamson.healthyesther.type.Database database,
            JPackage jPackage) throws JClassAlreadyExistsException {
        mDb = database;

        setJClass(jPackage._class(JMod.PUBLIC | JMod.FINAL, getName()));

        makeConstructor();
        makeStaticFileName();
        makeTables();
        makeCreate();
        makeDrop();
        makeUpgrade();
    }

    private void makeUpgrade() {
        JMethod upgrade = getJClass().method(JMod.PUBLIC | JMod.STATIC, model().VOID, "upgrade");
        JVar transaction = upgrade.param(Transaction.class, "transaction");
        JVar from = upgrade.param(model().INT, "from");
        JVar to = upgrade.param(model().INT, "to");
        upgrade.body().staticInvoke(model().ref(com.robwilliamson.healthyesther.db.includes.Database.class), "upgrade").arg(transaction).arg(from).arg(to).arg(mTables);
    }

    private void makeDrop() {
        JMethod drop = getJClass().method(JMod.PUBLIC | JMod.STATIC, model().VOID, "drop");
        JVar transaction = drop.param(Transaction.class, "transaction");
        drop.body().staticInvoke(model().ref(com.robwilliamson.healthyesther.db.includes.Database.class), "drop").arg(transaction).arg(mTables);
    }

    private void makeCreate() {
        JMethod create = getJClass().method(JMod.PUBLIC | JMod.STATIC, model().VOID, "create");
        JVar transaction = create.param(Transaction.class, "transaction");
        create.body().staticInvoke(model().ref(com.robwilliamson.healthyesther.db.includes.Database.class), "create").arg(transaction).arg(mTables);
    }

    @Override
    public String getName() {
        return Strings.capitalize(mDb.getName()) + super.getName();
    }

    @Override
    public String getPreferredParameterName() {
        return mDb.getName() + super.getPreferredParameterName();
    }

    private JFieldVar makeStaticFileName() {
        return getJClass().field(JMod.STATIC | JMod.FINAL | JMod.PUBLIC, model().ref(String.class), "FILE_NAME", JExpr.lit(mDb.getName() + ".db3"));
    }

    private JMethod makeConstructor() {
        return getJClass().constructor(JMod.PRIVATE);
    }

    private void makeTables() throws JClassAlreadyExistsException {
        JPackage jPackage = getJClass().getPackage();
        JArray tablesList = JExpr.newArray(model()._ref(Table.class));

        final List<com.robwilliamson.healthyesther.semantic.Table> tables = mDb.getTables();
        final Map<String, TableGenerator> tableGenerators = new HashMap<>(tables.size());
        for (com.robwilliamson.healthyesther.semantic.Table table : tables) {
            TableGenerator tableGenerator = new TableGenerator(
                    jPackage,
                    table);

            table.setGenerator(tableGenerator);

            JFieldVar tableField = getJClass().field(
                    JMod.PUBLIC | JMod.FINAL | JMod.STATIC,
                    tableGenerator.getJClass(),
                    Strings.constantName(tableGenerator.getPreferredParameterName()),
                    JExpr._new(tableGenerator.getJClass()));

            tablesList.add(tableField);
            tableGenerators.put(table.getName(), tableGenerator);
        }

        for (Map.Entry<String, TableGenerator> generator : tableGenerators.entrySet()) {
            generator.getValue().init();
        }

        mTables = getJClass().field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, model()._ref(Table.class).array(), "TABLES", tablesList);
    }
}
