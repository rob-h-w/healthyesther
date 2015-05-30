package com.robwilliamson.healthyesther.db.definition;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;

public class HealthScore extends Table {

    public static class Modification extends com.robwilliamson.healthyesther.db.definition.Modification {

        private Value mScore;
        private boolean mEnabled = false;

        public Modification(long rowId) {
            setRowId(rowId);
        }

        public Modification(Value score) {
            mScore = score;

            if (score._id != null) {
                setRowId(score._id);
            }
        }

        public Modification(Value score, boolean enabled) {
            this(score);
            enable(enabled);
        }

        public Modification(String name,
                            int bestValue,
                            boolean randomQuery,
                            String minLabel,
                            String maxLabel) {
            mScore = new Value(name,
                    bestValue,
                    randomQuery,
                    minLabel,
                    maxLabel);
        }

        public void enable(boolean enabled) {
            mEnabled = enabled;
        }

        @Override
        public void modify(SQLiteDatabase db) {
            if (getRowId() == null) {
                setRowId(Contract.getInstance().HEALTH_SCORE.insert(
                        db, mScore));
                mScore._id = getRowId();
            } else {
                if (!mEnabled) {
                    return;
                }

                mScore._id = getRowId();
                Contract.getInstance().HEALTH_SCORE.update(db, mScore);
            }
        }
    }

    public static class Value extends DataAbstraction {
        public Long _id;
        public String name;
        public int bestValue;
        public boolean randomQuery;
        public String minLabel;
        public String maxLabel;

        public Value() {}

        public Value(String name,
                     int bestValue,
                     boolean randomQuery,
                     String minLabel,
                     String maxLabel) {
            this(null,
                    name,
                    bestValue,
                    randomQuery,
                    minLabel,
                    maxLabel);
        }

        public Value(
                long id,
                String name,
                int bestValue,
                boolean randomQuery,
                String minLabel,
                String maxLabel) {
            this(id == 0L ? null : id,
                    name,
                    bestValue,
                    randomQuery,
                    minLabel,
                    maxLabel);
        }

        public Value(
                Long id,
                String name,
                int bestValue,
                boolean randomQuery,
                String minLabel,
                String maxLabel) {
            this._id = id;
            this.name = name;
            this.bestValue = bestValue;
            this.randomQuery = randomQuery;
            this.minLabel = minLabel;
            this.maxLabel = maxLabel;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (o == null) {
                return false;
            }

            if (!(o instanceof Value)) {
                return false;
            }

            Value other = (Value)o;

            if (
                    _id != other._id &&
                            (_id == null ||
                            !_id.equals(other._id))) {
                return false;
            }

            if (!Utils.Strings.equals(name, other.name)) {
                return false;
            }

            if (bestValue != other.bestValue) {
                return false;
            }

            if (randomQuery != other.randomQuery) {
                return false;
            }

            if (!Utils.Strings.equals(minLabel, other.minLabel)) {
                return false;
            }

            if (!Utils.Strings.equals(maxLabel, other.maxLabel)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int h = super.hashCode();
            h += _id == null ? 31 : _id;
            h += name == null ? 31 : name.hashCode();
            h += bestValue * 31;
            h *= randomQuery ? 2 : 4;
            h += minLabel == null ? 31 : minLabel.hashCode();
            h += maxLabel == null ? 31 : maxLabel.hashCode();
            return h;
        }

        public void bundle(Bundle bundle, String bundleKey) {
            bundle.putBundle(bundleKey, asBundle());
        }

        @Override
        protected void asBundle(Bundle bundle) {
            if (_id != null) {
                bundle.putLong(_ID, _id);
            }

            bundle.putString(NAME, name);
            bundle.putInt(BEST_VALUE, bestValue);
            bundle.putBoolean(RANDOM_QUERY, randomQuery);
            bundle.putString(MIN_LABEL, minLabel);
            bundle.putString(MAX_LABEL, maxLabel);
        }

        @Override
        public ContentValues asContentValues() {
            ContentValues values = new ContentValues();
            values.put(NAME, name);
            values.put(BEST_VALUE, bestValue);
            values.put(RANDOM_QUERY, randomQuery);

            if (minLabel != null) {
                values.put(MIN_LABEL, minLabel);
            }

            if (maxLabel != null) {
                values.put(MAX_LABEL, maxLabel);
            }
            return values;
        }

        @Override
        protected void populateFrom(Cursor cursor) {
            final int rowIdIndex = cursor.getColumnIndex(_ID);
            final int nameIndex = cursor.getColumnIndex(NAME);
            final int bestValueIndex = cursor.getColumnIndex(BEST_VALUE);
            final int randomQueryIndex = cursor.getColumnIndex(RANDOM_QUERY);
            final int minLabelIndex = cursor.getColumnIndex(MIN_LABEL);
            final int maxLabelIndex = cursor.getColumnIndex(MAX_LABEL);

            this._id = cursor.getLong(rowIdIndex);
            this.name = cursor.getString(nameIndex);
            this.bestValue = cursor.getInt(bestValueIndex);
            this.randomQuery = cursor.getInt(randomQueryIndex) > 0;
            this.minLabel = cursor.getString(minLabelIndex);
            this.maxLabel = cursor.getString(maxLabelIndex);
        }

        @Override
        protected void populateFrom(Bundle bundle) {
            long id = bundle.getLong(_ID);
            _id = id == 0L ? null : id;
            name = bundle.getString(NAME);
            bestValue = bundle.getInt(BEST_VALUE);
            randomQuery = bundle.getBoolean(RANDOM_QUERY, false);
            minLabel = bundle.getString(MIN_LABEL, null);
            maxLabel = bundle.getString(MAX_LABEL, null);
        }
    }

    public static final int MAX = 5;
    public static final int MID = 3;
    public static final int MIN = 1;

    public static final String TABLE_NAME = "health_score";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String BEST_VALUE = "best_value";
    public static final String RANDOM_QUERY = "random_query";
    public static final String MIN_LABEL = "min_label";
    public static final String MAX_LABEL = "max_label";

    @Override
    public String getName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE health_score ( \n" +
                "    _id          INTEGER      PRIMARY KEY AUTOINCREMENT,\n" +
                "    name         TEXT( 140 )  NOT NULL\n" +
                "                              UNIQUE,\n" +
                "    best_value   INTEGER      NOT NULL,\n" +
                "    random_query BOOLEAN      NOT NULL\n" +
                "                              DEFAULT ( 0 ),\n" +
                "    min_label    TEXT( 140 ),\n" +
                "    max_label    TEXT( 140 ) \n" +
                ");");

        insert(db, "Happiness", MAX, true, "Sad", "Happy");
        insert(db, "Energy", MAX, true, "Tired", "Energetic");
        insert(db, "Drowsiness", MAX, true, "Sleepy", "Awake");
    }

    @Override
    public void upgrade(SQLiteDatabase db, int from, int to) {
        if (from == 1) {
            create(db);
        }
    }

    public long insert(
            SQLiteDatabase db,
            String name,
            int bestValue,
            boolean randomQuery,
            String minLabel,
            String maxLabel) {
        return insert(db, new Value(name, bestValue, randomQuery, minLabel, maxLabel));
    }

    public long insert(
            SQLiteDatabase db,
            Value score) {
        return insert(db, score.asContentValues());
    }

    public void update(SQLiteDatabase db,
                       Value score) {

        update(db, score.asContentValues(), score._id);
    }
}
