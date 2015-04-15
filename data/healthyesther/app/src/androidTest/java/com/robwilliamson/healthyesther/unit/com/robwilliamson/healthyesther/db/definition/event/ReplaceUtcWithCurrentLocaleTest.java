package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.db.definition.event;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.definition.Table;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class ReplaceUtcWithCurrentLocaleTest extends InstrumentationTestCase {

    private class EventDates {
        public String when;
        public String created;
        public String modified;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof EventDates)) {
                return false;
            }

            EventDates other = (EventDates) o;

            return
                    equals(when, other.when) &&
                    equals(created, other.created) &&
                    equals(modified, other.modified);
        }

        @Override
        public int hashCode() {
            return hash(when) + hash(created) + hash(modified);
        }

        @Override
        public String toString() {
            String result = "{" +
                    stringRepresentationOf("when", when) + ", " +
                    stringRepresentationOf("created", created) + ", " +
                    stringRepresentationOf("modified", modified) +
                    "}";
            return result;
        }

        private String stringRepresentationOf(String name, String value) {
            if (value == null) {
                return "null";
            }

            return name + ": " + value;
        }

        public void assertIsNullOrUtc() {
            assertIsNullOrUtc(when);
            assertIsNullOrUtc(created);
            assertIsNullOrUtc(modified);
        }

        public void assertIsNullOrLocal() {
            assertIsNullOrLocal(when, TimeZone.getDefault());
            assertIsNullOrLocal(created, DateTimeZone.UTC.toTimeZone());
            assertIsNullOrLocal(modified, DateTimeZone.UTC.toTimeZone());
        }

        private int hash(String str) {
            if (str == null) {
                return 13;
            }

            return str.hashCode();
        }

        private void assertIsNullOrLocal(String dateString, TimeZone expectedTimeZone) {
            if (dateString == null) {
                return;
            }

            DateTime dateTime = Utils.Time.fromLocalString(dateString);
            assertTrue(
                    "Expect " + dateString + " to use timezone " + expectedTimeZone + ".",
                    dateTime.getZone().toTimeZone().getRawOffset() == expectedTimeZone.getRawOffset());
        }

        private void assertIsNullOrUtc(String dateString) {
            if (dateString == null) {
                return;
            }

            DateTime dateTime = Utils.Time.fromUtcString(dateString);
            assertTrue("Expect " + dateString + " to be UTC.", dateTime.getZone().equals(DateTimeZone.UTC));
        }

        private boolean equals(String left, String right) {
            if (left == null) {
                if (right == null) {
                    return true;
                }

                return false;
            }

            return left.equals(right);
        }
    }

    private SQLiteDatabase mDb;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mDb = HealthDbHelper.getInstance(
                getInstrumentation().getTargetContext()).getWritableDatabase();
        Utils.Db.TestData.cleanOldData(mDb);
        Utils.Db.TestData.insertV3FakeData(mDb);
    }

    public void testNormalUpgradePath() throws Exception {
        Event event = Contract.getInstance().EVENT;
        Method replaceUtcWithCurrentLocale = Event.class.getDeclaredMethod("replaceUtcWithCurrentLocale", SQLiteDatabase.class);
        replaceUtcWithCurrentLocale.setAccessible(true);

        Cursor c = mDb.query(Event.TABLE_NAME, null, null, null, null, null, null);
        final Set<Integer> oldEventHashes = new HashSet<>(c.getCount());
        final Set<EventDates> oldEvents = getEvents(c);
        for (EventDates dates : oldEvents) {
            dates.assertIsNullOrUtc();
            oldEventHashes.add(dates.hashCode());
        }

        replaceUtcWithCurrentLocale.invoke(event, mDb);

        c = mDb.query(Event.TABLE_NAME, null, null, null, null, null, null);
        final Set<EventDates> newEvents = getEvents(c);
        for (EventDates dates : newEvents) {
            // Assert the dates are null or local.
            dates.assertIsNullOrLocal();

            // Assert the dates are the same.
            EventDates utcVersion = new EventDates();
            utcVersion.when = toUtcString(dates.when);
            utcVersion.created = toUtcString(dates.created);
            utcVersion.modified = toUtcString(dates.modified);

            assertTrue("Expect the old event dates set to contain an equivalent to each new date, like the one at " +
                            dates + ". This has UTC equivalent " + utcVersion,
                    oldEventHashes.contains(utcVersion.hashCode()));
        }
    }

    private String toUtcString(String timeString) {
        if (timeString == null) {
            return null;
        }

        return Utils.Time.toUtcString(Utils.Time.fromLocalString(timeString).withZone(DateTimeZone.UTC));
    }

    private Set<EventDates> getEvents(Cursor c) {
        Set<EventDates> set = new HashSet<>(c.getCount());
        if (c.moveToFirst()) {
            final int whenIndex = c.getColumnIndex(Table.cleanName(Event.WHEN));
            final int createdIndex = c.getColumnIndex(Event.CREATED);
            final int modifiedIndex = c.getColumnIndex(Event.MODIFIED);

            do {
                EventDates dates = new EventDates();
                dates.when = c.getString(whenIndex);
                dates.created = c.getString(createdIndex);
                dates.modified = c.getString(modifiedIndex);
                set.add(dates);
            } while (c.moveToNext());
        }
        return set;
    }
}
