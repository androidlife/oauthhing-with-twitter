package com.meg7.soas.oauth.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meg7.soas.oauth.api.DataCallbackMain;
import com.meg7.soas.oauth.ui.FragmentCallback;

import java.util.ArrayList;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by laaptu on 4/12/16.
 */
public abstract class BaseFragment extends Fragment {
    abstract protected int getLayoutId();

    abstract protected String getLogTag();

    public Context context;

    private FragmentCallback fragmentCallback;

    private ArrayList<DataCallbackMain> dataCallbacks;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initContext(context);
        Timber.tag(getLogTag()).d("onAttach (Context context)");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initContext(activity);
        Timber.tag(getLogTag()).d("onAttach (Activity activity ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(getLogTag()).d("onCreate() ");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        Timber.tag(getLogTag()).d("onCreateView()");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.tag(getLogTag()).d("onActivityCreated()");
        dataCallbacks = new ArrayList<>();
    }

    public void addDataCallback(DataCallbackMain dataCallbackMain) {
        dataCallbacks.add(dataCallbackMain);
    }

    public void removeDataCallback(DataCallbackMain dataCallbackMain) {
        dataCallbacks.remove(dataCallbackMain);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.tag(getLogTag()).d("onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.tag(getLogTag()).d("onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.tag(getLogTag()).d("onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.tag(getLogTag()).d("onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.tag(getLogTag()).d("onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.tag(getLogTag()).d("onDestroy()");
        nullifyContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.tag(getLogTag()).d("onDetach()");
    }

    private void initContext(Context context) {
        this.context = context;
        if (this.context instanceof FragmentCallback)
            fragmentCallback = (FragmentCallback) this.context;
    }


    private void nullifyContext() {
        this.context = null;
        fragmentCallback = null;
        clearAllDataCallbacks();
    }

    private void clearAllDataCallbacks() {
        if (dataCallbacks != null && dataCallbacks.size() > 0) {
            for (DataCallbackMain dataCallbackMain : dataCallbacks)
                dataCallbackMain.cancel();
            dataCallbacks.clear();
        }
        dataCallbacks = null;
    }


    public void changeFragment(int fragment) {
        Timber.d("ChangeFragment, is callback null =%b", fragmentCallback == null);
        if (fragmentCallback != null)
            fragmentCallback.changeFragment(fragment);

    }

    public void setToolbar(Toolbar toolbar) {
        if (fragmentCallback != null)
            fragmentCallback.setToolbar(toolbar);
    }

    public void setStatusBarColor(int color) {
        if (fragmentCallback != null)
            fragmentCallback.setStatusBarColor(color);
    }

    public void showMessage(String message) {
        if (fragmentCallback != null) {
            fragmentCallback.showMessage(message);
        }
    }

    public void onBackPress() {
        if (fragmentCallback != null)
            fragmentCallback.onBackPress();
    }

    public void setToolbarTitle(String title) {
        if (fragmentCallback != null)
            fragmentCallback.setToolbarTitle(title);
    }


}
