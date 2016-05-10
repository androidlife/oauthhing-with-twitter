package com.meg7.soas.oauth.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.meg7.soas.oauth.R;
import com.meg7.soas.oauth.ui.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by laaptu on 4/27/16.
 */
public class TweetPostFragment extends BaseFragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tweet_txt)
    EditText tweetText;

    @Bind(R.id.input_layout)
    TextInputLayout inputLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_tweepost;
    }

    @Override
    protected String getLogTag() {
        return "TweetPostFragment: ";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setStatusBarColor(R.color.colorPrimaryDark);
        setToolbar(toolbar);

        inputLayout.setHint(String.format(context.getString(R.string.write_tweet), 0));
        tweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputLayout.setHint(String.format(context.getString(R.string.write_tweet), s.toString().length()));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        tweetText.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tweetText.getWindowToken(), 0);
    }
}
