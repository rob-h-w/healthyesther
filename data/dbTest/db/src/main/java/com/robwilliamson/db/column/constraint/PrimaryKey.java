package com.robwilliamson.db.column.constraint;

public class PrimaryKey extends Constraint {
    private boolean mAutoIncrement = false;

    public PrimaryKey autoIncrement() {
        mAutoIncrement = true;
        return this;
    }

    @Override
    public String toString() {
        if (mAutoIncrement) {
            return "PRIMARY KEY AUTOINCREMENT";
        }

        return "PRIMARY KEY";
    }
}
