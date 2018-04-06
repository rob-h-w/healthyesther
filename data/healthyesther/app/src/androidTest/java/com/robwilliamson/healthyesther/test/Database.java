/*
   © Robert Williamson 2014-2016.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.test;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;

import junit.framework.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class Database {
    public static void useV4Database() throws IOException {
        Context testContext = InstrumentationRegistry.getContext();

        // Record the string before we delete the db because deletion also resets HealthDbHelper.
        String dbPath = getDatabaseAbsolutePath();

        deleteDatabase();

        int v4DbId = testContext.getResources().getIdentifier("v4", "raw", testContext.getPackageName());

        try (InputStream v4InputStream = testContext.getResources().openRawResource(v4DbId)) {
            Utils.File.copy(v4InputStream, dbPath);
        }
    }

    public static void useV3Database() throws IOException {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context testContext = instrumentation.getContext();

        // Record the string before we delete the db because deletion also resets HealthDbHelper.
        String dbPath = getDatabaseAbsolutePath();

        deleteDatabase();

        int v3DbId = testContext.getResources().getIdentifier("v3", "raw", testContext.getPackageName());

        try (InputStream v3InputStream = testContext.getResources().openRawResource(v3DbId)) {
            Utils.File.copy(v3InputStream, dbPath);
        }
    }

    public static void deleteDatabase() {
        HealthDbHelper.closeDb();

        File file = new File(getDatabaseAbsolutePath());

        if (file.exists()) {
            Assert.assertTrue("Unable to delete " + file.getAbsolutePath(), file.delete());
        }
    }

    /**
     * Careful with this, it's a hack. It uses HealthDbHelper to get the path of the database.
     * HealthDbHelper's life must be managed carefully in order to set up the conditions of first
     * application startup with an old Db, and new code.
     */
    private static String getDatabaseAbsolutePath() {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getDatabasePath(
                HealthDbHelper.getInstance().getDatabaseName()).getAbsolutePath();
    }

    public static int countEntries() {
        com.robwilliamson.healthyesther.db.includes.Database database = HealthDbHelper.getDatabase();

        EventTable.Row[] rows = DatabaseAccessor.EVENT_TABLE.select(database, WhereContains.any());
        return rows.length;
    }
}
