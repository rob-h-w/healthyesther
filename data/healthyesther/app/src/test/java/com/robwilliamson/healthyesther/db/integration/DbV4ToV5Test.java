package com.robwilliamson.healthyesther.db.integration;


import com.robwilliamson.healthyesther.App;
import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.WhereContains;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DbV4ToV5Test {
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        HealthDbHelper.closeDb();

        InputStream inputStream = this.getClass().getResourceAsStream("/4.db3");
        if (inputStream == null) {
            fail("Could not open the version 4 database.");
        }

        String dbFolder = App.getInstance().getDatabasePath(HealthDatabase.FILE_NAME).getParentFile().getAbsolutePath();

        File f = new File(dbFolder + HealthDatabase.FILE_NAME);
        try {
            try (FileOutputStream outputStream = new FileOutputStream(f)) {
                int readBytes;
                byte[] buffer = new byte[4096];
                while ((readBytes = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, readBytes);
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void whenOpeningV4_keepsScores() {
        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row[] scoreTypes = HealthDatabase.HEALTH_SCORE_TABLE.select(db, WhereContains.any());

        assertThat(scoreTypes.length, is(3));
    }
}
