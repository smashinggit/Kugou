package com.cs.kugou.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.cs.kugou.R

/**
 * Created by chensen on 2018/1/20.
 */
class MyTabView : FrameLayout {
    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.item_tab,this)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
    }

}