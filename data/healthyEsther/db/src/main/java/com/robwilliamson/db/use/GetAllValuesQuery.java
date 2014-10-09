package com.robwilliamson.db.use;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.Meal;
import com.robwilliamson.db.definition.Table;

public abstract class GetAllValuesQuery implements SelectQuery {
    public abstract String getTableName();
    public abstract String getOrderColumn();
    public abstract String getOrder();

    @Override
    public Cursor query(SQLiteDatabase db) {
        Cursor cursor = null;
        Contract c = Contract.getInstance();

        String orderExpression = null;

        if (!Utils.Strings.nullOrEmpty(getOrderColumn()) &&
                !Utils.Strings.nullOrEmpty(getOrder())) {
            orderExpression = getOrderColumn() + " COLLATE " + getOrder();
        }

        try {
            db.beginTransaction();
            cursor = db.query(getTableName(), getResultColumns(), null, null, null, null, orderExpression);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }
}
