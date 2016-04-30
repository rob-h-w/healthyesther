package com.robwilliamson.db.definition;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;

public class HealthScore extends Table {

    public static class Modification extends com.robwilliamson.db.definition.Modification {

        private Score mScore;

        public Modification(long rowId) {
            setRowId(rowId);
        }

        public Modification(Score score) {
            mScore = score;

            if (score._id != null) {
                setRowId(score._id);
            }
        }

        public Modification(String name,
                            int bestValue,
                            boolean randomQuery,
                            String minLabel,
                            String maxLabel) {
            mScore = new Score(name,
                    bestValue,
                    randomQuery,
                    minLabel,
                    maxLabel);
        }

        @Override
        public void modify(SQLiteDatabase db) {
            if (getRowId() == null) {
                setRowId(Contract.getInstance().HEALTH_SCORE.insert(
                        db, mScore));
            } else {
                Contract.getInstance().HEALTH_SCORE.update(db, mScore);
            }
        }
    }

    public static class Score {
        public Long _id;
        public String name;
        public int bestValue;
        public boolean randomQuery;
        public String minLabel;
        public String maxLabel;

        private static String keysName(String key) {
            return key + "Keys";
        }

        private static String valueName(String bundleKey, String key) {
            return bundleKey + "Value_" + key;
        }

        public static Score from(Bundle bundle, String bundleKey) {
            Score score = new Score();
            score.fromBundle(bundle.getBundle(bundleKey));
            return score;
        }

        public Score() {}

        public Score(String name,
                     int bestValue,
                     boolean randomQuery,
                     String minLabel,
                     String maxLabel) {
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

            if (!(o instanceof Score)) {
                return false;
            }

            Score other = (Score)o;

            if (_id != other._id) {
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

        public void bundle(Bundle bundle, String bundleKey) {
            bundle.putBundle(bundleKey, asBundle());
        }

        private Bundle asBundle() {
            Bundle bundle = new Bundle();
            if (_id != null) {
                bundle.putLong(_ID, _id);
            }

            bundle.putString(NAME, name);
            bundle.putInt(BEST_VALUE, bestValue);
            bundle.putBoolean(RANDOM_QUERY, randomQuery);
            bundle.putString(MIN_LABEL, minLabel);
            bundle.putString(MAX_LABEL, maxLabel);
            return bundle;
        }

        private void fromBundle(Bundle bundle) {
            long id = bundle.getLong(_ID);
            _id = id == 0L ? null : id;
            name = bundle.getString(NAME);
            bestValue = bundle.getInt(BEST_VALUE);
            randomQuery = bundle.getBoolean(RANDOM_QUERY, false);
            minLabel = bundle.getString(MIN_LABEL, null);
            maxLabel = bundle.getString(MAX_LABEL, null);
        }

        public ContentValues getContentValues() {
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
        return insert(db, new Score(name, bestValue, randomQuery, minLabel, maxLabel));
    }

    public long insert(
            SQLiteDatabase db,
            Score score) {
        return insert(db, score.getContentValues());
    }

    public void update(SQLiteDatabase db,
                       Score score) {

        update(db, score.getContentValues(), score._id);
    }
}
