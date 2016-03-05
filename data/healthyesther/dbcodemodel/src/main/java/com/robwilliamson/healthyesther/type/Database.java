package com.robwilliamson.healthyesther.type;

import com.robwilliamson.healthyesther.semantic.Table;

import java.util.List;

public class Database {
    @SuppressWarnings("unused")
    private String type;
    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    private DbObject[] objects;

    private transient List<Table> mTables;

    public Database() {
    }

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
