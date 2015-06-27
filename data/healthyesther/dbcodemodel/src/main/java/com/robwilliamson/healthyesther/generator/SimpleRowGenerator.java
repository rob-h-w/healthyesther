package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleRowGenerator extends RowGenerator {
    private JMethod mConstructor;

    public SimpleRowGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super(tableGenerator);

        mConstructor = getJClass().constructor(JMod.PUBLIC);
    }

    @Override
    public void init() {
        initConstructor();
    }

    private void initConstructor() {
        List<Column> nullable = new ArrayList<>();
        List<Column> nonNull = new ArrayList<>();

        for (Column column : getTableGenerator().getTable().getColumns()) {
            if (column.isNotNull()) {
                nonNull.add(column);
            } else {
                nullable.add(column);
            }
        }

        Column.Comparator comparator = new Column.Comparator();
        Collections.sort(nullable, comparator);
        Collections.sort(nonNull, comparator);

        for (Column column : nonNull) {
            mConstructor.param(primitiveType(column), name(column));
        }

        for (Column column : nullable) {
            mConstructor.param(nullableType(column), name(column));
        }
    }
}
