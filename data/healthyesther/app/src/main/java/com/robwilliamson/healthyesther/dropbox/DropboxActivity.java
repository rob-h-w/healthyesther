/*
   © Robert Williamson 2014-2016.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.dropbox;


import android.content.SharedPreferences;

import com.dropbox.core.android.Auth;
import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.BusyActivity;


/**
 * Base class for Activities that require auth tokens
 * Will redirect to auth flow if needed
 */
public abstract class DropboxActivity extends BusyActivity {
    private static final String PREFERENCES_NAME = "com.robwilliamson.healthyesther.dropbox";
    private static final String TOKEN_NAME = "access-token";

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getPreferences();
        String accessToken = prefs.getString(TOKEN_NAME, null);
        if (accessToken == null) {
            accessToken = Auth.getOAuth2Token();
            if (accessToken == null) {
                Auth.startOAuth2Authentication(this, BuildConfig.HEALTHY_ESTHER_DROPBOX_KEY);
            } else {
                prefs.edit().putString(TOKEN_NAME, accessToken).apply();
                initAndLoadData(accessToken);
            }
        } else {
            initAndLoadData(accessToken);
        }
    }

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
        loadData();
    }

    private SharedPreferences getPreferences() {
        return getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
    }

    protected abstract void loadData();
}
