package com.cs.kugou.ui

import com.cs.framework.Android
import com.cs.framework.base.BaseFragment
import com.cs.kugou.R
import com.cs.kugou.utils.MusicUtils

/**
 * author :  chensen
 * data  :  2018/1/21
 * desc :本地音乐
 */
class LocalMusicFragment : BaseFragment() {


    override fun init() {
        val localMusicList = MusicUtils.getLocalMusic(context!!)
    }


    override fun getLayoutId() = R.layout.fragment_local

}