package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;

public class AndWhere implements Where {
    private StringBuilder mWhere = new StringBuilder();

    public AndWhere(@Nonnull Where... wheres) {
        String separator = "";
        for (Where where : wheres) {
            mWhere.append(separator).append("(").append(where.getWhere()).append(")");
            separator = " AND ";
        }
    }

    @Override
    public String getWhere() {
        return mWhere.toString();
    }
}