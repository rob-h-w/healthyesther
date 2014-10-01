package com.robwilliamson.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Accessor for the health database.
 */
public class HealthDbHelper extends SQLiteOpenHelper {
    public HealthDbHelper(Context context) {
        super(context, Contract.NAME, null, Contract.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contract.Event.CREATE_TABLE);
        sqLiteDatabase.execSQL(Contract.EventTypeTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(Contract.MealEvent.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
    }
}
