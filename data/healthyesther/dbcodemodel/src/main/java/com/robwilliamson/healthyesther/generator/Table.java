package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

@ClassGeneratorFeatures(name = "Table", parameterName = "Table")
public class Table extends BaseClassGenerator {
    private final com.robwilliamson.healthyesther.semantic.Table mTable;

    public Table(
            JPackage jPackage,
            com.robwilliamson.healthyesther.semantic.Table table,
            BaseTable baseTable) throws JClassAlreadyExistsException {
        mTable = table;
        setJClass(jPackage._class(JMod.PUBLIC | JMod.FINAL, getName()));
        getJClass()._extends(baseTable.getJClass());
    }

    @Override
    public String getName() {
        return Strings.underscoresToCamel(Strings.capitalize(mTable.getName()) + super.getName());
    }

    @Override
    public String getPreferredParameterName() {
        return mTable.getName() + super.getPreferredParameterName();
    }
}
