package com.robwilliamson.healthyesther.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.robwilliamson.healthyesther.App;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.integration.DatabaseWrapperClass;

import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * Accessor for the health database.
 */
public final class HealthDbHelper extends SQLiteOpenHelper {
    private static final Object sSync = new Object();
    public static boolean sDebug = false;
    private static volatile HealthDbHelper sInstance = null;
    private static volatile DatabaseWrapperClass sDatabase = null;
    private final Context mContext;

    private HealthDbHelper(Context context) {
        super(context.getApplicationContext(), Contract.NAME, null, Contract.VERSION);
        mContext = context;
    }

    @Nonnull
    private static HealthDbHelper getInstance(@Nonnull Context context) {
        synchronized (sSync) {
            if (sInstance == null) {
                sInstance = new HealthDbHelper(context.getApplicationContext());
            }

            return sInstance;
        }
    }

    @Nonnull
    public static HealthDbHelper getInstance() {
        if (sInstance == null) {
            sInstance = getInstance(App.getInstance());
        }

        return sInstance;
    }

    @Nonnull
    public static Database getDatabase() {
        return getDatabaseWrapper();
    }

    public static DatabaseWrapperClass getDatabaseWrapper() {
        synchronized (sSync) {
            if (sDatabase == null || !sDatabase.getSqliteDatabase().isOpen()) {
                sDatabase = new DatabaseWrapperClass(getInstance().getWritableDatabase());
            }

            return sDatabase;
        }
    }

    public static void closeDb() {
        synchronized (sSync) {
            if (sDatabase != null) {
                sDatabase.getSqliteDatabase().close();
                sDatabase = null;
                sInstance = null;
            }
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        synchronized (sSync) {
            sDatabase = new DatabaseWrapperClass(sqLiteDatabase);
            try (Transaction transaction = sDatabase.getTransaction()) {
                DatabaseAccessor.configure(transaction);
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        synchronized (sSync) {
            sDatabase = new DatabaseWrapperClass(sqLiteDatabase);
            try (Transaction transaction = sDatabase.getTransaction()) {
                DatabaseAccessor.create(transaction);
                transaction.commit();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int from, int to) {
        synchronized (sSync) {
            sDatabase = new DatabaseWrapperClass(sqLiteDatabase);
            try (Transaction transaction = sDatabase.getTransaction()) {
                DatabaseAccessor.upgrade(transaction, from, to);
                transaction.commit();
            }
        }
    }

    public void backupToDropbox() throws IOException {
        synchronized (sSync) {
            closeDb();
            Utils.File.copy(mContext.getDatabasePath(getDatabaseName()).getAbsolutePath(),
                    Utils.File.Dropbox.dbFile());
        }
    }

    public void restoreFromDropbox(@Nonnull Context context) throws IOException {
        synchronized (sSync) {
            // Close the DB.
            closeDb();

            Utils.File.copy(Utils.File.Dropbox.dbFile(),
                    mContext.getDatabasePath(getDatabaseName()).getAbsolutePath());

            // We'll need to be re-created after this event in case the dropbox version is different
            // from this one.
            sInstance = getInstance(context);
        }
    }
}
