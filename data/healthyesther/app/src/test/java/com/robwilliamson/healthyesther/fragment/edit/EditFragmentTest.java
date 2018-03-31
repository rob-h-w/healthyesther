/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment.edit;

import android.annotation.SuppressLint;
import android.content.Context;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.includes.Key;
import com.robwilliamson.healthyesther.db.includes.Transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditFragmentTest {
    private TestableEditFragment mEditFragment;

    @Mock
    private Context mContext;

    @Mock
    private TestableRow mRow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mEditFragment = new TestableEditFragment();
    }

    @Test
    public void whenRowIsNull_hasRowIsNull() {
        assertThat(mEditFragment.hasRow(), is(false));
    }

    @SuppressLint("ValidFragment")
    private static class TestableEditFragment extends EditFragment<TestableRow> {

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        protected TestableRow createRow() {
            return null;
        }

        @Override
        protected int getFragmentLayout() {
            return 0;
        }
    }

    private static class TestableRow extends BaseRow<TestablePrimaryKey> {

        @Nonnull
        @Override
        protected Object insert(@Nonnull Transaction transaction) {
            return 1;
        }

        @Override
        protected void update(@Nonnull Transaction transaction) {

        }

        @Override
        protected void remove(@Nonnull Transaction transaction) {

        }

        @Override
        public boolean isValid() {
            return false;
        }
    }

    private static class TestablePrimaryKey implements Serializable, Key {

        @Nullable
        @Override
        public String getWhere() {
            return null;
        }
    }
}
