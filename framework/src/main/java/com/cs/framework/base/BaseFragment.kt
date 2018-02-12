package com.cs.framework.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cs.framework.Android

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:
 */
abstract class BaseFragment : Fragment() {

    lateinit var mStateLayout: StateLayout
    lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(getLayoutId(), null, false)
        mStateLayout = StateLayout(inflater, view)
        mStateLayout.showContentView()
        return mStateLayout.rootView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = activity
        init()
    }

    fun setTitleView(title: View) {
        mStateLayout.setTitleView(title)
    }

    fun showLoadingView() {
        mStateLayout.showLoadingView()
    }

    fun showEmptyView() {
        mStateLayout.showEmptyView()
    }

    fun showErrorView() {
        mStateLayout.showErrorView()
    }

    fun showNonetView() {
        mStateLayout.showNonetView()
    }

    fun showContentView() {
        mStateLayout.showContentView()
    }

    abstract fun init()

    abstract fun getLayoutId(): Int

    fun Context.toast(message: Any) {
        Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
    }

    fun Context.toastLong(message: Any) {
        Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show()
    }

    fun Context.log(message: Any) {
        Android.log(message)
    }

    fun Context.log(tag: String, message: Any) {
        Android.log(tag, message)
    }
}