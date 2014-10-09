package com.robwilliamson.db.definition;

import android.app.ActionBar;
import android.database.sqlite.SQLiteDatabase;

public class MedicationName extends Table {
    public static final String TABLE_NAME = "medication_name";
    public static final String MEDICATION_ID = "medication_id";
    public static final String NAME = "name";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE medication_name ( \n" +
                "    medication_id             REFERENCES medication ( _id ) ON DELETE CASCADE\n" +
                "                                                            ON UPDATE CASCADE,\n" +
                "    name          TEXT( 50 )  NOT NULL,\n" +
                "    PRIMARY KEY ( medication_id, name ) \n" +
                ");");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {

    }
}
