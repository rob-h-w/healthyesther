/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;

public class OrderBy {
    public static Factory column() {
        return new Factory();
    }

    public static Order allOf(@Nonnull final Order... orders) {
        return new Order() {
            @Nonnull
            @Override
            public String getOrder() {
                String separator = "";
                StringBuilder orderStringBuilder = new StringBuilder();
                for (Order order : orders) {
                    orderStringBuilder
                            .append(separator)
                            .append("(")
                            .append(order.getOrder())
                            .append(")");
                    separator = ", ";
                }
                return orderStringBuilder.toString();
            }
        };
    }

    enum Direction {
        ASC,
        DESC
    }

    public static class Factory implements Order {
        private Direction mDirection = Direction.ASC;
        private String mName;

        public Factory named(@Nonnull String name) {
            mName = name;
            return this;
        }

        public Factory asc() {
            mDirection = Direction.ASC;
            return this;
        }

        public Factory desc() {
            mDirection = Direction.DESC;
            return this;
        }

        @Nonnull
        @Override
        public String getOrder() {
            return "[" + mName + "] " + mDirection.toString();
        }
    }
}
