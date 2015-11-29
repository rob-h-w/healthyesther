package com.robwilliamson.healthyesther.test;

import android.app.Instrumentation;
import android.content.Context;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;

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

        try (InputStream v3InputStream = testContext.getResources().openRawResource(v3DbId)) {
            Utils.File.copy(v3InputStream, dbPath);
        }
    }

    public static void deleteDatabase(Context targetContext) throws NoSuchFieldException, IllegalAccessException {
        HealthDbHelper helper = HealthDbHelper.getInstance();
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
                HealthDbHelper.getInstance().getDatabaseName()).getAbsolutePath();
    }

    public static int countEntries() {
        com.robwilliamson.healthyesther.db.includes.Database database = HealthDbHelper.getDatabase();

        EventTable.Row[] rows = DatabaseAccessor.EVENT_TABLE.select(database, WhereContains.all());
        return rows.length;
    }
}
