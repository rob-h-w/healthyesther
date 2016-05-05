/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.OkHttpRequestor;
import com.dropbox.core.v2.DbxClientV2;

import java.util.Locale;

import javax.annotation.Nonnull;

public class DropboxClientFactory {

    private static DbxClientV2 sDbxClient;

    public static void init(@Nonnull String accessToken) {
        if (sDbxClient == null) {
            String userLocale = Locale.getDefault().toString();
            DbxRequestConfig requestConfig = new DbxRequestConfig(
                    "healthy Esther",
                    userLocale,
                    OkHttpRequestor.INSTANCE);

            sDbxClient = new DbxClientV2(requestConfig, accessToken);
        }
    }

    @Nonnull
    public static DbxClientV2 getClient() {
        if (sDbxClient == null) {
            throw new IllegalStateException("Client not initialized.");
        }
        return sDbxClient;
    }
}
