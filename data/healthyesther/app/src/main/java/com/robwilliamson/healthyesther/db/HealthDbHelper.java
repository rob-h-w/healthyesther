package com.robwilliamson.healthyesther.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.SearchResult;
import com.dropbox.core.v2.files.UploadUploader;
import com.robwilliamson.healthyesther.App;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.integration.DatabaseWrapperClass;
import com.robwilliamson.healthyesther.dropbox.DropboxClientFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    @Nonnull
    private static String getDropboxFileName() {
        String name = App.getInstance().getUsername();

        if (name == null) {
            name = "test";
        }

        return name + ".db3";
    }

    public void backupToDropbox() throws IOException, DbxException {
        synchronized (sSync) {
            closeDb();

            @Nonnull
            final DbxClientV2 client = DropboxClientFactory.getClient();
            @Nonnull
            final UploadUploader uploader = com.robwilliamson.healthyesther.Utils.checkNotNull(client.files.upload("/" + getDropboxFileName()));

            try (FileInputStream fileInputStream = new FileInputStream(mContext.getDatabasePath(getDatabaseName()).getAbsolutePath())) {
                uploader.uploadAndFinish(fileInputStream);
            }
        }
    }

    public void restoreFromDropbox() throws IOException, DbxException {
        synchronized (sSync) {

            @Nonnull
            final DbxClientV2 client = DropboxClientFactory.getClient();
            @Nonnull
            final String dbFilename = getDropboxFileName();
            @Nonnull
            final SearchResult result = client.files.search("", dbFilename);

            if (result.getMatches().isEmpty()) {
                return;
            }

            // Close the DB.
            closeDb();

            @Nonnull
            final DbxDownloader<FileMetadata> dbxDownloader = com.robwilliamson.healthyesther.Utils.checkNotNull(client.files.download("/" + dbFilename));

            try (FileOutputStream fileOutputStream = new FileOutputStream(mContext.getDatabasePath(getDatabaseName()).getAbsolutePath())) {
                dbxDownloader.download(fileOutputStream);
            }

            // We'll need to be re-created after this event in case the dropbox version is different
            // from this one.
            sInstance = getInstance(App.getInstance());
        }
    }
}
