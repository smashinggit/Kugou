package com.cs.kugou.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cs.kugou.R
import com.cs.kugou.adapter.MusicClassifyAdapter
import kotlinx.android.synthetic.main.fragment_listen.*


/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
@SuppressLint("ValidFragment")
class ListenFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_listen, null)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        recyclerview.adapter = MusicClassifyAdapter(context!!)
        recyclerview.layoutManager = GridLayoutManager(context!!, 3, GridLayoutManager.VERTICAL, false)

        llLoacl.setOnClickListener {
            (activity as MainActivity).mPresenter.addFragment(LocalMusicFragment(), "LocalMusicFragment")
        }
        llLike.setOnClickListener { }
        llDownLoad.setOnClickListener { }
        llRecent.setOnClickListener { }
    }

}