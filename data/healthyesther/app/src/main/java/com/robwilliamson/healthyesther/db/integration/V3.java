/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.integration;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import javax.annotation.Nonnull;

public class V3 implements Upgrade {
    @Override
    public void upgradeFrom(@Nonnull Transaction transaction) {
        SQLiteDatabase db = transaction.db();
        try {
            db.beginTransaction();
            try (
                    android.database.Cursor cursor = db.query(
                            "health_score",
                            new String[]{"_id"},
                            "name == ?",
                            new String[]{"Drowsiness"},
                            null,
                            null,
                            null)) {
                final int idID = cursor.getColumnIndex("_id");
                cursor.moveToFirst();
                long id = cursor.getLong(idID);
                ContentValues value = new ContentValues();
                value.put("min_label", "Awake");
                value.put("max_label", "Sleepy");
                db.update(
                        "health_score",
                        value,
                        "_id == ?",
                        new String[]{String.valueOf(id)});
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
    }
}
