package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.definition.MedicationName;
import com.robwilliamson.healthyesther.db.definition.Table;

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
