/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.dropbox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.HealthDbHelper;

import java.io.IOException;

import javax.annotation.Nonnull;

public class DropboxSyncActivity extends DropboxActivity {
    public static final String RESTORE = "restore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPermissionToReadUserContacts();
    }

    @Override
    protected synchronized void loadData() {
        if (lacksRequiredPermissions()) {
            return;
        }

        setBusy(true);

        Boolean restore = false;

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(RESTORE)) {
            restore = intent.getBooleanExtra(RESTORE, false);
        }

        final Boolean backup = !restore;

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (backup) {
                        HealthDbHelper.getInstance().backupToDropbox();
                    } else {
                        HealthDbHelper.getInstance().restoreFromDropbox();
                    }
                } catch (IOException | DbxException e) {
                    runOnUiThread(() -> Toast.makeText(
                            DropboxSyncActivity.this,
                            R.string.dropbox_sync_failed,
                            Toast.LENGTH_SHORT).show());
                    e.printStackTrace();
                } finally {
                    setBusy(false);
                    finish();
                }

                return null;
            }
        };
        task.execute();
    }

    // Identifier for the permission request
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    private boolean lacksRequiredPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED;
    }

    // Called when the user is performing an action which requires the app to read the
    // user's contacts
    public void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (lacksRequiredPermissions()) {
            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.read_contacts_permission_explanation)
                        .setTitle(R.string.dropbox)
                        .setNeutralButton(android.R.string.ok, (dialog, which) -> {
                            // Fire off an async request to actually get the permission
                            // This will show the standard permission request dialog UI
                            DropboxSyncActivity.this.requestPermissions(new String[]{
                                            Manifest.permission.READ_CONTACTS},
                                    READ_CONTACTS_PERMISSIONS_REQUEST);
                        }
                        );
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@Nonnull String permission) {
        return permission.equals(Manifest.permission.READ_CONTACTS)
                || super.shouldShowRequestPermissionRationale(permission);

    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @Nonnull String permissions[],
                                           @Nonnull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.read_contacts_permission_granted, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.read_contacts_permission_denied, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
