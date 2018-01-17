package com.cs.framework.mvp;

/**
 * Created by chensen on 2018/1/17.
 */

public interface V<T extends P> {
    void bindPresenter(T presenter);

    void onBind();

    void onUnBind();
}
