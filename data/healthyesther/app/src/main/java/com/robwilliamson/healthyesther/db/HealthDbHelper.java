package com.robwilliamson.healthyesther.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;

/**
 * Accessor for the health database.
 */
public final class HealthDbHelper extends SQLiteOpenHelper {
    private static final Object sSync = new Object();
    public static boolean sDebug = false;
    private static volatile HealthDbHelper sInstance = null;
    private final Context mContext;

    private HealthDbHelper(Context context) {
        super(context.getApplicationContext(), Contract.NAME, null, Contract.VERSION);
        mContext = context;
    }

    public static HealthDbHelper getInstance(Context context) {
        synchronized (sSync) {
            if (sInstance == null) {
                sInstance = new HealthDbHelper(context.getApplicationContext());
            }

            return sInstance;
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
            Contract.getInstance().create(sqLiteDatabase);
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
            // Ensure the db isn't in use
            close();

            Utils.File.copy(mContext.getDatabasePath(getDatabaseName()).getAbsolutePath(),
                    Utils.File.Dropbox.dbFile());
        }
    }

    public void restoreFromDropbox() throws IOException {
        synchronized (sSync) {
            // Ensure the db isn't in use
            close();

            Utils.File.copy(Utils.File.Dropbox.dbFile(),
                    mContext.getDatabasePath(getDatabaseName()).getAbsolutePath());

            // We'll need to be re-created after this event in case the dropbox version is different
            // from this one.
            sInstance = null;
        }
    }
}
