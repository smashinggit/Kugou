package com.cs.kugou.ui.listen

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.cs.framework.Android
import com.cs.framework.base.BaseFragment
import com.cs.kugou.App
import com.cs.kugou.R
import com.cs.kugou.adapter.LocalMusicAdapter
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.moudle.MusicMoudle
import com.cs.kugou.ui.MainActivity
import com.cs.kugou.utils.Caches
import com.cs.kugou.utils.MediaUtils
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.title_common.*

/**
 * author :  chensen
 * data  :  2018/1/21
 * desc :本地音乐
 */
class LocalFragment : BaseFragment() {

    override fun init() {
        var titleView = LayoutInflater.from(mContext).inflate(R.layout.title_common, null, false)
        setTitleView(titleView)

        if (App.isFirstSacanLocal) {
            tvTitle.text = "本地音乐"
            showLoadingView()

            MusicMoudle.getLoacalList {
                if (it.isEmpty())
                    showEmptyView()
                else {
                    MusicMoudle.localList = it as ArrayList<Music>
                    showLocalMusic(it)
                    tvTitle.text = "本地音乐(${MusicMoudle.localList.size})"
                    Caches.save("localCount", "${it.size}")
                    showContentView()
                }
                App.isFirstSacanLocal = false
            }
        } else {
            if (MusicMoudle.localList.isEmpty())
                showEmptyView()
            else {
                showLocalMusic(MusicMoudle.localList)
                tvTitle.text = "本地音乐(${MusicMoudle.localList.size})"
            }
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