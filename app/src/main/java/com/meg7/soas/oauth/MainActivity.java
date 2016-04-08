package com.meg7.soas.oauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wordpress.laaptu.oauth.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToTestActivity();
    }

    private void goToTestActivity(){
        startActivity(new Intent(this,TestActivity.class));
        this.finish();
    }
}
