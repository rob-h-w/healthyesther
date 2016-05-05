/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.type;

import com.robwilliamson.healthyesther.Strings;

import java.util.Arrays;

public class DbObject {
    private String type;
    private String name;
    private boolean withoutRowId;
    private String ddl;
    private Column[] columns;
    private Constraint[] constraints;

    public DbObject() {
    }

    public boolean isTable() {
        return type.equals("table");
    }

    public void copyTo(DbObject other) {
        other.type = type;
        other.name = name;
        other.withoutRowId = withoutRowId;
        other.ddl = ddl;
        other.columns = columns == null ? null : Arrays.copyOf(columns, columns.length);
        other.constraints = constraints == null ? null : Arrays.copyOf(constraints, constraints.length);
    }

    public String getName() {
        return Strings.stripSquareBrackets(name);
    }

    public Column[] getColumns() {
        if (columns == null) {
            columns = new Column[]{};
        }

        return columns;
    }

    public Constraint[] getConstraints() {
        if (constraints == null) {
            constraints = new Constraint[]{};
        }

        return constraints;
    }

    public String getDdl() {
        return ddl;
    }
}
