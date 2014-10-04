package com.robwilliamson.db.use;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.EventType;
import com.robwilliamson.db.definition.Table;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * List events and their types.
 */
public class SelectEventAndType implements Query {
    private final Calendar mEarliest;

    public SelectEventAndType() {
        mEarliest = null;
    }

    /**
     * Show events that occurred on or later than the earliest date provided.
     * @param earliest
     */
    public SelectEventAndType(final Calendar earliest) {
        mEarliest = earliest;
        mEarliest.setTimeZone(TimeZone.getTimeZone("utc"));
    }

    @Override
    public Cursor query(SQLiteDatabase db) {
        Cursor cursor = null;
        Contract c = Contract.getInstance(db);
        try {
            db.beginTransaction();

            String eventId = c.EVENT.getFullyQualifiedColumnName(Event.Columns._ID);
            String when = c.EVENT.getFullyQualifiedColumnName(Event.Columns.WHEN);
            String eventName = c.EVENT.getFullyQualifiedColumnName(Event.Columns.NAME);
            String eventTypeName = c.EVENT_TYPE.getFullyQualifiedColumnName(EventType.Columns.Name);
            String event = c.EVENT.getName();
            String eventType = c.EVENT_TYPE.getName();
            String eventDotTypeId = c.EVENT.getFullyQualifiedColumnName(Event.Columns.TYPE_ID);
            String eventTypeDotId = c.EVENT_TYPE.getFullyQualifiedColumnName(EventType.Columns._Id);

            String where = mEarliest == null ? "" :
                    " where " + when + " >= datetime(" + Table.Time.toString(mEarliest) + ")\n";

            String selectQuery = "SELECT ?, ?, ?, ?\n" + // eventId, when, eventName, eventTypeName
                    "FROM ?\n" + // event
                    " inner join ?\n" + // eventType
                    " on ? = ?\n" + // eventDotTypeId, eventTypeDotId
                    where +
                    "  order by ? desc\n"; // when

            cursor = db.rawQuery(selectQuery,
                    new String[]{
                            eventId,
                            when,
                            eventName,
                            eventTypeName,
                            event,
                            eventType,
                            eventDotTypeId,
                            eventTypeDotId,
                            when
                    });
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }
}
