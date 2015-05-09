package com.robwilliamson.healthyesther.db.definition;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.data.EventData;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

/**
 * Table detailing event ids, times and types.
 *
 * Note that internal times are in UTC, while user-displayed or analytical times use the local TZ.
 */
public class Event extends Table {
    public enum Type {
        MEAL(1),
        MEDICATION(2),
        HEALTH_SCORE(3),
        NOTE(4),;

        private final long m_id;

        Type(long _id) {
            m_id = _id;
        }

        public long id() {
            return m_id;
        }

        @NonNull
        public static Type valueOf(long _id) {
            return Type.values()[(int) _id - 1];
        }
    }

    public static class Modification extends com.robwilliamson.healthyesther.db.definition.Modification {
        private final EventData mValue;

        public Modification(String name, DateTime when) {
            assertValidName(name);

            mValue = new EventData(when, Utils.Time.localNow(), null, 0, name);
        }

        public void setTypeId(long id) {
            mValue.typeId = id;
        }

        @Override
        public void modify(SQLiteDatabase db) {
            if (mValue.typeId == 0) {
                throw new IllegalArgumentException("An event must have a type.");
            }

            Contract c = Contract.getInstance();
            if (isRowIdSet()) {
                // Update an existing event
                mValue._id = getRowId();
                c.EVENT.update(db, mValue);
            } else {
                // Create a new event
                setRowId(c.EVENT.insert(db, mValue));
                mValue._id = getRowId();
            }
        }
    }

    public final static String TABLE_NAME = "event";
    public final static String _ID = "_id";
    public final static String WHEN = "[when]";         // This is in the local time zone.
    public final static String CREATED = "created";     // This is in UTC.
    public final static String MODIFIED = "modified";   // This is in UTC or absent.
    public final static String TYPE_ID = "type_id";
    public final static String NAME = "name";

    public static boolean validateName(String name) {
        return name == null || Utils.Strings.validateLength(name, 0, 140);
    }

    private static void assertValidName(String name) {
        if (!validateName(name)) {
            throw new IllegalArgumentException("Name must be present and less than 141 characters.");
        }
    }

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE event ( \n" +
                "    _id      INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    [when]   DATETIME     NOT NULL\n" +
                "                          DEFAULT ( CURRENT_TIMESTAMP ),\n" +
                "    created  DATETIME     NOT NULL\n" +
                "                          DEFAULT ( CURRENT_TIMESTAMP ),\n" +
                "    modified DATETIME,\n" +
                "    type_id               REFERENCES event_type ( _id ) ON DELETE CASCADE\n" +
                "                                                        ON UPDATE CASCADE\n" +
                "                          NOT NULL,\n" +
                "    name     TEXT( 140 )  DEFAULT ( '' ) \n" +
                ");");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {
        if (from < 4 && to == 4) {
            replaceUtcWithCurrentLocale(db);
        }
    }

    public long insert(SQLiteDatabase db, EventData eventData) {
        return insert(db, eventData.asContentValues());
    }

    public int update(SQLiteDatabase db, EventData eventData) {
        if (eventData._id == null) {
            throw new IllegalArgumentException(
                    "An event must specify an ID before it's database entry can be updated.");
        }

        return update(db, eventData.asContentValues(), eventData._id);
    }

    private void replaceUtcWithCurrentLocale(SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            final int idIndex = c.getColumnIndex(_ID);
            final int whenIndex = c.getColumnIndex(Table.cleanName(WHEN));
            final int createdIndex = c.getColumnIndex(CREATED);
            final int modifiedIndex = c.getColumnIndex(MODIFIED);

            do {
                long id = c.getLong(idIndex);
                DateTime when = utcToLocalTime(c.getString(whenIndex));
                DateTime created = utcToUtcTime(c.getString(createdIndex));
                DateTime modified = utcToUtcTime(c.getString(modifiedIndex));

                ContentValues values = new ContentValues();

                values.put(WHEN, Utils.Time.toLocalString(when));
                values.put(CREATED, Utils.Time.toLocalString(created));
                values.put(MODIFIED, Utils.Time.toLocalString(modified));

                update(db, values, id);
            } while (c.moveToNext());
        }
    }

    private static DateTime utcToLocalTime(String utcTime) {
        return utcToZoneTime(utcTime, DateTimeZone.forTimeZone(TimeZone.getDefault()));
    }

    private static DateTime utcToUtcTime(String utcTime) {
        return utcToZoneTime(utcTime, DateTimeZone.UTC);
    }

    private static DateTime utcToZoneTime(String utcTime, DateTimeZone zone) {
        if (utcTime == null) {
            return null;
        }

        try {
            return Utils.Time.fromUtcString(utcTime).withZone(zone);
        } catch (IllegalArgumentException ignored) {
            return Utils.Time.fromDatabaseDefaultString(utcTime).withZone(zone);
        }
    }
}
