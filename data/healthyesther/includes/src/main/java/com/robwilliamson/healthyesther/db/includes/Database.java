/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.includes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public abstract class Database {
    public static void create(@Nonnull Transaction transaction, @Nonnull Table[] tables) {
        for (Table table : tables) {
            table.create(transaction);
        }
    }

    public static void drop(@Nonnull Transaction transaction, @Nonnull Table[] tables) {
        List<Table> reverse = Arrays.asList(Arrays.copyOf(tables, tables.length));
        Collections.reverse(reverse);

        for (Table table : reverse) {
            table.drop(transaction);
        }
    }

    public static void upgrade(@Nonnull Transaction transaction, int from, int to, @Nonnull Table[] tables) {
        for (Table table : tables) {
            table.upgrade(transaction, from, to);
        }
    }

    @Nonnull
    public abstract Cursor select(@Nonnull Where where, @Nonnull Table table);

    @Nonnull
    public abstract Cursor select(@Nonnull Where where, @Nonnull Table table, @Nonnull Order order);

    public abstract Transaction getTransaction();
}
