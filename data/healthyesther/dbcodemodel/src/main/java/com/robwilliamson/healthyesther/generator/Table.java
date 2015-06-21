package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.CodeGenerator;
import com.robwilliamson.healthyesther.Strings;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ClassGeneratorFeatures(name = "Table", parameterName = "Table")
public class Table extends BaseClassGenerator {
    private final com.robwilliamson.healthyesther.semantic.Table mTable;
    private final List<Table> mDependencies = new ArrayList<>();
    private Row mRow;

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

    public com.robwilliamson.healthyesther.semantic.Table getTable() {
        return mTable;
    }

    public void init(Map<String, Table> tableGeneratorsByName) throws JClassAlreadyExistsException {
        Set<com.robwilliamson.healthyesther.semantic.Table> dependencies = mTable.getTableDependencies();

        for (com.robwilliamson.healthyesther.semantic.Table table : dependencies) {
            Table dependency = tableGeneratorsByName.get(table.getName());

            if (dependency == null) {
                continue;
            }

            mDependencies.add(dependency);
        }

        if (getTable().hasDependencies()) {
            mRow = new JoinedRow(this);
        } else {
            mRow = new SimpleRow(this);
        }

        CodeGenerator.ASYNC.schedule(new Runnable() {
            @Override
            public void run() {
                build();
            }
        });
    }

    private void build() {
        mRow.init();
    }
}
