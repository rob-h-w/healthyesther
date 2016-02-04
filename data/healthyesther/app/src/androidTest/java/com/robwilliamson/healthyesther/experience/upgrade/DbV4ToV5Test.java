package com.robwilliamson.healthyesther.experience.upgrade;

import android.test.ActivityInstrumentationTestCase2;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MedicationEventTable;
import com.robwilliamson.healthyesther.db.generated.NoteEventTable;
import com.robwilliamson.healthyesther.db.includes.Where;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.test.Database;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DbV4ToV5Test extends ActivityInstrumentationTestCase2<HomeActivity> {
    public DbV4ToV5Test() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Database.useV4Database(getInstrumentation());

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        Database.deleteDatabase(getInstrumentation().getTargetContext());

        super.tearDown();
    }

    public void test_whenUpgraded_noEventDetailsWithSameEvent() {
        com.robwilliamson.healthyesther.db.includes.Database db = HealthDbHelper.getDatabase();
        EventTable.Row[] events = DatabaseAccessor.EVENT_TABLE.select(db, WhereContains.any());

        for (EventTable.Row event : events) {
            Where where = WhereContains.foreignKey(MealEventTable.EVENT_ID, event.getConcretePrimaryKey().getId());
            MealEventTable.Row[] mealEvents = DatabaseAccessor.MEAL_EVENT_TABLE.select(db, where);
            MedicationEventTable.Row[] medicationEvents = DatabaseAccessor.MEDICATION_EVENT_TABLE.select(db, where);
            HealthScoreEventTable.Row[] scoreEvents = DatabaseAccessor.HEALTH_SCORE_EVENT_TABLE.select(db, where);
            NoteEventTable.Row[] noteEvents = DatabaseAccessor.NOTE_EVENT_TABLE.select(db, where);

            assertThat(mealEvents.length + medicationEvents.length + scoreEvents.length + noteEvents.length, anyOf(is(0), is(1)));
        }
    }
}