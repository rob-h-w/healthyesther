/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.integration;

import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.includes.Cursor;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.Where;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DatabaseWrapperClassTest {
    private static final String TABLE_NAME = "table name";
    private static final String WHERE = "where";
    private static final String COLUMN_NAME = "column name";
    private static final int COLUMN_INDEX = 0;
    @Mock
    Table mTable;
    @Mock
    Where mWhere;
    @Mock
    private SQLiteDatabase mDatabase;
    @InjectMocks
    private DatabaseWrapperClass mDatabaseWrapperClass;
    @Mock
    private android.database.Cursor mAndroidCursor;
    @InjectMocks
    private DatabaseWrapperClass.Cursor mCursor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        doReturn(TABLE_NAME).when(mTable).getName();
        doReturn(WHERE).when(mWhere).getWhere();

        doReturn(COLUMN_INDEX).when(mAndroidCursor).getColumnIndex(COLUMN_NAME);
    }

    @Test
    public void select_returnsCursorFromQueryWithWhereAndTable() {
        Cursor cursor = mDatabaseWrapperClass.select(mWhere, mTable);

        verify(mDatabase).query(false, TABLE_NAME, null, WHERE, new String[]{}, null, null, null, null);
        assertThat(cursor, is(not((Cursor) null)));
    }

    @Test
    public void getTransaction_returnsATransaction() {
        com.robwilliamson.healthyesther.db.includes.Transaction transaction = mDatabaseWrapperClass.getTransaction();

        assertThat(transaction, is(not((Transaction) null)));
        assertThat(transaction, instanceOf(com.robwilliamson.healthyesther.db.integration.Transaction.class));
    }

    @Test
    public void cursor_getBooleanTrue_returnsTrue() {
        doReturn(2).when(mAndroidCursor).getInt(COLUMN_INDEX);

        assertThat(mCursor.getBoolean(COLUMN_NAME), is(true));
    }

    @Test
    public void cursor_getBooleanFalse_returnsFalse() {
        doReturn(0).when(mAndroidCursor).getInt(COLUMN_INDEX);

        assertThat(mCursor.getBoolean(COLUMN_NAME), is(false));
    }

    @Test
    public void cursor_getBooleanMultipleTimes_callsCursorGetColumnIndexOnce() {
        doReturn(1).when(mAndroidCursor).getInt(COLUMN_INDEX);

        mCursor.getBoolean(COLUMN_NAME);
        mCursor.getBoolean(COLUMN_NAME);
        mCursor.getBoolean(COLUMN_NAME);

        verify(mAndroidCursor).getColumnIndex(COLUMN_NAME);
    }

    @Test
    public void cursor_getDouble_callsThrough() {
        mCursor.getDouble(COLUMN_NAME);

        verify(mAndroidCursor).getDouble(COLUMN_INDEX);
    }

    @Test
    public void cursor_getLong_callsThrough() {
        mCursor.getLong(COLUMN_NAME);

        verify(mAndroidCursor).getLong(COLUMN_INDEX);
    }

    @Test
    public void cursor_getString_callsThrough() {
        mCursor.getString(COLUMN_NAME);

        verify(mAndroidCursor).getString(COLUMN_INDEX);
    }

    @Test
    public void cursor_getDateTime_callsGetString() {
        mCursor.getDateTime(COLUMN_NAME);

        verify(mAndroidCursor).getString(COLUMN_INDEX);
    }

    @Test
    public void cursor_moveToFirst_callsThrough() {
        mCursor.moveToFirst();

        verify(mAndroidCursor).moveToFirst();
    }

    @Test
    public void cursor_moveToNext_callsThrough() {
        doReturn(true).when(mAndroidCursor).moveToNext();

        assertThat(mCursor.moveToNext(), is(true));

        verify(mAndroidCursor).moveToNext();

        doReturn(false).when(mAndroidCursor).moveToNext();

        assertThat(mCursor.moveToNext(), is(false));
    }

    @Test
    public void cursor_count_callsThrough() {
        doReturn(0).when(mAndroidCursor).getCount();

        assertThat(mCursor.count(), is(0));

        verify(mAndroidCursor).getCount();
    }
}
