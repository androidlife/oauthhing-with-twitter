package com.meg7.soas.oauth.ui;

import android.support.v7.widget.Toolbar;

/**
 */
public interface FragmentCallback {
    void changeFragment(int fragment);

    void setToolbar(Toolbar toolbar);

    void setStatusBarColor(int color);

    void showMessage(String message);

    void onBackPress();
}
