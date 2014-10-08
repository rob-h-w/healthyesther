package com.robwilliamson.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.robwilliamson.db.definition.Table;

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
        if (!sqLiteDatabase.isReadOnly()) {
            sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Contract.getInstance().create(sqLiteDatabase);
        fakeData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int from, int to) {
        Contract.getInstance().upgrade(sqLiteDatabase, from, to);
    }

    private void fakeData() {
        if (BuildConfig.DEBUG) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                db.beginTransaction();

                Contract contract = Contract.getInstance();

                long pieId = contract.MEAL.insert(db, "PAH!");
                long stewId = contract.MEAL.insert(db, "Stoo");

                long paracetamolId = contract.MEDICATION.insert(db, "Paracetamol");

                for (int i = 0; i < 1000; i++) {
                    int second = (31 * i / 10) % 60;
                    int minute = i % 60;
                    int hour = (127 * i / 10) % 24;
                    int day = (i / 10) % 28 + 1;
                    int type = ((13 * second * hour * i / 10) % 2) + 1;
                    DateTime now = DateTime.now();
                    DateTime then = now.withTime(hour, minute, second, 0);
                    then = then.withDate(2014, 3, day);
                    long eventId = contract.EVENT.insert(db, then, type, "Event " + i);

                    if (type == 1) {
                        contract.MEAL_EVENT.insert(db, (minute % 2 > 0) ? pieId : stewId, eventId);
                    } else {
                        contract.MEDICATION_EVENT.insert(db, paracetamolId, eventId);
                    }
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    private void cleanOldData(SQLiteDatabase db) {
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
