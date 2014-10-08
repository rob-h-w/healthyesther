package com.robwilliamson.db.use;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.definition.Meal;
import com.robwilliamson.db.definition.Table;

public abstract class GetAllMealsQuery implements SelectQuery {
    @Override
    public String[] getResultColumns() {
        return Table.cleanName(new String [] {
                Meal._ID,
                Meal.NAME
        });
    }

    @Override
    public Cursor query(SQLiteDatabase db) {
        Cursor cursor = null;
        Contract c = Contract.getInstance();
        try {
            db.beginTransaction();
            cursor = db.query(Meal.TABLE_NAME, getResultColumns(), null, null, null, null, Meal.NAME + " COLLATE NOCASE ASC");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }
}
