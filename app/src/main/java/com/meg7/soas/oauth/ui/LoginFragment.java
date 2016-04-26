package com.meg7.soas.oauth.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.meg7.soas.oauth.R;
import com.meg7.soas.oauth.api.DataCallback;
import com.meg7.soas.oauth.api.DataCallbackMain;
import com.meg7.soas.oauth.api.DataManager;
import com.meg7.soas.oauth.api.oauth.Token;
import com.meg7.soas.oauth.ui.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by laaptu on 4/25/16.
 */
public class LoginFragment extends BaseFragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected String getLogTag() {
        return "LoginFragment: ";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setStatusBarColor(R.color.colorPrimaryDark);
        setToolbar(toolbar);
    }

    private void firstFetchRequestToken() {
        DataManager.getInstance().getRequestToken(requestTokenCallback);
    }

    private DataCallbackMain<Token> requestTokenCallback = new DataCallbackMain<>(new DataCallback<Token>() {
        @Override
        public void onResponse(Token response) {

        }

        @Override
        public void onFailure(String error) {

        }

        @Override
        public void cancel() {

        }
    });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requestTokenCallback.cancel();
    }
}
