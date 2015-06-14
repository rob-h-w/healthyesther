package com.robwilliamson.healthyesther.type;

public class DbObject {
    private String type;
    private String name;
    private boolean withoutRowId;
    private String ddl;
    private Column[] columns;
    private Constraint[] constraints;

    public DbObject() {}

    public boolean isTable() {
        return type.equals("table");
    }
}
