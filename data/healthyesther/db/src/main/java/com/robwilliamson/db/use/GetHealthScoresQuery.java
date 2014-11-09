package com.robwilliamson.db.use;

import android.database.Cursor;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.definition.Table;

import java.util.ArrayList;
import java.util.List;

public abstract class GetHealthScoresQuery extends GetAllValuesQuery {
    public static class Score {
        public Long _id;
        public String name;
        public int bestValue;
        public boolean randomQuery;
        public String minLabel;
        public String maxLabel;
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
