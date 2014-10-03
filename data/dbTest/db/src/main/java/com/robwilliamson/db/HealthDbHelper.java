package com.robwilliamson.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Accessor for the health database.
 */
public class HealthDbHelper extends SQLiteOpenHelper {
    private static volatile HealthDbHelper sInstance = null;
    public static synchronized HealthDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HealthDbHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private HealthDbHelper(Context context) {
        super(context, Contract.NAME, null, Contract.VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        if (!sqLiteDatabase.isReadOnly()) {
            sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contract.Event.CREATE_TABLE);
        sqLiteDatabase.execSQL(Contract.EventType.CREATE_TABLE);
        sqLiteDatabase.execSQL(Contract.MealEvent.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
