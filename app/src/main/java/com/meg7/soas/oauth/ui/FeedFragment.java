package com.meg7.soas.oauth.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.meg7.soas.oauth.MainActivity;
import com.meg7.soas.oauth.PrefManager;
import com.meg7.soas.oauth.R;
import com.meg7.soas.oauth.api.DataManager;
import com.meg7.soas.oauth.imageloaders.GlideConfigurator;
import com.meg7.soas.oauth.model.Tweet;
import com.meg7.soas.oauth.ui.base.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 */
public class FeedFragment extends BaseFragment {

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.tweet_list)
    RecyclerView recyclerView;

    @Bind(R.id.image)
    SquaredImageView profileImage;

    LinearLayoutManager linearLayoutManager;

    TweetListAdapter tweetListAdapter;

    ArrayList<Tweet> tweets = new ArrayList<>();

    private enum LoginState {
        Logged, NotLogged;
    }

    private LoginState loginState;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_feed;
    }

    @Override
    protected String getLogTag() {
        return "FeedFragment: ";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setStatusBarColor(android.R.color.transparent);
        setToolbar(toolbar);

        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        loginState = PrefManager.getInstance().isUserLoggedIn() ? LoginState.Logged : LoginState.NotLogged;
        if (loginState == LoginState.NotLogged)
            initEmptyList();
        else {
            initUserList();
        }
    }

    private void initUserList() {
        fab.setImageDrawable(context.getDrawable(R.drawable.ic_post_tweet));
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        setToolbarTitle(PrefManager.getInstance().getUserScreenName());
        if (PrefManager.getInstance().getUserProfileImage() == null) {
            fetchUserInfo();
        } else {
            addTitleNImageToToolbar();
        }
    }

    private void addTitleNImageToToolbar() {
        GlideConfigurator.loadCircularImage(context, PrefManager.getInstance().getUserProfileImage(), R.color.colorPrimary);
    }

    private void fetchUserInfo() {

    }

    private void initEmptyList() {
        //// TODO: 4/25/16  check for logged in or not
        tweetListAdapter = new TweetListAdapter(tweets);
        recyclerView.setAdapter(tweetListAdapter);
        fab.setImageDrawable(context.getDrawable(R.drawable.ic_login));
        swipeRefreshLayout.setEnabled(false);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setTitle("Not logged in");
    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        if (loginState == LoginState.NotLogged) {
            // go for web view login and stuff
            changeFragment(MainActivity.FRAG_LOGIN);
        } else {
            //go for status post
            changeFragment(MainActivity.FRAG_TWEET_POST);
        }
    }


}
