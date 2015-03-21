package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.definition.HealthScore;
import com.robwilliamson.healthyesther.db.definition.Table;

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
