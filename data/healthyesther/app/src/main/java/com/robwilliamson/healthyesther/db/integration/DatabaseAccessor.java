/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.integration;

import android.database.sqlite.SQLiteDatabase;

import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Table;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class DatabaseAccessor extends HealthDatabase {
    final static String HAPPINESS_NAME = "Happiness";
    final static String ENERGY_NAME = "Energy";
    final static String DROWSINESS_NAME = "Drowsiness";
    private final static Table[] TABLES;
    private final static List<Class<? extends Upgrade>> UPGRADES = new ArrayList<>();

    static {
        TABLES = new Table[HealthDatabase.TABLES.length - 1];
        for (int i = 0, k = 0; i < TABLES.length; i++, k++) {
            if (HealthDatabase.TABLES[k] == HealthDatabase.ANDROID_METADATA_TABLE) {
                k++;
            }

            TABLES[i] = HealthDatabase.TABLES[k];
        }

        UPGRADES.add(null); // from 0 not supported.
        UPGRADES.add(null); // from 1 not supported.
        UPGRADES.add(null); // from 2 not supported.
        UPGRADES.add(V3.class);
        UPGRADES.add(V4.class);
    }

    public static void configure(com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
        activateForeignKeyChecking(((Transaction) transaction).db());
    }

    static void activateForeignKeyChecking(@Nonnull SQLiteDatabase sqLiteDb) {
        sqLiteDb.execSQL("PRAGMA foreign_keys = ON;");
    }

    static void deactivateForeignKeyChecking(@Nonnull SQLiteDatabase sqLiteDb) {
        sqLiteDb.execSQL("PRAGMA foreign_keys = OFF;");
    }

    static void checkForeignKeys(@Nonnull SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_key_check;");
    }

    public static void create(com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
        Database.create(transaction, TABLES);
        EventTypeTable.populateTable(transaction);

        V4.createDefaultJudgmentFor(new HealthScoreTable.Row(HAPPINESS_NAME, true, "Happy", "Sad"), 5, transaction);
        V4.createDefaultJudgmentFor(new HealthScoreTable.Row(ENERGY_NAME, true, "Energetic", "Tired"), 5, transaction);
        V4.createDefaultJudgmentFor(new HealthScoreTable.Row(DROWSINESS_NAME, true, "Sleepy", "Awake"), 1, transaction);
    }

    public static void drop(com.robwilliamson.healthyesther.db.includes.Transaction transaction) {
        Database.drop(transaction, TABLES);
    }

    public static void upgrade(com.robwilliamson.healthyesther.db.includes.Transaction transaction, int from, int to) {
        Database.upgrade(transaction, from, to, TABLES);

        for (int i = from; i < Contract.VERSION; i++) {
            try {
                Class<? extends Upgrade> upgradeClass = UPGRADES.get(i);
                if (upgradeClass == null) {
                    throw new UnsupportedOperationException("Cannot upgrade from v" + i);
                }
                UPGRADES.get(i).newInstance().upgradeFrom((Transaction) transaction);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IncrementalUpgradeException(i, from, to, e);
            }
        }
    }

    private static class IncrementalUpgradeException extends RuntimeException {
        public IncrementalUpgradeException(int i, int from, int to, Throwable cause) {
            super(
                    "Error upgrading from database version " + from + " to v" + to + ". Failed upgrading incrementally from v" + i + " to v" + (i + 1),
                    cause);
        }
    }
}
