package com.robwilliamson.healthyesther.db.integration;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Where;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

public class Transaction implements com.robwilliamson.healthyesther.db.includes.Transaction {
    private static final String LOG_TAG = Transaction.class.getName();
    private final Set<CompletionHandler> mCompletionHandlers = new HashSet<>();
    @Nonnull
    private DatabaseWrapperClass mDatabase;
    private volatile boolean mIsInTransaction;

    Transaction(@Nonnull DatabaseWrapperClass databaseWrapperClass) {
        mDatabase = databaseWrapperClass;
        db().beginTransaction();
        mIsInTransaction = true;
    }

    @Nonnull
    SQLiteDatabase db() {
        return mDatabase.getSqliteDatabase();
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
                o = null;
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
            } else if (clazz == DateTime.class && o != null) {
                values.put(columnName, ((DateTime) o).getString());
            }
            index++;
        }

        return values;
    }

    @Override
    public void addCompletionHandler(@Nonnull CompletionHandler handler) {
        assertInTransaction();
        mCompletionHandlers.add(handler);
    }

    @Override
    public void execSQL(@Nonnull String sql) {
        assertInTransaction();
        db().execSQL(sql);
    }

    @Override
    public long insert(@Nonnull String table, @Nonnull List<String> columnNames, @Nonnull Object... columnValues) {
        assertInTransaction();
        return db().insertOrThrow(table, null, valuesFrom(columnNames, columnValues));
    }

    @Override
    public int update(@Nonnull String table, @Nonnull Where where, @Nonnull List<String> columnNames, @Nonnull Object... columnValues) {
        assertInTransaction();
        return db().update(table, valuesFrom(columnNames, columnValues), where.getWhere(), null);
    }

    @Override
    public int remove(@Nonnull String table, @Nonnull Where where) {
        assertInTransaction();
        return db().delete(table, where.getWhere(), null);
    }

    @Override
    public void commit() {
        if (!mIsInTransaction) {
            return;
        }

        db().setTransactionSuccessful();
        db().endTransaction();

        mIsInTransaction = false;

        for (CompletionHandler handler : mCompletionHandlers) {
            handler.onCompleted();
        }
    }

    @Override
    public void rollBack() {
        if (!mIsInTransaction) {
            return;
        }

        db().endTransaction();

        mIsInTransaction = false;
    }

    @Override
    public Database getDatabase() {
        return HealthDbHelper.getDatabaseWrapper();
    }

    @Override
    public void close() {
        rollBack();
    }

    private void assertInTransaction() {
        if (!mIsInTransaction) {
            throw new IllegalStateException("Cannot use a transaction after it's closed!");
        }
    }

    public static class NullColumnValueException extends RuntimeException {
        NullColumnValueException(String column) {
            super("Column " + column + " was provided a null value. Column value lists should replace null values with class types, i.e. for a null String, use String.class.");
        }
    }
}
