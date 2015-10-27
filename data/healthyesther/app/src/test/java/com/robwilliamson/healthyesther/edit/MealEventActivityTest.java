package com.robwilliamson.healthyesther.edit;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.Where;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditMealFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.List;

import test.HealthDatabaseAccessor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MealEventActivityTest {
    @NonNull
    private ActivityController<TestableMealEventActivity> mActivityController = Robolectric.buildActivity(TestableMealEventActivity.class);

    @NonNull
    private TestableMealEventActivity mActivity = mActivityController.get();

    @Mock
    private Database mDatabase;

    @Mock
    private Transaction mTransaction;

    @Mock
    private Cursor mCursor;

    @Mock
    private EditMealFragment mMealFragment;

    @Mock
    private TransactionExecutor.QueryOperation mMealInitQuery;

    @Mock
    private EditEventFragment mEditEventFragment;

    @Mock
    private MealTable.Row mMealTableRow;

    @Mock
    private MealTable.PrimaryKey mMealPrimaryKey;

    @Mock
    private EventTable.Row mEventTableRow;

    @Mock
    private EventTable.PrimaryKey mEventPrimaryKey;

    @Mock
    private MealEventTable mMealEventTable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        doReturn(mCursor).when(mDatabase).select(any(Where.class), any(Table.class));
        doReturn(mTransaction).when(mDatabase).getTransaction();
        doReturn(mMealTableRow).when(mMealFragment).getRow();
        doReturn(mEventTableRow).when(mEditEventFragment).getRow();

        mActivity.mEditEventFragment = mEditEventFragment;
        mActivity.mMealFragment = mMealFragment;

        doReturn(mMealInitQuery).when(mMealFragment).getInitializationQuery();

        doReturn(new Query[]{}).when(mEditEventFragment).getQueries();
        doReturn(new Query[]{}).when(mMealFragment).getQueries();

        doReturn(mMealPrimaryKey).when(mMealTableRow).getNextPrimaryKey();
        doReturn(mEventPrimaryKey).when(mEventTableRow).getNextPrimaryKey();

        doReturn(1L).when(mMealPrimaryKey).getId();
        doReturn(2L).when(mEventPrimaryKey).getId();

        HealthDatabaseAccessor.INSTANCE.setMealEventTable(mMealEventTable);
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
    public void getEditFragmentsWhenShownWithFalse_returnsAListOfExistingFragmentTagPairs() {
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

    @Test
    public void modifyOperation_callsFinish() {
        doReturn(new MealEventTable.Row[]{}).when(mMealEventTable).select(eq(mDatabase), any(Where.class));

        mActivity.onModifySelected().doTransactionally(mDatabase, mTransaction);

        assertThat(mActivity.finishCalled, is(true));
    }

    @Test
    public void modifyOperationWith0LengthMealEvent_createsMealAndEventAndMealEvent() {
        doReturn(mCursor).when(mDatabase).select(any(Where.class), any(Table.class));
    }

    private static class TestableMealEventActivity extends MealEventActivity {
        public boolean finishCalled = false;
        public EditMealFragment mMealFragment;
        public EditEventFragment mEditEventFragment;

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
        protected EditEventFragment getEventFragment() {
            return mEditEventFragment;
        }

        @Override
        protected EditMealFragment getMealFragment() {
            return mMealFragment;
        }

        @Override
        public void finish() {
            finishCalled = true;
            super.finish();
        }

        @Override
        protected void setBusy(boolean busy) {
            // TODO: Find a way to avoid the countdown latch lock.
            //super.setBusy(busy);
        }
    }
}
