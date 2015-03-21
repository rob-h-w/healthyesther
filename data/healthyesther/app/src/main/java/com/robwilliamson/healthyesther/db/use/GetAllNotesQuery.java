package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.definition.Note;
import com.robwilliamson.healthyesther.db.definition.Table;

public abstract class GetAllNotesQuery extends GetAllValuesQuery {
    @Override
    public String getTableName() {
        return Note.TABLE_NAME;
    }

    @Override
    public String getOrderColumn() {
        return Note.NAME;
    }

    @Override
    public String getOrder() {
        return "NOCASE ASC";
    }

    @Override
    public String[] getResultColumns() {
        return Table.cleanName(new String[] {
                Note._ID,
                Note.NAME,
                Note.NOTE
        });
    }
}
