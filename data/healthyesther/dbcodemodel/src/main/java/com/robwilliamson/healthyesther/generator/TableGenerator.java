package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

@ClassGeneratorFeatures(name = "Table", parameterName = "Table")
public class TableGenerator extends BaseClassGenerator {
    private final com.robwilliamson.healthyesther.semantic.Table mTable;
    private RowGenerator mRowGenerator;
    private PrimaryKeyGenerator mPrimaryKeyGenerator;

    public TableGenerator(
            JPackage jPackage,
            com.robwilliamson.healthyesther.semantic.Table table,
            BaseTable baseTable) throws JClassAlreadyExistsException {
        mTable = table;
        setJClass(jPackage._class(JMod.PUBLIC | JMod.FINAL, getName()));
        getJClass()._extends(baseTable.getJClass());
    }

    @Override
    public String getName() {
        return Strings.capitalize(Strings.underscoresToCamel(mTable.getName())) +
                Strings.capitalize(super.getName());
    }

    @Override
    public String getPreferredParameterName() {
        return Strings.lowerCase(Strings.underscoresToCamel(mTable.getName())) +
                Strings.capitalize(super.getPreferredParameterName());
    }

    public com.robwilliamson.healthyesther.semantic.Table getTable() {
        return mTable;
    }

    public RowGenerator getRow() {
        return mRowGenerator;
    }

    public void init() throws JClassAlreadyExistsException {
        mRowGenerator = new RowGenerator(this);

        mPrimaryKeyGenerator = new PrimaryKeyGenerator(this);

        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                build();
            }
        });
    }

    public PrimaryKeyGenerator getPrimaryKeyGenerator() {
        return mPrimaryKeyGenerator;
    }

    private void build() {
        mRowGenerator.init();
    }
}
