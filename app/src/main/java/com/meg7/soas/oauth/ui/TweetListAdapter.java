package com.meg7.soas.oauth.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meg7.soas.oauth.R;
import com.meg7.soas.oauth.model.Tweet;

import java.util.ArrayList;

/**
 */
public class TweetListAdapter extends RecyclerView.Adapter<TweetListAdapter.TweetViewHolder> {

    private ArrayList<Tweet> tweets;
    private final int EMPTY = 0x1, NOT_EMPTY = 0x2;
    private int type;

    public TweetListAdapter(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
        type = this.tweets.size() == 0 ? EMPTY : NOT_EMPTY;
    }

    @Override
    public TweetListAdapter.TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == EMPTY)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_list, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetListAdapter.TweetViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public int getItemCount() {
        return type == EMPTY ? 1 : tweets.size();
    }

    static class TweetViewHolder extends RecyclerView.ViewHolder {

        public TweetViewHolder(View itemView) {
            super(itemView);
        }
    }
}
