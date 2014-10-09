package com.robwilliamson.db.use;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.definition.Meal;
import com.robwilliamson.db.definition.Table;

public abstract class GetAllMealsQuery extends GetAllValuesQuery {
    @Override
    public String getTableName() {
        return Meal.TABLE_NAME;
    }

    @Override
    public String getOrderColumn() {
        return Meal.NAME;
    }

    @Override
    public String getOrder() {
        return "NOCASE ASC";
    }

    @Override
    public String[] getResultColumns() {
        return Table.cleanName(new String [] {
                Meal._ID,
                Meal.NAME
        });
    }
}
