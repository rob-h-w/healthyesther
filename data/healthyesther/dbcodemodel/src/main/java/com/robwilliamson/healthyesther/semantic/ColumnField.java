package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnField {
    public final String name;
    public final JFieldVar fieldVar;
    public final Column column;

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

    public static Map<String, ColumnField> makeMap(JDefinedClass owningClass, List<Column> columns) {
        return makeMap(columns, new DefaultMaker(owningClass));
    }

    public static Map<String, ColumnField> makeMap(List<Column> columns, Maker maker) {
        Map<String, ColumnField> map = new HashMap<>(columns.size());
        for (Column column : columns) {
            map.put(column.getName(), maker.make(column));
        }
        return map;
    }

    public static List<ColumnField> makeSortedList(JDefinedClass owningClass, List<Column> columns) {
        return makeSortedList(makeMap(owningClass, columns));
    }

    public static List<ColumnField> makeSortedList(Map<String, ColumnField> map) {
        List<ColumnField> list = new ArrayList<>(map.size());

        String[] names = new String[map.size()];
        map.keySet().toArray(names);
        Arrays.sort(names);

        for (String name : names) {
            list.add(map.get(name));
        }

        return list;
    }

    public static String name(Column column) {
        return Strings.lowerCase(Strings.underscoresToCamel(column.getName()));
    }

    public static String memberName(Column column) {
        return "m" + Strings.capitalize(name(column));
    }

    public ColumnField(JDefinedClass owningClass, Column column) {
        this.column = column;
        name = name(column);
        fieldVar = owningClass.field(JMod.PRIVATE, column.getDependentJtype(owningClass.owner()), memberName(column));
    }

    public ColumnField(JFieldVar fieldVar, Column column) {
        this.name = name(column);
        this.fieldVar = fieldVar;
        this.column = column;
    }

    public ColumnField(ColumnField other) {
        this.name = other.name;
        this.fieldVar = other.fieldVar;
        this.column = other.column;
    }
}
