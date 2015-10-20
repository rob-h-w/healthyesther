package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;

public class WhereForeignKey {
    @Nonnull
    private String mExpression;

    private WhereForeignKey(@Nonnull String columnName) {
        mExpression = columnName;
    }

    @Nonnull
    public static WhereForeignKey named(@Nonnull String name) {
        return new WhereForeignKey(name);
    }

    @Nonnull
    public WhereForeignKey is(@Nonnull Object value) {
        mExpression += " = " + String.valueOf(value);
        return this;
    }

    @Nonnull
    public Where build() {
        return new Where() {
            @Override
            public String getWhere() {
                return mExpression;
            }
        };
    }
}
