package com.robwilliamson.db;

/**
 * Contains database table and column names, and other database-definition data.
 */
public final class Contract {
    public static final String NAME = "health.db3";
    public static final int VERSION = 1;

    public static final class Event {
        public static final class Table {
            public static final String NAME = "event";
        }

        public static final class Column {
            public static final String ID       = "id";
            public static final String WHEN     = "when";
            public static final String CREATED  = "created";
            public static final String MODIFIED = "modified";
            public static final String TYPE_ID  = "type_id";
        }

        public static final String CREATE_TABLE = "CREATE TABLE event ( \n" +
                "    id       INTEGER  PRIMARY KEY AUTOINCREMENT,\n" +
                "    [when]   DATETIME NOT NULL,\n" +
                "    created  DATETIME NOT NULL\n" +
                "                      DEFAULT ( CURRENT_TIMESTAMP ),\n" +
                "    modified DATETIME,\n" +
                "    type_id           NOT NULL\n" +
                "                      REFERENCES eventType_table ( id ) ON DELETE CASCADE\n" +
                "                                                        ON UPDATE CASCADE \n" +
                ");";
    }

    public static final class EventTypeTable {
        public static final class Table {
            public static final String NAME = "eventType_table";
        }

        public static final class Column {
            public static final String ID         = "id";
            public static final String NAME       = "name";
            public static final String TABLE_NAME = "table_name";
        }

        public static final String CREATE_TABLE = "CREATE TABLE eventType_table ( \n" +
                "    id         INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                "    name       TEXT( 30 )  NOT NULL\n" +
                "                           UNIQUE,\n" +
                "    table_name TEXT( 30 )  NOT NULL\n" +
                "                           UNIQUE \n" +
                ");\n";
    }

    public static final class MealEvent {
        public static final class Table {
            public static final String NAME = "meal_event";
        }

        public static final class Column {
            public static final String MEAL_ID  = "meal_id";
            public static final String EVENT_ID = "event_id";
            public static final String AMOUNT   = "amount";
            public static final String UNITS    = "units";
        }

        public static final String CREATE_TABLE = "CREATE TABLE meal_event ( \n" +
                "    meal_id       NOT NULL\n" +
                "                  REFERENCES meal ( id ) ON DELETE CASCADE\n" +
                "                                         ON UPDATE CASCADE,\n" +
                "    event_id      NOT NULL\n" +
                "                  REFERENCES event ( id ) ON DELETE CASCADE\n" +
                "                                          ON UPDATE CASCADE,\n" +
                "    amount   REAL,\n" +
                "    units         REFERENCES units ON DELETE SET NULL\n" +
                "                                  ON UPDATE CASCADE,\n" +
                "    PRIMARY KEY ( meal_id, event_id ) \n" +
                ");\n";
    }
}
