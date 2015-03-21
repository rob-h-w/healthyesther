package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.definition.Meal;
import com.robwilliamson.healthyesther.db.definition.Table;

public abstract class GetAllMealsQuery extends GetAllValuesQuery {
    @Override
    public String getTableName() {
        return Meal.TABLE_NAME;
    }

    @Override
    public String getOrderColumn() {
        return Meal.NAME;
    }

    @Override
    public String getOrder() {
        return "NOCASE ASC";
    }

    @Override
    public String[] getResultColumns() {
        return Table.cleanName(new String [] {
                Meal._ID,
                Meal.NAME
        });
    }
}
