package com.robwilliamson.healthyesther.db.abstraction;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Table;

import org.joda.time.DateTime;

public class EventData extends DataAbstraction {
    public Long _id;
    public DateTime when;
    public DateTime created;
    public DateTime modified;
    public long typeId;
    public String name;

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
        this._id = (_id == null || _id <= 0L) ? null : _id;
        this.when = when;
        this.created = created;
        this.modified = modified;
        this.typeId = typeId;
        this.name = name;
    }

    @Override
    public Bundle asBundle() {
        Bundle bundle = new Bundle();
        if (_id != null) {
            bundle.putLong(com.robwilliamson.healthyesther.db.definition.Event._ID, _id);
        }

        bundle.putString(com.robwilliamson.healthyesther.db.definition.Event.WHEN, Utils.Time.toDatabaseString(when));

        if (created != null) {
            bundle.putString(com.robwilliamson.healthyesther.db.definition.Event.CREATED, Utils.Time.toDatabaseString(created));
        }

        if (modified != null) {
            bundle.putString(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED, Utils.Time.toDatabaseString(modified));
        }

        bundle.putLong(com.robwilliamson.healthyesther.db.definition.Event.TYPE_ID, typeId);
        bundle.putString(com.robwilliamson.healthyesther.db.definition.Event.NAME, name);
        return bundle;
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.put(com.robwilliamson.healthyesther.db.definition.Event._ID, _id);
        values.put(com.robwilliamson.healthyesther.db.definition.Event.WHEN, Utils.Time.toDatabaseString(when));

        if (created == null) {
            values.put(com.robwilliamson.healthyesther.db.definition.Event.CREATED, Utils.Time.toDatabaseString(Utils.Time.localNow()));
        } else {
            values.put(com.robwilliamson.healthyesther.db.definition.Event.CREATED, Utils.Time.toDatabaseString(created));
        }

        if (modified != null) {
            values.put(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED, Utils.Time.toDatabaseString(modified));
        } else if (created != null) {
            values.put(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED, Utils.Time.toDatabaseString(Utils.Time.localNow()));
        }

        values.put(com.robwilliamson.healthyesther.db.definition.Event.TYPE_ID, typeId);
        values.put(com.robwilliamson.healthyesther.db.definition.Event.NAME, name);

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

        this._id = cursor.getLong(rowIdIndex);
        this.when = Utils.Time.fromDatabaseString(cursor.getString(whenIndex));
        this.created = Utils.Time.fromDatabaseString(cursor.getString(createdIndex));
        this.modified = Utils.Time.fromDatabaseString(cursor.getString(modifiedIndex));
        this.typeId = cursor.getLong(typeIdIndex);
        this.name = cursor.getString(nameIndex);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        if (bundle.containsKey(com.robwilliamson.healthyesther.db.definition.Event._ID)) {
            _id = bundle.getLong(com.robwilliamson.healthyesther.db.definition.Event._ID);
        } else {
            _id = null;
        }

        when = Utils.Time.fromDatabaseString(bundle.getString(com.robwilliamson.healthyesther.db.definition.Event.WHEN));

        if (bundle.containsKey(com.robwilliamson.healthyesther.db.definition.Event.CREATED)) {
            created = Utils.Time.fromDatabaseString(bundle.getString(com.robwilliamson.healthyesther.db.definition.Event.CREATED));
        } else {
            created = null;
        }

        if (bundle.containsKey(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED)) {
            modified = Utils.Time.fromDatabaseString(bundle.getString(com.robwilliamson.healthyesther.db.definition.Event.MODIFIED));
        } else {
            modified = null;
        }

        typeId = bundle.getLong(com.robwilliamson.healthyesther.db.definition.Event.TYPE_ID);
        name = bundle.getString(com.robwilliamson.healthyesther.db.definition.Event.NAME);
    }
}
