package com.meg7.soas.oauth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.meg7.soas.oauth.ui.FeedFragment;
import com.meg7.soas.oauth.ui.FragmentCallback;
import com.meg7.soas.oauth.ui.LoginFragment;
import com.meg7.soas.oauth.ui.SplashFragment;


public class MainActivity extends AppCompatActivity implements FragmentCallback {

    //TODO move to another variable if possible
    public static final int FRAG_SPLASH = 0x1, FRAG_FEED = 0x2, FRAG_TWEET_POST = 0x3, FRAG_LOGIN = 0x4;
    private int currentFrag = 0;
    private final String CURRENT_FRAG = "currentFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (false) {
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
        String tag = String.valueOf(currentFrag);
        switch (this.currentFrag) {
            case FRAG_SPLASH:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new SplashFragment(), tag)
                        .commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            case FRAG_FEED:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new FeedFragment(), tag)
                        .commit();
                getSupportFragmentManager().executePendingTransactions();
                break;
            case FRAG_LOGIN:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new LoginFragment(), tag)
                        .addToBackStack(tag).commit();
                getSupportFragmentManager().executePendingTransactions();
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
    public void changeFragment(int fragment) {
        if (fragment < 1 || fragment > 4)
            return;
        setCurrentFragment(fragment);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    @Override
    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(color,null));
        }
    }
}
