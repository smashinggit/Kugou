package com.cs.kugou.ui

import android.support.v7.widget.LinearLayoutManager
import com.cs.framework.base.BaseFragment
import com.cs.kugou.R
import com.cs.kugou.adapter.LocalMusicAdapter
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.moudle.MusicMoudle
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.title_common.*

/**
 * author :  chensen
 * data  :  2018/1/21
 * desc :本地音乐
 */
class LocalMusicFragment : BaseFragment() {

    override fun init() {
        var localList = MusicMoudle.localList

        if (localList.isEmpty()) {
            showNoLocalMusic()
            tvTitle.text = "本地音乐"
        } else {
            showLocalMusic(localList)
            tvTitle.text = "本地音乐(${localList.size})"
        }

        ivBack.setOnClickListener {
            (activity as MainActivity).mPresenter.popFragment()
        }
    }

    fun showNoLocalMusic() {
        showEmpty()
    }

    fun showLocalMusic(localMusicList: ArrayList<Music>) {
        var adapter = LocalMusicAdapter(mContext, localMusicList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId() = R.layout.fragment_local
}