package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.type.Column;

public class ColumnDependency {
    public final Column dependent;
    public final String table;
    public final String column;

    private Column mDependency;

    public ColumnDependency(
            Column dependent,
            String table,
            String column) {
        this.dependent = dependent;
        this.table = table;
        this.column = column;
    }

    public Column getDependency() {
        return mDependency;
    }

    public void setDependency(Column dependency) {
        mDependency = dependency;
    }
}
