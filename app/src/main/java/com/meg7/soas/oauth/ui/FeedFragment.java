package com.meg7.soas.oauth.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.meg7.soas.oauth.R;
import com.meg7.soas.oauth.ui.base.BaseFragment;

import butterknife.Bind;

/**
 */
public class FeedFragment extends BaseFragment {

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_feed;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Hello how are you ",Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
