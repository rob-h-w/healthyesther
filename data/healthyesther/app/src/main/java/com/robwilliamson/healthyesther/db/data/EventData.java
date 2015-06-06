package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Table;

import org.joda.time.DateTime;

public class EventData extends DataAbstraction {
    private IdData mId;
    private DateTime mWhen;
    private DateTime mCreated;
    private DateTime mModified;
    private long mTypeId;
    private String mName;

    public EventData() {
        mId = new IdData(this, com.robwilliamson.healthyesther.db.definition.Event._ID);
    }

    public EventData(
            DateTime when,
            DateTime created,
            DateTime modified,
            long typeId,
            String name) {
        this(
                null,
                when,
                created,
                modified,
                typeId,
                name);
    }

    public EventData(
            Long _id,
            DateTime when,
            DateTime created,
            DateTime modified,
            long typeId,
            String name) {
        mId = new IdData(this, com.robwilliamson.healthyesther.db.definition.Event._ID);
        mId.set_id(_id);
        this.mWhen = when;
        this.mCreated = created;
        this.mModified = modified;
        this.mTypeId = typeId;
        this.mName = name;
    }

    @Override
    protected void asBundle(Bundle bundle) {
        mId.asBundle(bundle);

        bundle.putString(com.robwilliamson.healthyesther.db.definition.Event.WHEN, Utils.Time.toDatabaseString(mWhen));

        if (mCreated != null) {
            bundle.putString(com.robwilliamson.healthyesther.db.definition.Event.CREATED, Utils.Time.toDatabaseString(mCreated));
        }

        if (mModified != null) {
            bundle.putString(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED, Utils.Time.toDatabaseString(mModified));
        }

        bundle.putLong(com.robwilliamson.healthyesther.db.definition.Event.TYPE_ID, mTypeId);
        bundle.putString(com.robwilliamson.healthyesther.db.definition.Event.NAME, mName);
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.putAll(mId.asContentValues());
        values.put(com.robwilliamson.healthyesther.db.definition.Event.WHEN, Utils.Time.toDatabaseString(mWhen));

        if (mCreated == null) {
            values.put(com.robwilliamson.healthyesther.db.definition.Event.CREATED, Utils.Time.toDatabaseString(Utils.Time.localNow()));
        } else {
            values.put(com.robwilliamson.healthyesther.db.definition.Event.CREATED, Utils.Time.toDatabaseString(mCreated));
        }

        if (mModified != null) {
            values.put(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED, Utils.Time.toDatabaseString(mModified));
        } else if (mCreated != null) {
            values.put(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED, Utils.Time.toDatabaseString(Utils.Time.localNow()));
        }

        values.put(com.robwilliamson.healthyesther.db.definition.Event.TYPE_ID, mTypeId);
        values.put(com.robwilliamson.healthyesther.db.definition.Event.NAME, mName);

        return values;
    }

    @Override
    protected void populateFrom(Cursor cursor) {
        final int rowIdIndex = cursor.getColumnIndex(com.robwilliamson.healthyesther.db.definition.Event._ID);
        final int whenIndex = cursor.getColumnIndex(Table.cleanName(com.robwilliamson.healthyesther.db.definition.Event.WHEN));
        final int createdIndex = cursor.getColumnIndex(com.robwilliamson.healthyesther.db.definition.Event.CREATED);
        final int modifiedIndex = cursor.getColumnIndex(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED);
        final int typeIdIndex = cursor.getColumnIndex(com.robwilliamson.healthyesther.db.definition.Event.TYPE_ID);
        final int nameIndex = cursor.getColumnIndex(com.robwilliamson.healthyesther.db.definition.Event.NAME);

        mId.set_id(cursor.getLong(rowIdIndex));
        this.mWhen = Utils.Time.fromDatabaseString(cursor.getString(whenIndex));
        this.mCreated = Utils.Time.fromDatabaseString(cursor.getString(createdIndex));
        this.mModified = Utils.Time.fromDatabaseString(cursor.getString(modifiedIndex));
        this.mTypeId = cursor.getLong(typeIdIndex);
        this.mName = cursor.getString(nameIndex);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        mId.populateFrom(bundle);

        mWhen = Utils.Time.fromDatabaseString(bundle.getString(com.robwilliamson.healthyesther.db.definition.Event.WHEN));

        if (bundle.containsKey(com.robwilliamson.healthyesther.db.definition.Event.CREATED)) {
            mCreated = Utils.Time.fromDatabaseString(bundle.getString(com.robwilliamson.healthyesther.db.definition.Event.CREATED));
        } else {
            mCreated = null;
        }

        if (bundle.containsKey(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED)) {
            mModified = Utils.Time.fromDatabaseString(bundle.getString(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED));
        } else {
            mModified = null;
        }

        mTypeId = bundle.getLong(com.robwilliamson.healthyesther.db.definition.Event.TYPE_ID);
        mName = bundle.getString(com.robwilliamson.healthyesther.db.definition.Event.NAME);
    }

    public Long get_id() {
        return mId.get_id();
    }

    public void set_id(Long _id) {
        mId.set_id(_id);
    }

    public DateTime getCreated() {
        return mCreated;
    }

    public void setCreated(DateTime created) {
        this.mCreated = created;
        setModified(true);
    }

    public DateTime getModified() {
        return mModified;
    }

    public void setModified(DateTime modified) {
        this.mModified = modified;
        setModified(true);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
        setModified(true);
    }

    public long getTypeId() {
        return mTypeId;
    }

    public void setTypeId(long typeId) {
        this.mTypeId = typeId;
        setModified(true);
    }

    public DateTime getWhen() {
        return mWhen;
    }

    public void setWhen(DateTime when) {
        this.mWhen = when;
        setModified(true);
    }
}
