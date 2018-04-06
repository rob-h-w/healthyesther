/*
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.content.Intent;

import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.edit.MealEventActivity;
import com.robwilliamson.healthyesther.edit.MedicationEventActivity;
import com.robwilliamson.healthyesther.edit.NoteEventActivity;
import com.robwilliamson.healthyesther.edit.ScoreEventActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowIntent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HomeActivityTest {
    private TestableHomeActivity mActivity;

    @Mock
    private EventTable.Row mRow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ActivityController<TestableHomeActivity> mActivityController = Robolectric.buildActivity(TestableHomeActivity.class);
        mActivity = mActivityController.get();
    }

    @Test
    public void whenEventSelectedWithMeal_launchesMealIntent() {
        whenEventSelected_launchesActivityType(EventTypeTable.MEAL.getId(), MealEventActivity.class);
    }

    @Test
    public void whenEventSelectedWithMedication_launchesMedicationIntent() {
        whenEventSelected_launchesActivityType(EventTypeTable.MEDICATION.getId(), MedicationEventActivity.class);
    }

    @Test
    public void whenEventSelectedWithHealth_launchesHealthScoreIntent() {
        whenEventSelected_launchesActivityType(EventTypeTable.HEALTH.getId(), ScoreEventActivity.class);
    }

    @Test
    public void whenEventSelectedWithNote_launchesNoteIntent() {
        whenEventSelected_launchesActivityType(EventTypeTable.NOTE.getId(), NoteEventActivity.class);
    }

    private <T> void whenEventSelected_launchesActivityType(T primaryKey, Class activityClass) {
        //noinspection ResultOfMethodCallIgnored
        doReturn(primaryKey).when(mRow).getTypeId();
        mActivity.onEventSelected(mRow);
        ShadowIntent shadowIntent = ((ShadowIntent) Shadow.extract(mActivity.startActivityIntent));
        assertThat(shadowIntent.getIntentClass(), is(equalTo(activityClass)));

    }

    private static class TestableHomeActivity extends HomeActivity {
        public Intent startActivityIntent = null;

        @Override
        public void startActivity(Intent intent) {
            startActivityIntent = intent;
            super.startActivity(intent);
        }
    }
}
