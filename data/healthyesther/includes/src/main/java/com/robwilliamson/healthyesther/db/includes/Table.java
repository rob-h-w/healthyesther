/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.includes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public abstract class Table {
    private final List<Upgrader> mUpgraders = new ArrayList<>();

    public abstract
    @Nonnull
    String getName();

    public abstract void create(Transaction transaction);

    public abstract void drop(Transaction transaction);

    public void upgrade(Transaction transaction, int from, int to) {
        for (Upgrader upgrader : mUpgraders) {
            upgrader.upgrade(transaction, from, to);
        }
    }

    public void addUpgrader(@Nonnull Upgrader upgrader) {
        mUpgraders.add(upgrader);
    }

    public interface Upgrader {
        void upgrade(Transaction transaction, int from, int to);
    }

    public static class TooManyRowsException extends RuntimeException {
        public TooManyRowsException(@Nonnull Table table, int count, @Nonnull Where where) {
            super("Expected to get 0 or 1 rows from table " + table.getName() + " matching selection statement \"" + where.getWhere() + "\", instead got " + count + ".");
        }
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(@Nonnull Table table, @Nonnull Where where) {
            super("Could not find a row from table " + table.getName() + " matching selection statement \"" + where.getWhere() + "\".");
        }
    }
}
