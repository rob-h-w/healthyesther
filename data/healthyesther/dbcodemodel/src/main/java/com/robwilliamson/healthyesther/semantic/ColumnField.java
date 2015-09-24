package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnField extends BaseField {
    public final Column column;

    public ColumnField(JDefinedClass owningClass, Column column) {
        super(owningClass, column.getName(), column.getDependentJtype(owningClass.owner()));
        this.column = column;
    }

    public ColumnField(JFieldVar fieldVar, Column column) {
        super(fieldVar, column.getName());
        this.column = column;
    }

    public ColumnField(ColumnField other) {
        super(other);
        this.column = other.column;
    }

    public static Map<String, ColumnField> makeMap(JDefinedClass owningClass, List<Column> columns, boolean excludePrimaryKey) {
        return makeMap(columns, new DefaultMaker(owningClass), excludePrimaryKey);
    }

    public static Map<String, ColumnField> makeMap(List<Column> columns, Maker maker, boolean excludePrimaryKey) {
        Map<String, ColumnField> map = new HashMap<>(columns.size());
        for (Column column : columns) {
            if (excludePrimaryKey && column.isPrimaryKey()) {
                continue;
            }
            map.put(column.getName(), maker.make(column));
        }
        return map;
    }

    public static List<ColumnField> makeSortedList(JDefinedClass owningClass, List<Column> columns, boolean excludePrimaryKey) {
        return makeSortedList(makeMap(owningClass, columns, excludePrimaryKey), excludePrimaryKey);
    }

    public static List<ColumnField> makeSortedList(Map<String, ColumnField> map, boolean excludePrimaryKey) {
        List<ColumnField> list = new ArrayList<>(map.size());

        String[] names = new String[map.size()];
        map.keySet().toArray(names);
        Arrays.sort(names);

        for (String name : names) {
            ColumnField field = map.get(name);
            if (excludePrimaryKey && field.column.isPrimaryKey()) {
                continue;
            }

            list.add(field);
        }

        return list;
    }

    public interface Maker {
        ColumnField make(Column column);
    }

    public static class DefaultMaker implements Maker {
        private final JDefinedClass mOwningClass;

        public DefaultMaker(JDefinedClass owningClass) {
            mOwningClass = owningClass;
        }

        @Override
        public ColumnField make(Column column) {
            return new ColumnField(mOwningClass, column);
        }
    }
}
