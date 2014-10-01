package com.robwilliamson.db;

import com.robwilliamson.db.column.ColumnDefinition;
import com.robwilliamson.db.column.constraint.*;
import com.robwilliamson.db.column.constraint.References.*;

import static com.robwilliamson.db.column.constraint.Constraints.notNull;
import static com.robwilliamson.db.column.constraint.Constraints.primaryKey;
import static com.robwilliamson.db.column.constraint.Constraints.references;
import static com.robwilliamson.db.column.constraint.Constraints.unique;
import static com.robwilliamson.db.column.constraint.References.onDelete;

/**
 * Contains database table and column names, and other database-definition data.
 */
public final class Contract {
    public static final class Event {
        public static final class Table {
            public static final String NAME = "event";
        }

        public static final class Column {
            public static final ColumnDefinition ID       = new ColumnDefinition("id",       Type.INTEGER,  null,                primaryKey().autoIncrement());
            public static final ColumnDefinition WHEN     = new ColumnDefinition("when",     Type.DATETIME, null,                notNull());
            public static final ColumnDefinition CREATED  = new ColumnDefinition("created",  Type.DATETIME, "CURRENT_TIMESTAMP", notNull());
            public static final ColumnDefinition MODIFIED = new ColumnDefinition("modified", Type.DATETIME, null);
            public static final ColumnDefinition TYPE_ID  = new ColumnDefinition("type_id",  null,          null,                notNull(),
                                                                                                                                 references(EventTypeTable.Table.NAME,
                                                                                                                                            EventTypeTable.Column.ID,
                                                                                                                                            onDelete(Action.CASCADE)));
        }
    }

    public static final class  EventTypeTable {
        public static final class Table {
            public static final String NAME = "eventType_table";
        }

        public static final class Column {
            public static final ColumnDefinition ID         = new ColumnDefinition("id",         Type.INTEGER, null, primaryKey().autoIncrement());
            public static final ColumnDefinition NAME       = new ColumnDefinition("name",       Type.TEXT,    null, notNull(), unique());
            public static final ColumnDefinition TABLE_NAME = new ColumnDefinition("table_name", Type.TEXT,    null, notNull(), unique());
        }
    }
}
