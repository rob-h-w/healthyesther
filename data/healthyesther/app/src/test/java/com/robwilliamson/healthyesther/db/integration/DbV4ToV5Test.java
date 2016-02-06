package com.robwilliamson.healthyesther.db.integration;


import com.robwilliamson.healthyesther.App;
import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreJudgmentRangeTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.util.time.Range;

import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
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

        //noinspection ResultOfMethodCallIgnored
        new File(dbFolder).mkdirs();
        File f = new File(dbFolder + '/' + HealthDatabase.FILE_NAME);
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

        assertThat(scoreTypes.length, is(4));
    }

    @Test
    public void whenOpeningV4_createsScoreJudgmentTable() {
        Database db = HealthDbHelper.getDatabase();

        HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE.select(db, WhereContains.any());
    }

    @Test
    public void whenOpeningV4_createsJudgmentForHappiness() {
        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row happiness = HealthDatabase.HEALTH_SCORE_TABLE.select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, "Happiness"));
        HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE.select1(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, happiness.getConcretePrimaryKey().getId()));
    }

    @Test
    public void whenOpeningV4_happinessJudgmentBestValueIsSame() {
        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row happiness = HealthDatabase.HEALTH_SCORE_TABLE.select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, "Happiness"));
        HealthScoreJudgmentRangeTable.Row scoreJudgment = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE.select1(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, happiness.getConcretePrimaryKey().getId()));

        assertThat(scoreJudgment.getBestValue(), is(5L));
    }

    @Test
    public void whenOpeningV4_happinessJudgmentIsRoundTheClock() {
        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row happiness = HealthDatabase.HEALTH_SCORE_TABLE.select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, "Happiness"));
        HealthScoreJudgmentRangeTable.Row scoreJudgment = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE.select1(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, happiness.getConcretePrimaryKey().getId()));

        assertThat(scoreJudgment.getStartTime(), is((Long) null));
        assertThat(scoreJudgment.getEndTime(), is((Long) null));
    }

    @Test
    public void whenOpeningV4_happinessJudgmentIsInterpretedAsRoundTheClock() {
        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row happiness = HealthDatabase.HEALTH_SCORE_TABLE.select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, "Happiness"));
        HealthScoreJudgmentRangeTable.Row scoreJudgment = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE.select1(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, happiness.getConcretePrimaryKey().getId()));

        DateTime now = DateTimeConverter.now();
        Range range = Range.Starting(now.as(org.joda.time.DateTime.class)).from(scoreJudgment);

        assertThat(range.length(), equalTo(Duration.standardDays(1)));
    }

    @Test
    public void whenOpeningV4_creates2DrowsinessJudgments() {
        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row drowsiness = HealthDatabase.HEALTH_SCORE_TABLE.select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, "Drowsiness"));
        HealthScoreJudgmentRangeTable.Row[] scoreJudgments = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE.select(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, drowsiness.getConcretePrimaryKey().getId()));

        assertThat(scoreJudgments.length, is(2));
    }

    @Test
    public void whenOpeningV4_createsDayDrowsinessJudgment() {
        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row drowsiness = HealthDatabase.HEALTH_SCORE_TABLE.select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, "Drowsiness"));
        HealthScoreJudgmentRangeTable.Row[] scoreJudgments = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE.select(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, drowsiness.getConcretePrimaryKey().getId()));

        DateTime now = DateTimeConverter.now();
        Range day = Range.Starting(now.as(org.joda.time.DateTime.class)).from(scoreJudgments[0]);
        Range night = Range.Starting(now.as(org.joda.time.DateTime.class)).from(scoreJudgments[1]);

        if (day.from.isAfter(night.to)) {
            day = night;
        }

        assertThat(day.from.hourOfDay().get(), is(8));
        assertThat(day.to.hourOfDay().get(), is(20));
    }

    @Test
    public void whenOpeningV4_createsNightDrowsinessJudgment() {
        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row drowsiness = HealthDatabase.HEALTH_SCORE_TABLE.select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, "Drowsiness"));
        HealthScoreJudgmentRangeTable.Row[] scoreJudgments = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE.select(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, drowsiness.getConcretePrimaryKey().getId()));

        DateTime now = DateTimeConverter.now();
        Range day = Range.Starting(now.as(org.joda.time.DateTime.class)).from(scoreJudgments[0]);
        Range night = Range.Starting(now.as(org.joda.time.DateTime.class)).from(scoreJudgments[1]);

        if (day.from.isAfter(night.to)) {
            night = day;
        }

        assertThat(night.from.hourOfDay().get(), is(22));
        assertThat(night.to.hourOfDay().get(), is(6));
    }

    @Test
    public void whenOpeningV4_createsRoundTheClockJudgmentsForCustomScores() {
        Database db = HealthDbHelper.getDatabase();

        HealthScoreTable.Row foonsness = HealthDatabase.HEALTH_SCORE_TABLE.select1(db, WhereContains.columnEqualling(HealthScoreTable.NAME, "Foonsness"));
        HealthScoreJudgmentRangeTable.Row scoreJudgment = HealthDatabase.HEALTH_SCORE_JUDGMENT_RANGE_TABLE.select1(db, WhereContains.foreignKey(HealthScoreJudgmentRangeTable.SCORE_ID, foonsness.getConcretePrimaryKey().getId()));

        DateTime now = DateTimeConverter.now();
        Range range = Range.Starting(now.as(org.joda.time.DateTime.class)).from(scoreJudgment);

        assertThat(range.length(), equalTo(Duration.standardDays(1)));
    }
}
