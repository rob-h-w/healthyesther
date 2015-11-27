package com.robwilliamson.healthyesther.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
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
    public static HealthDbHelper getInstance(@Nonnull Context context) {
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
            throw new IllegalStateException("HealthDbHelper.getInstance(Context) must be called at least once prior to this!");
        }

        return sInstance;
    }

    public static Database getDatabase() {
        return getDatabaseWrapper();
    }

    public static DatabaseWrapperClass getDatabaseWrapper() {
        synchronized (sSync) {
            if (sDatabase == null) {
                sDatabase = new DatabaseWrapperClass(getInstance().getWritableDatabase());
            }

            return sDatabase;
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        synchronized (sSync) {
            super.onConfigure(sqLiteDatabase);

            if (!sqLiteDatabase.isReadOnly()) {
                sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        synchronized (sSync) {
            Database database = new DatabaseWrapperClass(sqLiteDatabase);
            try (Transaction transaction = database.getTransaction()) {
                HealthDatabase.create(transaction);
                transaction.commit();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int from, int to) {
        synchronized (sSync) {
            Contract.getInstance().upgrade(sqLiteDatabase, from, to);
        }
    }

    public void backupToDropbox() throws IOException {
        synchronized (sSync) {
            Utils.File.copy(mContext.getDatabasePath(getDatabaseName()).getAbsolutePath(),
                    Utils.File.Dropbox.dbFile());
        }
    }

    public void restoreFromDropbox() throws IOException {
        synchronized (sSync) {
            Utils.File.copy(Utils.File.Dropbox.dbFile(),
                    mContext.getDatabasePath(getDatabaseName()).getAbsolutePath());

            // We'll need to be re-created after this event in case the dropbox version is different
            // from this one.
            sInstance = null;
        }
    }
}
