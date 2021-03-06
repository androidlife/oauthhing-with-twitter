package com.meg7.soas.oauth.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.meg7.soas.oauth.MainActivity;
import com.meg7.soas.oauth.R;
import com.meg7.soas.oauth.ui.base.BaseFragment;

/**
 */
public class SplashFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.frag_splash;
    }

    @Override
    protected String getLogTag() {
        return "SplashFragment: ";
    }

    public SplashFragment() {

    }

    private void navigateToMain() {
        changeFragment(MainActivity.FRAG_FEED);
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
