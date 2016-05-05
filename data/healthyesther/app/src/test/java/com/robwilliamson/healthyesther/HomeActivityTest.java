/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

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
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.util.ActivityController;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricGradleTestRunner.class)
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
        doReturn(primaryKey).when(mRow).getTypeId();
        mActivity.onEventSelected(mRow);
        ShadowIntent shadowIntent = ((ShadowIntent) ShadowExtractor.extract(mActivity.startActivityIntent));
        assertThat(shadowIntent.getIntentClass(), is(equalTo(activityClass)));

    }

    private static class TestableHomeActivity extends HomeActivity {
        public Intent startActivityIntent = null;

        /**
         * Same as {@link #startActivity(Intent, Bundle)} with no options
         * specified.
         *
         * @param intent The intent to start.
         * @throws ActivityNotFoundException
         * @see {@link #startActivity(Intent, Bundle)}
         * @see #startActivityForResult
         */
        @Override
        public void startActivity(Intent intent) {
            startActivityIntent = intent;
            super.startActivity(intent);
        }
    }
}
