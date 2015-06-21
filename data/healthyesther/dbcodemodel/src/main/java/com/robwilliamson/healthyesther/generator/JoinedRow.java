package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import java.util.HashSet;
import java.util.Set;

public class JoinedRow extends Row {
    private JMethod mRowConstructor;
    private JMethod mJoinConstructor;

    public JoinedRow(Table table) throws JClassAlreadyExistsException {
        super(table);

        mRowConstructor = getJClass().constructor(JMod.PUBLIC);
        mJoinConstructor = getJClass().constructor(JMod.PUBLIC);
    }

    @Override
    public void init() {
        initConstructors();
    }

    private void initConstructors() {
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
            if (column.isForeignKey()) {
                // TODO
            } else {
                mRowConstructor.param(primitiveType(column), name(column));
                mJoinConstructor.param(primitiveType(column), name(column));
            }
        }

        for (Column column : nullable) {
            if (column.isForeignKey()) {
                // TODO
            } else {
                mRowConstructor.param(nullableType(column), name(column));
                mJoinConstructor.param(nullableType(column), name(column));
            }
        }
    }
}
