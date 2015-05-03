package com.robwilliamson.healthyesther.db.use;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;

public abstract class GetSomeValuesQuery implements SelectQuery {
    public abstract String getTableName();
    public abstract String getWhereSelection();
    public abstract String getOrderColumn();
    public abstract String getOrder();

    @Override
    public Cursor query(SQLiteDatabase db) {
        Cursor cursor = null;
        Contract c = Contract.getInstance();

        String orderExpression = null;

        if (!Utils.Strings.nullOrEmpty(getOrderColumn()) &&
                !Utils.Strings.nullOrEmpty(getOrder())) {
            orderExpression = getOrderColumn() + " " + getOrder();
        }

        try {
            db.beginTransaction();
            cursor = db.query(getTableName(), getResultColumns(), getWhereSelection(), null, null, null, orderExpression);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }
}
