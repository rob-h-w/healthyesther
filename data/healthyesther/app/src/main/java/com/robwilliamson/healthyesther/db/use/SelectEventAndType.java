package com.robwilliamson.healthyesther.db.use;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.definition.EventType;
import com.robwilliamson.healthyesther.db.definition.Table;

import org.joda.time.DateTime;

/**
 * List events and their types.
 */
public abstract class SelectEventAndType implements SelectQuery {
    private final DateTime mEarliest;
    private final DateTime mLatest;
    private final String TYPE_NAME = "type_name";

    public SelectEventAndType() {
        this(null, null);
    }

    /**
     * Show events that occurred on or later than the earliest date provided.
     *
     * @param earliest
     */
    public SelectEventAndType(final DateTime earliest) {
        this(earliest, null);
    }

    public SelectEventAndType(final DateTime earliest, final DateTime latest) {
        mEarliest = earliest;
        mLatest = latest;
    }

    @Override
    public Cursor query(SQLiteDatabase db) {
        Cursor cursor = null;
        Contract c = Contract.getInstance();
        try {
            db.beginTransaction();

            String[] qualifiedUniqueColumns = new String[]{
                    c.EVENT.getQualifiedName(Event._ID),
                    c.EVENT.getQualifiedName(Event.WHEN),
                    c.EVENT.getQualifiedName(Event.NAME),
                    c.EVENT_TYPE.getQualifiedName(EventType.NAME) + " AS " + TYPE_NAME,
            };

            String where = "";
            String earliestTerm = "";
            String and = "";
            String latestTerm = "";
            String terminator = "";

            if (mEarliest != null) {
                where = " where ";
                earliestTerm = c.EVENT.getQualifiedName(Event.WHEN) + " >= \"" + Utils.Time.toDatabaseString(mEarliest) + "\" ";
                terminator = "\n";
            }

            if (mLatest != null) {
                where = " where ";
                latestTerm = c.EVENT.getQualifiedName(Event.WHEN) + " <= \"" + Utils.Time.toDatabaseString(mLatest) + "\" ";
                terminator = "\n";
            }

            if (!Utils.Strings.nullOrEmpty(earliestTerm) &&
                    !Utils.Strings.nullOrEmpty(latestTerm)) {
                and = " and ";
            }

            where = where + earliestTerm + and + latestTerm + terminator;

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
        return Table.cleanName(new String[]{
                Event._ID,
                Event.WHEN,
                Event.NAME,
                TYPE_NAME,
        });
    }
}
