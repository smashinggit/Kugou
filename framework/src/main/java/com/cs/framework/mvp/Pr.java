package com.cs.framework.mvp;

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:
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
