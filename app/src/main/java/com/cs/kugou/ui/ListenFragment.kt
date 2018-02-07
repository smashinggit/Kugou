package com.cs.kugou.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cs.framework.Android
import com.cs.framework.base.BaseFragment
import com.cs.kugou.R
import com.cs.kugou.adapter.MusicClassifyAdapter
import com.cs.kugou.utils.Caches
import kotlinx.android.synthetic.main.fragment_listen.*


/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
@SuppressLint("ValidFragment")
class ListenFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_listen


    override fun init() {
        recyclerview.adapter = MusicClassifyAdapter(context!!)
        recyclerview.layoutManager = GridLayoutManager(context!!, 3, GridLayoutManager.VERTICAL, false)

        tvLocalNum.text = if (Caches.query("localCount").isEmpty()) "0" else Caches.query("localCount")
        tvLikeNum.text = if (Caches.query("likeCount").isEmpty()) "0" else Caches.query("likeCount")
        tvDownNum.text = if (Caches.query("downCount").isEmpty()) "0" else Caches.query("downCount")
        tvRecentNum.text = if (Caches.query("recentCount").isEmpty()) "0" else Caches.query("recentCount")

        Android.log("ListenFragment  init()")

        llLoacl.setOnClickListener {
            (activity as MainActivity).mPresenter.addFragment(LocalMusicFragment(), "LocalMusicFragment")
        }
        llLike.setOnClickListener { }
        llDownLoad.setOnClickListener { }
        llRecent.setOnClickListener { }
    }

}