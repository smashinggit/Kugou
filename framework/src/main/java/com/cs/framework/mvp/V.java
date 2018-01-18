package com.cs.framework.mvp;

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:
 */

public interface V<T extends P> {
    void bindPresenter(T presenter);

    void onBind();

    void onUnBind();
}
