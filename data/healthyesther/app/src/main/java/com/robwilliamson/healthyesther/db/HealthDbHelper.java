package com.robwilliamson.healthyesther.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;

/**
 * Accessor for the health database.
 */
public final class HealthDbHelper extends SQLiteOpenHelper {
    private static volatile HealthDbHelper sInstance = null;
    public static boolean sDebug = false;

    private final Context mContext;

    public static synchronized HealthDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HealthDbHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private HealthDbHelper(Context context) {
        super(context.getApplicationContext(), Contract.NAME, null, Contract.VERSION);
        mContext = context;
    }

    @Override
    public synchronized void onConfigure(SQLiteDatabase sqLiteDatabase) {
        super.onConfigure(sqLiteDatabase);

        if (!sqLiteDatabase.isReadOnly()) {
            sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    @Override
    public synchronized void onCreate(SQLiteDatabase sqLiteDatabase) {
        Contract.getInstance().create(sqLiteDatabase);
    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase sqLiteDatabase, int from, int to) {
        Contract.getInstance().upgrade(sqLiteDatabase, from, to);
    }

    public synchronized void backupToDropbox() throws IOException {
        // Ensure the db isn't in use
        close();

        Utils.File.copy(mContext.getDatabasePath(getDatabaseName()).getAbsolutePath(),
                Utils.File.Dropbox.dbFile());
    }
}
