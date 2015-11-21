package com.robwilliamson.healthyesther.test;

import android.app.Instrumentation;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseWrapperClass;

import junit.framework.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public final class Database {
    public static void useV3Database(Instrumentation instrumentation) throws IOException, NoSuchFieldException, IllegalAccessException {
        Context testContext = instrumentation.getContext();
        Context targetContext = instrumentation.getTargetContext();

        // Record the string before we delete the db because deletion also resets HealthDbHelper.
        String dbPath = getDatabaseAbsolutePath(targetContext);

        deleteDatabase(targetContext);

        int v3DbId = testContext.getResources().getIdentifier("v3", "raw", testContext.getPackageName());

        InputStream v3InputStream = testContext.getResources().openRawResource(v3DbId);

        try {
            Utils.File.copy(v3InputStream, dbPath);
        } finally {
            v3InputStream.close();
        }
    }

    public static void deleteDatabase(Context targetContext) throws NoSuchFieldException, IllegalAccessException {
        HealthDbHelper helper = HealthDbHelper.getInstance(targetContext);
        helper.close();

        File file = new File(getDatabaseAbsolutePath(targetContext));

        if (file.exists()) {
            Assert.assertTrue("Unable to delete " + file.getAbsolutePath(), file.delete());
        }

        Field sInstance = HealthDbHelper.class.getDeclaredField("sInstance");
        sInstance.setAccessible(true);
        sInstance.set(null, null);
    }

    /**
     * Careful with this, it's a hack. It uses HealthDbHelper to get the path of the database.
     * HealthDbHelper's life must be managed carefully in order to set up the conditions of first
     * application startup with an old Db, and new code.
     */
    public static String getDatabaseAbsolutePath(Context targetContext) {
        return targetContext.getDatabasePath(
                HealthDbHelper.getInstance(targetContext).getDatabaseName()).getAbsolutePath();
    }

    public static int countEntries(Context targetContext) {

        SQLiteDatabase db = HealthDbHelper.getInstance(targetContext).getWritableDatabase();
        com.robwilliamson.healthyesther.db.includes.Database database = new DatabaseWrapperClass(db);

        EventTable.Row[] rows = HealthDatabase.EVENT_TABLE.select(database, WhereContains.all());

        Cursor c = db.query(Event.TABLE_NAME, null, null, null, null, null, null);

        try {
            if (c.moveToFirst()) {
                int count = 0;
                do {
                    count++;
                } while (c.moveToNext());

                return count;
            } else {
                return 0;
            }
        } finally {
            c.close();
        }
    }
}
