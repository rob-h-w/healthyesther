package com.robwilliamson.healthyesther.db.integration;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Where;

import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.TimeZone;

import javax.annotation.Nonnull;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TransactionTest {
    static {
        new DateTimeConverter();
    }

    private static final String TABLE = "table";
    private static final String[] COLUMN_NAMES = {
            "nullString",
            "string",
            "integer",
            "boolean",
            "long",
            "DATE_TIME",
            "nullDateTime"
    };
    private static final DateTime DATE_TIME = new DateTime(new org.joda.time.DateTime().withDate(2015, 10, 24).withTime(22, 36, 21, 0).withZoneRetainFields(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT"))));
    private static final String DATE_TIME_STRING = "2015-10-24T22:36:21 +00:00";
    private static final Object[] VALUES = {
            String.class,
            "string",
            1,
            true,
            1L,
            DATE_TIME,
            DateTime.class
    };
    private static final Object[] EXPECTED = {
            null,
            "string",
            1,
            true,
            1L,
            DATE_TIME_STRING,
            null
    };
    private static final String WHERE = "where string";

    @Mock
    private SQLiteDatabase mDatabase;

    @InjectMocks
    private Transaction mTransaction;

    @Mock
    private Where mWhere;

    @Mock
    private com.robwilliamson.healthyesther.db.includes.Transaction.CompletionHandler mCompletionHandler0;

    @Mock
    private com.robwilliamson.healthyesther.db.includes.Transaction.CompletionHandler mCompletionHandler1;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        doReturn(WHERE).when(mWhere).getWhere();
    }

    @Test
    public void constructorDbNotInTransaction_beginsTrasaction() {
        verify(mDatabase).beginTransaction();
    }

    @Test
    public void constructorDbInTransaction_doesNotBeginTransaction() {
        Mockito.reset(mDatabase);
        doReturn(true).when(mDatabase).inTransaction();
        mTransaction = new Transaction(mDatabase);

        verify(mDatabase, never()).beginTransaction();
    }

    @Test
    public void execSQL_callsThrough() {
        mTransaction.execSQL("test");

        verify(mDatabase).execSQL("test");
    }

    @Test
    public void insertWithAColumnValueOfEachType_callsInsertOrThrowOnTheDbWithTheCorrectValues() {
        mTransaction.insert(TABLE, Arrays.asList(COLUMN_NAMES), VALUES);

        ArgumentCaptor<ContentValues> contentValuesArgumentCaptor = ArgumentCaptor.forClass(ContentValues.class);
        verify(mDatabase).insertOrThrow(eq(TABLE), isNull(String.class), contentValuesArgumentCaptor.capture());

        checkContentValues(contentValuesArgumentCaptor);
    }

    @Test(expected = Transaction.NullColumnValueException.class)
    public void insertWithANullColumnValue_throwsNullColumnValueException() {
        mTransaction.insert(TABLE, Arrays.asList(new String[]{"null"}), new Object[]{null});
    }

    @Test
    public void updateWithValidValues_callsThroughToDbWithWhereAndCorrectValues() {
        mTransaction.update(TABLE, mWhere, Arrays.asList(COLUMN_NAMES), VALUES);

        ArgumentCaptor<ContentValues> contentValuesArgumentCaptor = ArgumentCaptor.forClass(ContentValues.class);
        verify(mDatabase).update(eq(TABLE), contentValuesArgumentCaptor.capture(), eq(WHERE), isNull(String[].class));

        checkContentValues(contentValuesArgumentCaptor);
    }

    @Test(expected = Transaction.NullColumnValueException.class)
    public void updateWithANullColumnValue_throwsNullColumnValueException() {
        mTransaction.update(TABLE, mWhere, Arrays.asList(new String[]{"null"}), new Object[]{null});
    }

    @Test
    public void remove_callsThroughWithWhere() {
        mTransaction.remove(TABLE, mWhere);

        verify(mDatabase).delete(TABLE, WHERE, null);
        verify(mWhere).getWhere();
    }

    @Test
    public void commitWhenNoObserverNotInTrans_doesNothing() {
        Mockito.reset(mDatabase); // Ignore interactions from the transaction's c'tor.

        mTransaction.commit();

        verify(mDatabase, only()).inTransaction();
        verifyZeroInteractions(mCompletionHandler0);
    }

    @Test
    public void commitWhenInTransNoObserver_setSuccessAndEnds() {
        doReturn(true).when(mDatabase).inTransaction();

        mTransaction.commit();

        verify(mDatabase).setTransactionSuccessful();
        verify(mDatabase).endTransaction();
    }

    @Test
    public void commitWhenInTrans2Observers_setSuccessAndEndAndNotifyObservers() {
        doReturn(true).when(mDatabase).inTransaction();

        mTransaction.addCompletionHandler(mCompletionHandler0);
        mTransaction.addCompletionHandler(mCompletionHandler1);

        mTransaction.commit();

        verify(mDatabase).setTransactionSuccessful();
        verify(mDatabase).endTransaction();
        verify(mCompletionHandler0).onCompleted();
        verify(mCompletionHandler1).onCompleted();
    }

    @Test
    public void rollBackWhenNotInTrans_doesNothing() {
        Mockito.reset(mDatabase); // Ignore interactions from the transaction's c'tor.

        mTransaction.rollBack();

        verify(mDatabase, only()).inTransaction();
        verifyZeroInteractions(mCompletionHandler0);
        verifyZeroInteractions(mCompletionHandler1);
    }

    @Test
    public void rollBackWhenInTrans_endsTransaction() {
        doReturn(true).when(mDatabase).inTransaction();

        mTransaction.rollBack();

        verify(mDatabase).endTransaction();
        verify(mDatabase, never()).setTransactionSuccessful();
    }

    private void checkContentValues(@Nonnull ArgumentCaptor<ContentValues> contentValuesArgumentCaptor) {
        ContentValues contentValues = contentValuesArgumentCaptor.getValue();
        assertThat(contentValues, is(not((ContentValues) null)));

        int index = 0;
        for (String columnName : COLUMN_NAMES) {
            assertThat(contentValues.get(columnName), is(EXPECTED[index]));
            index++;
        }
    }
}
