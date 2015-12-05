package com.robwilliamson.healthyesther.edit;

import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.HomeActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.test.EditEventAccessor;
import com.robwilliamson.healthyesther.test.HomeActivityAccessor;
import com.robwilliamson.healthyesther.test.MealEventActivityAccessor;
import com.robwilliamson.healthyesther.test.MedicationEventActivityAccessor;
import com.robwilliamson.healthyesther.test.Orientation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.robwilliamson.healthyesther.test.Strings.from;

public class MedicationEventActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
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

    public void testAddNewMeal() {
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

        final String medName = "Paracetamol";
        onView(MedicationEventActivityAccessor.medName()).perform(typeText(medName));

        Orientation.check(new Orientation.Subject() {
            @Override
            public InstrumentationTestCase getTestCase() {
                return MedicationEventActivityTest.this;
            }

            @Override
            public void checkContent() {
                onView(MedicationEventActivityAccessor.medName()).check(matches(withText(medName)));
            }
        });

        onView(EditEventAccessor.ok()).perform(click());
    }
}
