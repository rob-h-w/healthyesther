package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ClassGeneratorFeatures(name = "Table", parameterName = "Table")
public class TableGenerator extends BaseClassGenerator {
    private final com.robwilliamson.healthyesther.semantic.Table mTable;
    private final List<TableGenerator> mDependencies = new ArrayList<>();
    private RowGenerator mRowGenerator;
    private BasePrimaryKeyGenerator mPrimaryKeyGenerator;

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
        return Strings.underscoresToCamel(Strings.capitalize(mTable.getName()) + super.getName());
    }

    @Override
    public String getPreferredParameterName() {
        return mTable.getName() + super.getPreferredParameterName();
    }

    public com.robwilliamson.healthyesther.semantic.Table getTable() {
        return mTable;
    }

    public RowGenerator getRow() {
        return mRowGenerator;
    }

    public void init(Map<String, TableGenerator> tableGeneratorsByName) throws JClassAlreadyExistsException {
        List<com.robwilliamson.healthyesther.semantic.Table> dependencies = mTable.getTableDependencies();

        for (com.robwilliamson.healthyesther.semantic.Table table : dependencies) {
            TableGenerator dependency = tableGeneratorsByName.get(table.getName());

            if (dependency == null) {
                continue;
            }

            mDependencies.add(dependency);
        }

        if (getTable().hasDependencies()) {
            mRowGenerator = new JoinedRowGenerator(this);
            mPrimaryKeyGenerator = new JoinedPrimaryKeyGenerator(this);
        } else {
            mRowGenerator = new SimpleRowGenerator(this);
            mPrimaryKeyGenerator = new SimplePrimaryKeyGenerator(this);
        }

        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                build();
            }
        });
    }

    public BasePrimaryKeyGenerator getPrimaryKeyGenerator() {
        return mPrimaryKeyGenerator;
    }

    private void build() {
        mRowGenerator.init();
    }
}
