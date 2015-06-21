package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.sun.codemodel.JArray;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

@ClassGeneratorFeatures(name = "Database", parameterName = "Db")
public class Database extends BaseClassGenerator {
    private final com.robwilliamson.healthyesther.type.Database mDb;
    private final DbTransactable mTransactable;

    private final JFieldVar mFileName;

    private final JMethod mConstructor;
    private final JMethod mCreate;

    public Database(
            com.robwilliamson.healthyesther.type.Database database,
            DbTransactable transactable,
            JPackage jPackage) throws JClassAlreadyExistsException {
        mDb = database;
        mTransactable = transactable;

        setJClass(jPackage._class(JMod.PUBLIC | JMod.FINAL, getName()));

        mFileName = makeStaticFileName();

        mConstructor = makeConstructor();
        mCreate = makeCreateMethod();
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
        JMethod constructor = getJClass().constructor(JMod.PUBLIC);
        return constructor;
    }

    private JMethod makeCreateMethod() {
        JMethod create = getJClass().method(JMod.PUBLIC | JMod.FINAL, model().VOID, "create");
        create.param(mTransactable.getJClass(), mTransactable.getPreferredParameterName());
        return create;
    }

    private void makeTables() throws JClassAlreadyExistsException {
        JPackage jPackage = getJClass().getPackage();
        BaseTable baseTable = new BaseTable(jPackage);

        JArray tablesList = JExpr.newArray(baseTable.getJClass());

        for (com.robwilliamson.healthyesther.semantic.Table table : mDb.getTables()) {
            Table jTable = new Table(
                    jPackage,
                    table,
                    baseTable);

            JFieldVar tableField = getJClass().field(
                    JMod.PUBLIC | JMod.FINAL | JMod.STATIC,
                    jTable.getJClass(),
                    Strings.constantName(jTable.getPreferredParameterName()),
                    JExpr._new(jTable.getJClass()));

            tablesList.add(tableField);
        }

        JFieldVar tablesListField = getJClass().field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, baseTable.getJClass().array(), "TABLES", tablesList);
    }
}
