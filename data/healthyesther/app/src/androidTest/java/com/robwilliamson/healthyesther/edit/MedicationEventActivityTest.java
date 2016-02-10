package com.robwilliamson.healthyesther.edit;

import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.MedicationTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.test.EditAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MedicationEventActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.robwilliamson.healthyesther.test.Strings.from;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class MedicationEventActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private static final String MED_NAME = "Paracetamol";

    public MedicationEventActivityTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        HomeActivityAccessor.setShowNavigationDrawer(false, getInstrumentation().getTargetContext());

        Utils.Db.TestData.cleanOldData();

        getActivity();

        HomeActivityAccessor.AddMode.start();
    }

    public void testOkAbsentWhenNameEmpty() {
        onView(HomeActivityAccessor.AddMode.medicationScoreButton()).perform(click());

        onView(EditAccessor.ok()).check(doesNotExist());
    }

    public void testUiInBothOrientations() {
        onView(HomeActivityAccessor.AddMode.medicationScoreButton()).perform(click());

        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return MedicationEventActivityTest.this;
            }

            @Override
            public void checkContent() {
                onView(MedicationEventActivityAccessor.medTitle()).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
                onView(MedicationEventActivityAccessor.medName()).check(matches(withHint(from(R.string.enter_medication_name))));
            }
        });
    }

    public void testTextPersistence() {
        onView(HomeActivityAccessor.AddMode.medicationScoreButton()).perform(click());

        onView(MedicationEventActivityAccessor.medName()).perform(typeText(MED_NAME));

        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return MedicationEventActivityTest.this;
            }

            @Override
            public void checkContent() {
                onView(MedicationEventActivityAccessor.medName()).check(matches(withText(MED_NAME)));
                onView(EditAccessor.eventEditText()).check(matches(withText(MED_NAME)));
            }
        });
    }

    public void testAddNewMed() {
        onView(HomeActivityAccessor.AddMode.medicationScoreButton()).perform(click());

        onView(MedicationEventActivityAccessor.medName()).perform(typeText(MED_NAME));

        onView(EditAccessor.ok()).perform(click());

        Database db = HealthDbHelper.getDatabase();
        MedicationTable.Row medication = DatabaseAccessor.MEDICATION_TABLE.select0Or1(db, WhereContains.any());
        assertThat(medication, not(is((MedicationTable.Row) null)));
        //noinspection ConstantConditions
        assertThat(medication.getName(), is(MED_NAME));
    }
}
