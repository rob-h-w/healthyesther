/*
   © Robert Williamson 2014-2016.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.http.StandardHttpRequestor;
import com.dropbox.core.v2.DbxClientV2;

import javax.annotation.Nonnull;

public class DropboxClientFactory {

    private static DbxClientV2 sDbxClient;

    public static void init(@Nonnull String accessToken) {
        if (sDbxClient == null) {
            StandardHttpRequestor requestor = new StandardHttpRequestor(
                    StandardHttpRequestor.Config.DEFAULT_INSTANCE);
            DbxRequestConfig requestConfig = DbxRequestConfig
                    .newBuilder("healthy Esther")
                    .withHttpRequestor(requestor)
                    .build();

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
