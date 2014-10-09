package com.robwilliamson.db.use;

import com.robwilliamson.db.definition.Medication;
import com.robwilliamson.db.definition.Table;

public abstract class GetAllMedicationsQuery extends GetAllValuesQuery {
    @Override
    public String getTableName() {
        return Medication.TABLE_NAME;
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
                Medication._ID,
                Medication.NAME
        });
    }
}
