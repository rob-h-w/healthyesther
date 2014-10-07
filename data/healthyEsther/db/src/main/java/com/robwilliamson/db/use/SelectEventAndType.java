package com.robwilliamson.db.use;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.EventType;
import com.robwilliamson.db.definition.Table;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

/**
 * List events and their types.
 */
public abstract class SelectEventAndType implements SelectQuery {
    private final DateTime mEarliest;
    private final String TYPE_NAME = "type_name";

    public SelectEventAndType() {
        mEarliest = null;
    }

    /**
     * Show events that occurred on or later than the earliest date provided.
     * @param earliest
     */
    public SelectEventAndType(final DateTime earliest) {
        mEarliest = earliest.withZone(DateTimeZone.UTC);
    }

    @Override
    public Cursor query(SQLiteDatabase db) {
        Cursor cursor = null;
        Contract c = Contract.getInstance();
        try {
            db.beginTransaction();

            String[] qualifiedUniqueColumns = new String[] {
                    c.EVENT.getQualifiedName(Event._ID),
                    c.EVENT.getQualifiedName(Event.WHEN),
                    c.EVENT.getQualifiedName(Event.NAME),
                    c.EVENT_TYPE.getQualifiedName(EventType.NAME) + " AS " + TYPE_NAME,
            };

            String where = mEarliest == null ? "" :
                    " where " + c.EVENT.getQualifiedName(Event.WHEN) + " >= datetime(" + Utils.Time.toDatabaseString(mEarliest) + ") \n";

            String selectQuery = "SELECT " + Utils.join(qualifiedUniqueColumns, ", ") + "\n" + // _Id, WHEN, NAME, EventType.NAME
                    "FROM " + Event.TABLE_NAME + "\n" +
                    " inner join " + EventType.TABLE_NAME + "\n" +
                    " on " + c.EVENT.getQualifiedName(Event.TYPE_ID) + " = " + c.EVENT_TYPE.getQualifiedName(EventType._ID) + " \n" +
                    where +
                    "  order by " + c.EVENT.getQualifiedName(Event.WHEN) + " desc \n";

            cursor = db.rawQuery(selectQuery, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    @Override
    public String[] getResultColumns() {
        return Table.cleanName(new String[] {
                Event._ID,
                Event.WHEN,
                Event.NAME,
                TYPE_NAME,
        });
    }
}
