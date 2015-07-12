package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.semantic.ColumnDependency;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoinedRowGenerator extends RowGenerator {
    private JMethod mRowConstructor;
    private JMethod mJoinConstructor;

    public JoinedRowGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super(tableGenerator);

        mRowConstructor = getJClass().constructor(JMod.PUBLIC);
        mJoinConstructor = getJClass().constructor(JMod.PUBLIC);
    }

    @Override
    public void init() {
        initConstructors();
    }

    private void initConstructors() {
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
            if (column.isForeignKey()) {
                addConstructorParam(column.getColumnDependency());
            } else {
                mRowConstructor.param(primitiveType(column), name(column));
                mJoinConstructor.param(primitiveType(column), name(column));
            }
        }

        for (Column column : nullable) {
            if (column.isForeignKey()) {
                addConstructorParam(column.getColumnDependency());
            } else {
                mRowConstructor.param(nullableType(column), name(column));
                mJoinConstructor.param(nullableType(column), name(column));
            }
        }
    }

    private void addConstructorParam(ColumnDependency columnDependency) {
        RowGenerator rowGenerator = columnDependency.getDependency().getTable().getGenerator().getRow();
        mRowConstructor.param(rowGenerator.getJClass(), name(rowGenerator));

        com.robwilliamson.healthyesther.semantic.Table table = rowGenerator.getTableGenerator().getTable();
        BasePrimaryKeyGenerator primaryKeyGenerator = table.getGenerator().getPrimaryKeyGenerator();
        mJoinConstructor.param(primaryKeyGenerator.getJClass(), primaryKeyGenerator.getPreferredParameterName());
    }
}
