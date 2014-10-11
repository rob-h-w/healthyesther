package com.robwilliamson.db.use;

import com.robwilliamson.db.definition.MedicationName;
import com.robwilliamson.db.definition.Table;

public abstract class GetAllMedicationNamesQuery extends GetAllValuesQuery {
    @Override
    public String getTableName() {
        return MedicationName.TABLE_NAME;
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
                MedicationName.MEDICATION_ID,
                MedicationName.NAME
        });
    }
}
