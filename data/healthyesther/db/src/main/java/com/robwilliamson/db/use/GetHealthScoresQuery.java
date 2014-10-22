package com.robwilliamson.db.use;

import android.database.Cursor;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.definition.Table;

public abstract class GetHealthScoresQuery extends GetAllValuesQuery {

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
