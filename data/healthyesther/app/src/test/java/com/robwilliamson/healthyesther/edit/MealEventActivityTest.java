package com.robwilliamson.healthyesther.edit;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MealEventActivityTest {
    @NonNull
    private ActivityController<TestableMealEventActivity> mActivityController = Robolectric.buildActivity(TestableMealEventActivity.class);

    @NonNull
    private TestableMealEventActivity mActivity = mActivityController.get();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getEditFragmentsWithTrue_returnsAListOfNewFragmentTagPairs() {
        List<Pair<EditFragment, String>> list = mActivity.getEditFragments(true);

        List<EditFragment> fragments = new ArrayList<>();
        for (Pair<EditFragment, String> pair : list) {
            fragments.add(pair.first);
        }

        assertThat(fragments.size(), is(2));
        assertThat(fragments.toArray(), hasItemInArray(instanceOf(EditMealFragment.class)));
        assertThat(fragments.toArray(), hasItemInArray(instanceOf(EditEventFragment.class)));
    }

    @Test
    public void getEditFragmnentsWhenShownWithFalse_returnsAListOfExistingFragmentTagPairs() {
        mActivityController.create().start().resume().visible();

        List<Pair<EditFragment, String>> list = mActivity.getEditFragments(false);

        List<EditFragment> fragments = new ArrayList<>();
        for (Pair<EditFragment, String> pair : list) {
            fragments.add(pair.first);
        }

        assertThat(fragments.size(), is(2));
        assertThat(fragments.toArray(), hasItemInArray(instanceOf(EditMealFragment.class)));
        assertThat(fragments.toArray(), hasItemInArray(instanceOf(EditEventFragment.class)));
    }

    private static class TestableMealEventActivity extends MealEventActivity {
        @Override
        public List<Pair<EditFragment, String>> getEditFragments(boolean create) {
            return super.getEditFragments(create);
        }

        @Override
        public int getModifyFailedStringId() {
            return super.getModifyFailedStringId();
        }

        @Override
        public TransactionExecutor.Operation onModifySelected() {
            return super.onModifySelected();
        }

        @Override
        public void onModifySelected(SQLiteDatabase db) {
            super.onModifySelected(db);
        }

        @Override
        protected void setBusy(boolean busy) {
            // TODO: Find a way to avoid the countdown latch lock.
            //super.setBusy(busy);
        }
    }
}
