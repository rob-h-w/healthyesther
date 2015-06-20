package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.type.DbObject;

import java.util.ArrayList;
import java.util.List;

public class Table extends DbObject {
    public static List<Table> getTables(DbObject[] objects) {
        List<Table> tables = new ArrayList<>(objects.length);

        for (DbObject object : objects) {
            if (object.isTable()) {
                tables.add(new Table(object));
            }
        }

        return tables;
    }

    public Table(DbObject table) {
        if (!table.isTable()) {
            throw new IllegalArgumentException("The DbObject must be a table object.");
        }

        table.copyTo(this);
    }
}
