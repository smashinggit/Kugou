package com.cs.kugou.ui

import android.support.v7.widget.LinearLayoutManager
import com.cs.framework.base.BaseFragment
import com.cs.kugou.R
import com.cs.kugou.adapter.LocalMusicAdapter
import com.cs.kugou.db.PlayList
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
        val localMusicList = MusicUtils.getLocalMusic(context!!)
        var adapter = LocalMusicAdapter(context!!, localMusicList)

//        for (i in 0 until localMusicList.size) {
//            val music = localMusicList[i]
//            val info = MusicUtils.formatMusic(music)
//
//            var playList = PlayList()
//            playList.name = info?.get(0)
//            playList.artist = info?.get(1)
//            playList.url = music.url
//            playList.save()
//        }
//        Caches.scanLoaclMusic()

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        tvTitle.text = "本地音乐(${localMusicList.size})"
        ivBack.setOnClickListener {
            (activity as MainActivity).mPresenter.popFragment()
        }
    }


    override fun getLayoutId() = R.layout.fragment_local
}