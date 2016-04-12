package com.meg7.soas.oauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meg7.soas.oauth.ui.base.SplashFragment;


public class MainActivity extends AppCompatActivity {

    private final int FRAG_SPLASH = 0x1, FRAG_FEED = 0x2, FRAG_TWEET_POST = 0x3;
    private int currentFrag = FRAG_SPLASH;
    private final String CURRENT_FRAG = "currentFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_FRAG)) {
            currentFrag = savedInstanceState.getInt(CURRENT_FRAG, FRAG_SPLASH);
        }
        setCurrentFragment();
    }

    private void setCurrentFragment() {
        switch (currentFrag) {
            case FRAG_SPLASH:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SplashFragment(), String.valueOf(currentFrag)).commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAG, currentFrag);
    }

    private void goToTestActivity() {
        startActivity(new Intent(this, TestActivity.class));
        this.finish();
    }
}
