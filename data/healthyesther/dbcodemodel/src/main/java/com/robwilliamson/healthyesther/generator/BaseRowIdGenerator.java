package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMod;

@ClassGeneratorFeatures(name = "Id", parameterName = "Id")
public abstract class BaseRowIdGenerator extends BaseClassGenerator {
    private final TableGenerator mTableGenerator;

    public BaseRowIdGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super();

        mTableGenerator = tableGenerator;

        setJClass(tableGenerator.getJClass()._class(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, getName()));
    }

    public TableGenerator getTableGenerator() {
        return mTableGenerator;
    }

    @Override
    public String getName() {
        return getTableGenerator().getName() + super.getName();
    }

    @Override
    public String getPreferredParameterName() {
        return getTableGenerator().getPreferredParameterName() + super.getPreferredParameterName();
    }
}
