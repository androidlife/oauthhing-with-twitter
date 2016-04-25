package com.meg7.soas.oauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meg7.soas.oauth.ui.FeedFragment;
import com.meg7.soas.oauth.ui.FragmentCallback;
import com.meg7.soas.oauth.ui.SplashFragment;


public class MainActivity extends AppCompatActivity implements FragmentCallback {

    private final int FRAG_SPLASH = 0x1, FRAG_FEED = 0x2, FRAG_TWEET_POST = 0x3;
    private int currentFrag = 0;
    private final String CURRENT_FRAG = "currentFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(false){
            goToTestActivity();
            return;
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_FRAG)) {
            setCurrentFragment(savedInstanceState.getInt(CURRENT_FRAG, FRAG_SPLASH));
            return;
        }
        setCurrentFragment(FRAG_FEED);
    }

    private void setCurrentFragment(int currentFrag) {
        if (this.currentFrag == currentFrag)
            return;
        this.currentFrag = currentFrag;
        switch (this.currentFrag) {
            case FRAG_SPLASH:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SplashFragment(), String.valueOf(currentFrag)).commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            case FRAG_FEED:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new FeedFragment(), String.valueOf(currentFrag)).commit();
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


    //called from SplashFragment to navigate to Main Fragment
    @Override
    public void navigateToMain() {
        setCurrentFragment(FRAG_FEED);
    }
}
