package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import java.util.HashSet;
import java.util.Set;

public class SimpleRow extends Row {
    private JMethod mConstructor;

    public SimpleRow(Table table) throws JClassAlreadyExistsException {
        super(table);

        mConstructor = getJClass().constructor(JMod.PUBLIC);
    }

    @Override
    public void init() {
        initConstructor();
    }

    private void initConstructor() {
        Set<Column> nullable = new HashSet<>();
        Set<Column> nonNull = new HashSet<>();

        for (Column column : getTableGenerator().getTable().getColumns()) {
            if (column.isNotNull()) {
                nonNull.add(column);
            } else {
                nullable.add(column);
            }
        }

        for (Column column : nonNull) {
            mConstructor.param(primitiveType(column), name(column));
        }

        for (Column column : nullable) {
            mConstructor.param(nullableType(column), name(column));
        }
    }
}
