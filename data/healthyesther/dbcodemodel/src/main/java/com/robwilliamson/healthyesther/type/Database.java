package com.robwilliamson.healthyesther.type;

import com.robwilliamson.healthyesther.semantic.Table;

import java.util.List;

public class Database {
    private String type;
    private String name;
    private DbObject[] objects;

    private volatile transient List<Table> mTables;

    public Database() {}

    public String getName() {
        return name;
    }

    public synchronized List<Table> getTables() {
        if (mTables == null) {
            mTables = Table.getTables(objects);
        }

        return mTables;
    }
}
