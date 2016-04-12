package com.meg7.soas.oauth.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.meg7.soas.oauth.R;

/**
 */
public class SplashFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.frag_splash;
    }

    public SplashFragment() {

    }

    private void navigateToMain() {
        if (context != null && context instanceof FragmentCallback)
            ((FragmentCallback) context).navigateToMain();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //just a 3 second delay for no reason
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToMain();
            }
        }, 2000);
    }
}
