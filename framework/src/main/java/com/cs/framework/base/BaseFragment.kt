package com.cs.framework.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:
 */
abstract class BaseFragment : Fragment() {

    lateinit var mStateLayout: StateLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater?.inflate(getLayoutId(), null, false)
        mStateLayout = StateLayout(inflater, view)

        return mStateLayout.rootView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    abstract fun init()

    abstract fun getLayoutId(): Int
}