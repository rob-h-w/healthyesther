package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.sun.codemodel.JArray;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ClassGeneratorFeatures(name = "Database", parameterName = "Db")
public class Database extends BaseClassGenerator {
    private final com.robwilliamson.healthyesther.type.Database mDb;

    public Database(
            com.robwilliamson.healthyesther.type.Database database,
            JPackage jPackage) throws JClassAlreadyExistsException {
        mDb = database;

        setJClass(jPackage._class(JMod.PUBLIC | JMod.FINAL, getName()));

        makeStaticFileName();

        makeConstructor();
        makeCreateMethod();
        makeTables();
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
        return getJClass().constructor(JMod.PUBLIC);
    }

    private JMethod makeCreateMethod() {
        JMethod create = getJClass().method(JMod.PUBLIC | JMod.FINAL, model().VOID, "create");
        create.param(model()._ref(Transaction.class), "transaction");
        return create;
    }

    private void makeTables() throws JClassAlreadyExistsException {
        JPackage jPackage = getJClass().getPackage();
        BaseTable baseTable = new BaseTable(jPackage);

        JArray tablesList = JExpr.newArray(baseTable.getJClass());

        final List<com.robwilliamson.healthyesther.semantic.Table> tables = mDb.getTables();
        final Map<String, TableGenerator> tableGenerators = new HashMap<>(tables.size());
        for (com.robwilliamson.healthyesther.semantic.Table table : tables) {
            TableGenerator tableGenerator = new TableGenerator(
                    jPackage,
                    table,
                    baseTable);

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

        getJClass().field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, baseTable.getJClass().array(), "TABLES", tablesList);
    }
}
