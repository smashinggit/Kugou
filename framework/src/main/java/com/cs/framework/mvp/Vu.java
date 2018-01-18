package com.cs.framework.mvp;

import android.app.Activity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:
 */

public abstract class Vu<T extends P> implements V<T> {
    private final Unbinder mUnbinder;
    protected View mRootView;
    protected T mPresenter;
    protected Activity mComtext;


    public Vu(Activity context, View rootView) {
        this.mComtext = context;
        this.mRootView = rootView;
        this.mUnbinder = ButterKnife.bind(this, mRootView);
    }

    @Override
    public void bindPresenter(T presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onBind() {
    }

    @Override
    public void onUnBind() {
        mUnbinder.unbind();
    }
}
