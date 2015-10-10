package com.robwilliamson.healthyesther.db.integration;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Where;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

public class Transaction implements com.robwilliamson.healthyesther.db.includes.Transaction {
    @Nonnull
    private final SQLiteDatabase mDb;
    private final Set<CompletionHandler> mCompletionHandlers = new HashSet<>();

    Transaction(@Nonnull SQLiteDatabase database) {
        mDb = database;
        mDb.beginTransaction();
    }

    private ContentValues valuesFrom(@Nonnull List<String> columnNames, @Nonnull Object... columnValues) {
        ContentValues values = new ContentValues();
        int index = 0;

        for (String columnName : columnNames) {
            Object o = columnValues[index];
            Class<?> clazz;
            if (o == null) {
                throw new NullColumnValueException(columnName);
            }
            if (o instanceof Class) {
                clazz = (Class<?>) o;
                if (clazz == DateTime.class) {
                    clazz = String.class;
                }
            } else {
                clazz = o.getClass();
            }

            if (clazz == String.class) {
                values.put(columnName, (String) o);
            } else if (clazz == Integer.class) {
                values.put(columnName, (Integer) o);
            } else if (clazz == Boolean.class) {
                values.put(columnName, (Boolean) o);
            } else if (clazz == Long.class) {
                values.put(columnName, (Long) o);
            } else if (clazz == DateTime.class) {
                values.put(columnName, ((DateTime) o).getString());
            }
            index++;
        }

        return values;
    }

    @Override
    public void addCompletionHandler(@Nonnull CompletionHandler handler) {
        mCompletionHandlers.add(handler);
    }

    @Override
    public void execSQL(@Nonnull String sql) {
        mDb.execSQL(sql);
    }

    @Override
    public long insert(@Nonnull String table, @Nonnull List<String> columnNames, @Nonnull Object... columnValues) {
        return mDb.insertOrThrow(table, null, valuesFrom(columnNames, columnValues));
    }

    @Override
    public int update(@Nonnull String table, @Nonnull Where where, @Nonnull List<String> columnNames, @Nonnull Object... columnValues) {
        return mDb.update(table, valuesFrom(columnNames, columnValues), where.getWhere(), null);
    }

    @Override
    public int remove(@Nonnull String table, @Nonnull Where where) {
        return mDb.delete(table, where.getWhere(), null);
    }

    @Override
    public void commit() {
        mDb.endTransaction();
        for (CompletionHandler handler : mCompletionHandlers) {
            handler.onCompleted();
        }
    }

    public static class NullColumnValueException extends RuntimeException {
        NullColumnValueException(String column) {
            super("Column " + column + " was provided a null value. Column value lists should replace null values with class types, i.e. for a null String, use String.class.");
        }
    }
}
