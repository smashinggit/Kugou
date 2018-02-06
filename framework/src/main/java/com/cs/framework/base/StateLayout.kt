package com.cs.framework.base

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.cs.framework.R

/**
 *
 * author : ChenSen
 * data : 2018/1/18
 * desc:
 */
class StateLayout(var inflater: LayoutInflater, var contentView: View) {
    private var rootView: FrameLayout = inflater.inflate(R.layout.layout_root, null, false) as FrameLayout

    private var loading: View
    private var empty: View
    private var error: View
    private var nonet: View

    init {
        contentView.visibility = View.GONE
        rootView.addView(contentView)

        loading = rootView.getChildAt(0)
        empty = rootView.getChildAt(1)
        error = rootView.getChildAt(2)
        nonet = rootView.getChildAt(3)
    }


    fun showLoadingView() {
        showView(0)
    }

    fun showEmptyView() {
        showView(1)
    }

    fun showErrorView() {
        showView(2)
    }

    fun showNonetView() {
        showView(3)
    }

    fun showContentView() {
        showView(4)
    }

    private fun showView(i: Int) {
        loading.visibility = View.GONE
        empty.visibility = View.GONE
        error.visibility = View.GONE
        nonet.visibility = View.GONE
        contentView.visibility = View.GONE

        when (i) {
            0 -> loading.visibility = View.VISIBLE
            1 -> empty.visibility = View.VISIBLE
            2 -> error.visibility = View.VISIBLE
            3 -> nonet.visibility = View.VISIBLE
            4 -> contentView.visibility = View.VISIBLE
        }
    }

    fun rootView() = rootView
}