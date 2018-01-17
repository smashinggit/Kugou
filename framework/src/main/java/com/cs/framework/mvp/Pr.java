package com.cs.framework.mvp;

/**
 * Created by chensen on 2018/1/17.
 */

public abstract class Pr<T extends V> implements P<T> {
    protected T mView;

    public Pr(T view) {
        this.mView = view;
        mView.bindPresenter(this);
    }

    @Override
    public void regist() {
    }

    @Override
    public void unRegist() {
    }

    @Override
    public void strt() {
        mView.onBind();
    }
}
