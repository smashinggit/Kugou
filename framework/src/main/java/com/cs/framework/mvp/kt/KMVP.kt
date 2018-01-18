package com.cs.framework.mvp.kt

import android.app.Activity

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:
 */
inline fun <reified P : KPresenter<P, V>, reified V : KView<P, V>> P.bind(ui: V): P {
    this.ui = ui
    ui.presenter = this
    this.init()
    this.ui!!.init()
    return this
}

inline fun <reified P : KPresenter<P, V>, reified V : KView<P, V>> P.unbind() {
    this.ui?.let {
        it.destory()
        it.presenter = null
    }
    this.ui = null
    this.destory()
}

inline fun <reified P : KPresenter<P, V>, reified V : KView<P, V>> V.bind(presenter: P): V {
    this.presenter = presenter
    presenter.ui = this
    this.init()
    this.presenter!!.init()
    return this
}

inline fun <reified P : KPresenter<P, V>, reified V : KView<P, V>> V.unbind() {
    this.presenter?.let {
        it.destory()
        it.ui = null
    }
    this.presenter = null
    this.destory()
}


interface KPresenter<P : KPresenter<P, V>, V : KView<P, V>> {
    var ui: V?
    fun init()
    fun destory()
}


interface KView<P : KPresenter<P, V>, V : KView<P, V>> {
    var presenter: P?
    fun init()
    fun destory()
}

abstract class KBasePresenter<P : KPresenter<P, V>, V : KView<P, V>> : KPresenter<P, V> {
    override var ui: V? = null
}

abstract class KBaseView<P : KPresenter<P, V>, V : KView<P, V>, out A : Activity>(activity: A) : KView<P, V> {
    override var presenter: P? = null
}
