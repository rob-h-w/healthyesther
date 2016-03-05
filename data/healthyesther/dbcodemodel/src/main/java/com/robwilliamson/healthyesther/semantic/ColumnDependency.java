package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.type.Column;

import javax.annotation.Nonnull;

public class ColumnDependency {
    public final Column dependent;
    public final String table;
    public final String column;

    private Column mDependency;

    public ColumnDependency(
            @Nonnull
            Column dependent,
            @Nonnull
            String table,
            @Nonnull
            String column) {
        this.dependent = dependent;
        this.table = table;
        this.column = column;
    }

    @Nonnull
    public Column getDependency() {
        if (mDependency != null) {
            return mDependency;
        }

        throw new NullPointerException("Dependency not set for " + table + "." + column);
    }

    public void setDependency(@Nonnull Column dependency) {
        mDependency = dependency;
    }
}
