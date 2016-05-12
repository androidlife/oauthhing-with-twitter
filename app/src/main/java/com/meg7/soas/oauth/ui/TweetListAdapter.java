package com.meg7.soas.oauth.ui;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meg7.soas.oauth.R;
import com.meg7.soas.oauth.imageloaders.GlideConfigurator;
import com.meg7.soas.oauth.model.Tweet;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        View view;
        if (viewType == EMPTY)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_list, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetListAdapter.TweetViewHolder holder, int position) {
        if (getItemViewType(position) == EMPTY)
            return;
        Tweet tweet = tweets.get(position);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tweet.createdAt + "\n");
        spannableStringBuilder.append(tweet.text);

        spannableStringBuilder.setSpan(new TextAppearanceSpan(
                        holder.profileImageView.getContext(),
                        R.style.TextAppearanceMain_Date),
                0, tweet.createdAt.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.setSpan(new TextAppearanceSpan(
                        holder.profileImageView.getContext(),
                        R.style.TextAppearanceMain),
                tweet.createdAt.length() + 1, spannableStringBuilder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tweetTextView.setText(spannableStringBuilder);
        GlideConfigurator.loadCircularImage(holder.profileImageView.getContext(), tweet.imageUrl, R.color.colorPrimary);

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

        @Nullable
        @Bind(R.id.image_view)
        ImageView profileImageView;
        @Nullable
        @Bind(R.id.tweet_txt)
        TextView tweetTextView;

        public TweetViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
