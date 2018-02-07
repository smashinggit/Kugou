package com.cs.kugou.ui

import android.support.v7.widget.LinearLayoutManager
import com.cs.framework.base.BaseFragment
import com.cs.kugou.R
import com.cs.kugou.adapter.LocalMusicAdapter
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.moudle.MusicMoudle
import com.cs.kugou.utils.Caches
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.title_common.*

/**
 * author :  chensen
 * data  :  2018/1/21
 * desc :本地音乐
 */
class LocalMusicFragment : BaseFragment() {

    override fun init() {

        if (MusicMoudle.localList.isEmpty()) {
            tvTitle.text = "本地音乐"
            showLoadingView()

            MusicMoudle.getLoacalList(object : MusicMoudle.onReadCompletedListener {
                override fun onReadComplete(list: List<Music>) {
                    if (list.isEmpty())
                        showEmptyView()
                    else {
                        showContentView()
                        MusicMoudle.localList = list as ArrayList<Music>
                        showLocalMusic(list)
                        tvTitle.text = "本地音乐(${MusicMoudle.localList.size})"
                        Caches.save("localCount", "${list.size}")
                    }
                }
            })
        } else {
            showLocalMusic(MusicMoudle.localList)
            tvTitle.text = "本地音乐(${MusicMoudle.localList.size})"
        }

        ivBack.setOnClickListener {
            (activity as MainActivity).mPresenter.popFragment()
        }
    }


    private fun showLocalMusic(localMusicList: ArrayList<Music>) {
        var adapter = LocalMusicAdapter(mContext, localMusicList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId() = R.layout.fragment_local
}