/*
   © Robert Williamson 2014-2016.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther.db.definition.event;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;
import com.robwilliamson.healthyesther.test.Database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class ReplaceUtcWithCurrentLocaleTest {
    static {
        // Ensure the time converter is loaded.
        new DateTimeConverter();
    }

    private SQLiteDatabase mDb;

    @Before
    public void setUp() throws Exception {
        Database.useV3Database();

        mDb = HealthDbHelper.getDatabaseWrapper().getSqliteDatabase();
    }

    @After
    public void tearDown() {
        Database.deleteDatabase();
    }

    @SuppressWarnings("RedundantThrows")
    @Test
    public void testNormalUpgradePath() throws Exception {
        Cursor c = mDb.query(
                DatabaseAccessor.EVENT_TABLE.getName(),
                null,
                null,
                null,
                null,
                null,
                null);
        final Set<EventDates> newEvents = getEvents(c);
        for (EventDates dates : newEvents) {
            // Assert the dates are null or local.
            try {
                dates.assertIsNullOrLocal();
            } catch (Throwable e) {
                e.printStackTrace();
                fail("threw " + e);
            }
        }
    }

    private Set<EventDates> getEvents(Cursor c) {
        Set<EventDates> set = new HashSet<>(c.getCount());
        if (c.moveToFirst()) {
            final int whenIndex = c.getColumnIndex(Utils.Db.cleanName(EventTable.WHEN));
            final int createdIndex = c.getColumnIndex(EventTable.CREATED);
            final int modifiedIndex = c.getColumnIndex(EventTable.MODIFIED);

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

    private class EventDates {
        public String when;
        String created;
        String modified;

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
            return "{" +
                    stringRepresentationOf("when", when) + ", " +
                    stringRepresentationOf("created", created) + ", " +
                    stringRepresentationOf("modified", modified) +
                    "}";
        }

        private String stringRepresentationOf(String name, String value) {
            if (value == null) {
                return "null";
            }

            return name + ": " + value;
        }

        void assertIsNullOrLocal() {
            assertIsNullOrLocal(when, null);
            assertIsNullOrLocal(created, ZoneOffset.UTC);
            assertIsNullOrLocal(modified, ZoneOffset.UTC);
        }

        private int hash(String str) {
            if (str == null) {
                return 13;
            }

            return str.hashCode();
        }

        private void assertIsNullOrLocal(
                @Nullable String dateString,
                @Nullable ZoneId expectedTimeZone) {
            if (dateString == null) {
                return;
            }

            ZonedDateTime dateTime = Utils.Time.fromLocalString(dateString);

            if (expectedTimeZone == null) {
                assertTrue(dateString.contains(" -") || dateString.contains(" +"));
            } else {
                assertTrue(
                        "Expect " + dateString + " to use timezone " + expectedTimeZone + ".",
                        dateTime.getZone().equals(expectedTimeZone));
            }
        }

        private boolean equals(String left, String right) {
            if (left == null) {
                return right == null;

            }

            return left.equals(right);
        }
    }
}
