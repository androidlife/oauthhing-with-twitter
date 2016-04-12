package com.meg7.soas.oauth.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import timber.log.Timber;

/**
 */
public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    //constructor is essential, as this needs to be initialized

    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * This method is called
     * 1. During layout initialization : where it checks each and every child and passes
     * that children on dependency.So it traverses each and every child
     * Since we are getting all the children, we can make comparison based on view id and
     * other properties
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        Timber.d("layoutDependsOn()");
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    /**
     * Seems like once the dependency is found, or once the view is found.
     * It looks for dependency property change
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        Timber.d("onDependentViewChanged()");
        float snackTranslationY = dependency.getTranslationY();
        float snackHeight = dependency.getHeight();
        Timber.d("Snack TranslationY =%f,Snack y pos =%f and  Snack Height =%f", snackTranslationY, snackHeight,dependency.getY());
        float translationY = Math.min(0, snackTranslationY - snackHeight);
        Timber.d("TranslationY = %f",translationY);
        child.setTranslationY(translationY);
        return true;
    }
}
