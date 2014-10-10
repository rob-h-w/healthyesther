package com.robwilliamson.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.DateTime;

/**
 * Accessor for the health database.
 */
public final class HealthDbHelper extends SQLiteOpenHelper {
    private static volatile HealthDbHelper sInstance = null;

    public static synchronized HealthDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HealthDbHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private HealthDbHelper(Context context) {
        super(context.getApplicationContext(), Contract.NAME, null, Contract.VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        super.onConfigure(sqLiteDatabase);

        if (!sqLiteDatabase.isReadOnly()) {
            sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Contract.getInstance().create(sqLiteDatabase);
        fakeData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int from, int to) {
        Contract.getInstance().upgrade(sqLiteDatabase, from, to);
    }

    private void fakeData(SQLiteDatabase db) {
        if (/*BuildConfig.DEBUG*/ true) {
            Utils.Db.TestData.insertFakeData(db);
        }
    }

    public void cleanOldData(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) {
            try {
                db.beginTransaction();
                Contract contract = Contract.getInstance();
                contract.delete(db);
                contract.create(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
