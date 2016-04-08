package com.meg7.soas.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.meg7.soas.oauth.ui.base.BaseActivity;

import butterknife.Bind;
import timber.log.Timber;

public class OAuthLoginActivity extends BaseActivity {


    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    @Bind(R.id.webview)
    WebView webView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    String url, callbackUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_oauth_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isIntentValid()) {
            Timber.e("Activity launched without valid url or callback url");
            this.finish();
            return;
        }
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWebView();
    }

    private boolean isIntentValid() {
        if (getIntent() != null && getIntent().hasExtra(URL) && getIntent().hasExtra(CALLBACK_URL)) {
            url = getIntent().getStringExtra(URL);
            callbackUrl = getIntent().getStringExtra(CALLBACK_URL);
            if (URLUtil.isValidUrl(url) && URLUtil.isValidUrl(callbackUrl))
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWebView() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Timber.d("On Page Load Started for url = %s", url);
                if (url.contains("denied")) {
                    Toast.makeText(OAuthLoginActivity.this, "Denied by user", Toast.LENGTH_SHORT).show();
                    Timber.e("Authorization denied by user");
                    OAuthLoginActivity.this.finish();
                } else if (url.contains(callbackUrl) && url.contains("oauth_verifier")) {
                    Timber.d("Callback url by user authorization = %s", url);
                    Uri uri = Uri.parse(url);
                    String oauthToken = uri.getQueryParameter("oauth_token");
                    String oauthVerifier = uri.getQueryParameter("oauth_verifier");
                    Timber.d("OAuth Token = %s", oauthToken);
                    Timber.d("OAuth Verifier = %s", oauthVerifier);
                    Intent intent = new Intent();
                    intent.putExtra(AUTHORIZE_TOKEN, oauthToken);
                    intent.putExtra(AUTHORIZE_VERIFIER, oauthVerifier);
                    setResult(Activity.RESULT_OK, intent);
                    webView.stopLoading();
                    OAuthLoginActivity.this.finish();

                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Timber.d("On Page Load Finished for url  = %s", url);

            }
        });
        webView.loadUrl(url);


    }

    public static final String URL = "url", CALLBACK_URL = "callbackUrl", AUTHORIZE_TOKEN = "oauth_token",
            AUTHORIZE_VERIFIER = "oauth_verifier";

    public static Intent launchActivity(Context context, String url, String callbackUrl) {
        Intent intent = new Intent(context, OAuthLoginActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(CALLBACK_URL, callbackUrl);
        return intent;
    }


}
