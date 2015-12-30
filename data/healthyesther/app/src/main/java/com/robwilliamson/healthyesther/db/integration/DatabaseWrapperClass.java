package com.robwilliamson.healthyesther.db.integration;

import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Order;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class DatabaseWrapperClass extends Database {
    @Nonnull
    private final SQLiteDatabase mDatabase;

    public DatabaseWrapperClass(@Nonnull SQLiteDatabase database) {
        mDatabase = database;
    }

    @Nonnull
    @Override
    public Cursor select(@Nonnull Where where, @Nonnull Table table) {
        return new Cursor(mDatabase.query(false, table.getName(), null, where.getWhere(), new String[]{}, null, null, null, null));
    }

    @Nonnull
    @Override
    public com.robwilliamson.healthyesther.db.includes.Cursor select(@Nonnull Where where, @Nonnull Table table, @Nonnull Order order) {
        return new Cursor(mDatabase.query(false, table.getName(), null, where.getWhere(), new String[]{}, null, null, order.getOrder(), null));
    }

    @Override
    public Transaction getTransaction() {
        return new com.robwilliamson.healthyesther.db.integration.Transaction(mDatabase);
    }

    @Nonnull
    public SQLiteDatabase getSqliteDatabase() {
        return mDatabase;
    }

    public static class Cursor implements com.robwilliamson.healthyesther.db.includes.Cursor {

        @Nonnull
        private final android.database.Cursor mCursor;

        @Nonnull
        private final Map<String, Integer> map = new HashMap<>();

        Cursor(@Nonnull android.database.Cursor cursor) {
            mCursor = cursor;
        }

        private int get(@Nonnull String column) {
            if (map.containsKey(column)) {
                return map.get(column);
            }

            int index = mCursor.getColumnIndex(column);
            map.put(column, index);
            return index;
        }

        @Override
        public Boolean getBoolean(@Nonnull String column) {
            int id = get(column);
            return mCursor.isNull(id) ? null : mCursor.getInt(get(column)) != 0;
        }

        @Override
        public Double getDouble(@Nonnull String column) {
            int id = get(column);
            return mCursor.isNull(id) ? null : mCursor.getDouble(get(column));
        }

        @Override
        public Long getLong(@Nonnull String column) {
            int id = get(column);
            return mCursor.isNull(id) ? null : mCursor.getLong(id);
        }

        @Override
        public String getString(@Nonnull String column) {
            int id = get(column);
            return mCursor.isNull(id) ? null : mCursor.getString(get(column));
        }

        @Override
        public DateTime getDateTime(@Nonnull String column) {
            int id = get(column);
            return mCursor.isNull(id) ? null : new DateTime(getString(column));
        }

        @Override
        public void moveToFirst() {
            mCursor.moveToFirst();
        }

        @Override
        public boolean moveToNext() {
            return mCursor.moveToNext();
        }

        @Override
        public int count() {
            return mCursor.getCount();
        }

        @Override
        public void close() {
            mCursor.close();
        }
    }
}
