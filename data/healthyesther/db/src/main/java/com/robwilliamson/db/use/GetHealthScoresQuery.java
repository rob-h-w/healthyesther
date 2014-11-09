package com.robwilliamson.db.use;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.definition.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class GetHealthScoresQuery extends GetAllValuesQuery {
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
                bundle.putLong(HealthScore._ID, _id);
            }

            bundle.putString(HealthScore.NAME, name);
            bundle.putInt(HealthScore.BEST_VALUE, bestValue);
            bundle.putBoolean(HealthScore.RANDOM_QUERY, randomQuery);
            bundle.putString(HealthScore.MIN_LABEL, minLabel);
            bundle.putString(HealthScore.MAX_LABEL, maxLabel);
            return bundle;
        }

        private void fromBundle(Bundle bundle) {
            long id = bundle.getLong(HealthScore._ID);
            _id = id == 0L ? null : id;
            name = bundle.getString(HealthScore.NAME);
            bestValue = bundle.getInt(HealthScore.BEST_VALUE);
            randomQuery = bundle.getBoolean(HealthScore.RANDOM_QUERY, false);
            minLabel = bundle.getString(HealthScore.MIN_LABEL, null);
            maxLabel = bundle.getString(HealthScore.MAX_LABEL, null);
        }

        public ContentValues getContentValues() {
            ContentValues values = new ContentValues();
            values.put(HealthScore.NAME, name);
            values.put(HealthScore.BEST_VALUE, bestValue);
            values.put(HealthScore.RANDOM_QUERY, randomQuery);

            if (minLabel != null) {
                values.put(HealthScore.MIN_LABEL, minLabel);
            }

            if (maxLabel != null) {
                values.put(HealthScore.MAX_LABEL, maxLabel);
            }
            return values;
        }
    }

    public static List<Score> scoresFrom(Cursor cursor) {
        ArrayList<Score> scores = new ArrayList<Score>(cursor.getCount());

        if (cursor.moveToFirst()) {
            final int rowIdIndex = cursor.getColumnIndex(HealthScore._ID);
            final int nameIndex = cursor.getColumnIndex(HealthScore.NAME);
            final int bestValueIndex = cursor.getColumnIndex(HealthScore.BEST_VALUE);
            final int randomQueryIndex = cursor.getColumnIndex(HealthScore.RANDOM_QUERY);
            final int minLabelIndex = cursor.getColumnIndex(HealthScore.MIN_LABEL);
            final int maxLabelIndex = cursor.getColumnIndex(HealthScore.MAX_LABEL);

            do {
                Score score = new Score();
                score._id = cursor.getLong(rowIdIndex);
                score.name = cursor.getString(nameIndex);
                score.bestValue = cursor.getInt(bestValueIndex);
                score.randomQuery = cursor.getInt(randomQueryIndex) > 0;
                score.minLabel = cursor.getString(minLabelIndex);
                score.maxLabel = cursor.getString(maxLabelIndex);
                scores.add(score);
            }
            while(cursor.moveToNext());
        }

        return scores;
    }

    @Override
    public String getTableName() {
        return HealthScore.TABLE_NAME;
    }

    @Override
    public String getOrderColumn() {
        return null;
    }

    @Override
    public String getOrder() {
        return null;
    }

    @Override
    public String[] getResultColumns() {
        return Table.cleanName(new String[] {
                HealthScore._ID,
                HealthScore.BEST_VALUE,
                HealthScore.MAX_LABEL,
                HealthScore.MIN_LABEL,
                HealthScore.NAME,
                HealthScore.RANDOM_QUERY
        });
    }
}
