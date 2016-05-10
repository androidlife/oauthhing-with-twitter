package com.meg7.soas.oauth.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.meg7.soas.oauth.PrefManager;
import com.meg7.soas.oauth.R;
import com.meg7.soas.oauth.api.ApiEndPoints;
import com.meg7.soas.oauth.api.DataCallback;
import com.meg7.soas.oauth.api.DataCallbackMain;
import com.meg7.soas.oauth.api.DataManager;
import com.meg7.soas.oauth.api.oauth.OAuthHelper;
import com.meg7.soas.oauth.api.oauth.Token;
import com.meg7.soas.oauth.ui.base.BaseFragment;

import butterknife.Bind;
import timber.log.Timber;

/**
 * Created by laaptu on 4/25/16.
 */
public class LoginFragment extends BaseFragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    @Bind(R.id.webview)
    WebView webView;

    @Bind(R.id.progressbar_web)
    ProgressBar progressBarWeb;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected String getLogTag() {
        return "LoginFragment: ";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setStatusBarColor(R.color.colorPrimaryDark);
        setToolbar(toolbar);
        firstFetchRequestToken();
    }

    private void firstFetchRequestToken() {
        DataManager.getInstance().getRequestToken(requestTokenCallback);
    }

    private DataCallbackMain<Token> requestTokenCallback = new DataCallbackMain<>(new DataCallback<Token>() {
        @Override
        public void onResponse(Token response) {
            secondBeginUserAuthentication(response);
        }

        @Override
        public void onFailure(String error) {
            onError(error);
        }

        @Override
        public void cancel() {

        }
    });


    Token requestToken;

    private void secondBeginUserAuthentication(Token token) {
        requestToken = token;
        String authenticationUrl = OAuthHelper.generateAuthorizationUrl(requestToken.token);
        Timber.d("AuthenticationUrl = %s", authenticationUrl);
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarWeb.setVisibility(View.VISIBLE);
                progressBarWeb.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    progressBarWeb.setVisibility(View.GONE);
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Timber.d("OnPage Load Started for %s", url);
                if (url.contains("denied")) {
                    onError("Denied by user");
                } else if (url.contains(ApiEndPoints.CALLBACK_URL) && url.contains("oauth_verifier")) {
                    Uri uri = Uri.parse(url);
                    String oauthToken = uri.getQueryParameter("oauth_token");
                    String oauthVerifier = uri.getQueryParameter("oauth_verifier");

                    Timber.d("OAuth Token = %s", oauthToken);
                    Timber.d("OAuth Verifier = %s", oauthVerifier);
                    webView.stopLoading();
                    thirdRequestAccessToken(oauthVerifier);

                }
            }
        });

        webView.loadUrl(authenticationUrl);

    }

    private void thirdRequestAccessToken(String oauthVerifier) {
        webView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        DataManager.getInstance().getAccessToken(accessTokenCallback, requestToken, oauthVerifier);
    }

    private DataCallbackMain<Token> accessTokenCallback = new DataCallbackMain<>(new DataCallback<Token>() {
        @Override
        public void onResponse(Token response) {
            storeAccessToken(response);
        }

        @Override
        public void onFailure(String error) {
            onError(error);
        }

        @Override
        public void cancel() {

        }
    });

    private DataCallbackMain<Token> accessTokenCallback1 = new DataCallbackMain<Token>(){
        @Override
        public void onResponse(Token response) {
            super.onResponse(response);
        }

        @Override
        public void onFailure(String error) {
            super.onFailure(error);
        }
    };

    private void storeAccessToken(Token token) {
        //store it into shared preference
        PrefManager.getInstance().addAuthToken(token.token, token.tokenSecret);
        PrefManager.getInstance().storeUserInfo(token.userId, token.screenName);
        onBackPress();
    }

    private void onError(String error) {
        Timber.e("Error : %s", error);
        showMessage(error);
        onBackPress();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requestTokenCallback.cancel();
        accessTokenCallback.cancel();

        if (true)
            return;

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
    }
}
