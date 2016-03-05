package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;

import java.util.ArrayList;
import java.util.Collections;
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

    public static Map<String, ColumnField> makeMap(List<Column> columns, Maker maker, Column.Picker picker) {
        Map<String, ColumnField> map = new HashMap<>(columns.size());
        for (Column column : columns) {
            if (!picker.pick(column)) {
                continue;
            }
            map.put(column.getName(), maker.make(column));
        }
        return map;
    }

    public static List<ColumnField> makeSortedList(Map<String, ColumnField> map, Column.Picker picker) {
        List<ColumnField> list = new ArrayList<>(map.size());
        List<Column> columns = new ArrayList<>(map.size());

        for (Map.Entry<String, ColumnField> entry : map.entrySet()) {
            columns.add(entry.getValue().column);
        }

        Collections.sort(columns, new Column.Comparator());

        for (Column column : columns) {
            ColumnField field = map.get(column.getName());
            if (!picker.pick(field.column)) {
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
