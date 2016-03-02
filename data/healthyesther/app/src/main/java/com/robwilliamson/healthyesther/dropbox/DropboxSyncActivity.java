package com.robwilliamson.healthyesther.dropbox;

import android.content.Intent;
import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.robwilliamson.healthyesther.db.HealthDbHelper;

import java.io.IOException;

public class DropboxSyncActivity extends DropboxActivity {
    public static final String RESTORE = "restore";

    @Override
    protected void loadData() {
        setBusy(true);

        Boolean restore = false;

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(RESTORE)) {
            restore = intent.getBooleanExtra(RESTORE, false);
        }

        final Boolean backup = !restore;

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (backup) {
                        HealthDbHelper.getInstance().backupToDropbox();
                    } else {
                        HealthDbHelper.getInstance().restoreFromDropbox();
                    }
                } catch (IOException | DbxException e) {
                    // TODO: Some failure UI here.
                    e.printStackTrace();
                }
                setBusy(false);
                finish();
                return null;
            }
        };
        task.execute();
    }
}
