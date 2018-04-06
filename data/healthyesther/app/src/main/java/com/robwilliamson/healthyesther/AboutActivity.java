/*
  © Robert Williamson 2014-2016.
  This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import javax.annotation.Nonnull;

public class AboutActivity extends BaseFragmentActivity {

    @SuppressLint({"PrivateResource", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        @SuppressWarnings("RedundantCast") WebView webView =
                Utils.checkNotNull((WebView) findViewById(R.id.about_content));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new Version(), "_version");
        webView.loadUrl("file:///android_res/raw/about.htm");

        ActionBar actionBar = Utils.checkNotNull(getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getTitle());

        @SuppressWarnings("RedundantCast") Toolbar toolbar =
                Utils.checkNotNull((Toolbar) findViewById(R.id.toolbar));
        toolbar.setTitle(getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_launcher);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private static class Version {
        @JavascriptInterface
        @Nonnull
        public String name() {
            return BuildConfig.VERSION_NAME;
        }

        @JavascriptInterface
        public int code() {
            return BuildConfig.VERSION_CODE;
        }

        @JavascriptInterface
        @Nonnull
        public String toString() {
            return "v" + name() + " (" + code() + ")";
        }
    }
}
