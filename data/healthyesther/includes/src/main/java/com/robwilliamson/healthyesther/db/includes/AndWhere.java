package com.robwilliamson.healthyesther.db.includes;

public class AndWhere implements Where {
    private StringBuilder mWhere = new StringBuilder();

    public AndWhere(Where... wheres) {
        String separator = "";
        for (Where where : wheres) {
            mWhere.append(separator + "(" + where.getWhere() + ")");
            separator = " AND ";
        }
    }

    @Override
    public String getWhere() {
        return mWhere.toString();
    }
}
