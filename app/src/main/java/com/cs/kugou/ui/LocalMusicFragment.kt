package com.cs.kugou.ui

import android.support.v7.widget.LinearLayoutManager
import com.cs.framework.base.BaseFragment
import com.cs.kugou.R
import com.cs.kugou.adapter.LocalMusicAdapter
import com.cs.kugou.db.Music
import com.cs.kugou.module.MusicModule
import com.cs.kugou.utils.Caches
import com.cs.kugou.utils.MusicUtils
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.title_common.*

/**
 * author :  chensen
 * data  :  2018/1/21
 * desc :本地音乐
 */
class LocalMusicFragment : BaseFragment() {

    override fun init() {
        val localMusicList: ArrayList<Music>

        //没有扫描过歌曲
        if (!Caches.isScaned()) {
            localMusicList = MusicUtils.getLocalMusic(context!!)//获取本地音乐列表
            MusicModule.saveLocalMusic(localMusicList)//保存到数据库
            MusicModule.loaclList = localMusicList
            Caches.scaned()
        } else {
            localMusicList = MusicModule.loaclList
        }

        if (localMusicList.isEmpty()) {
            showNoLocalMusic()
            tvTitle.text = "本地音乐"
        } else {
            showLocalMusic(localMusicList)
            tvTitle.text = "本地音乐(${localMusicList.size})"
        }

        ivBack.setOnClickListener {
            (activity as MainActivity).mPresenter.popFragment()
        }
    }

    fun showNoLocalMusic() {
        showEmpty()
    }

    fun showLocalMusic(localMusicList: ArrayList<Music>) {
        var adapter = LocalMusicAdapter(context!!, localMusicList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    override fun getLayoutId() = R.layout.fragment_local
}