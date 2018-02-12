package com.cs.kugou.ui.listen

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.cs.framework.base.BaseFragment
import com.cs.kugou.R
import com.cs.kugou.adapter.LocalMusicAdapter
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.moudle.MusicMoudle
import com.cs.kugou.ui.MainActivity
import com.cs.kugou.utils.Caches
import kotlinx.android.synthetic.main.fragment_local.*
import kotlinx.android.synthetic.main.title_common.*

/**
 * author :  chensen
 * data  :  2018/1/21
 * desc :喜欢音乐
 */
class LikeFragment : BaseFragment() {

    override fun init() {
        var titleView = LayoutInflater.from(mContext).inflate(R.layout.title_common, null, false)
        setTitleView(titleView)
        tvTitle.text = "喜欢·歌单"
        showLoadingView()

        MusicMoudle.getLikeList(object : MusicMoudle.onReadCompletedListener {
            override fun onReadComplete(list: List<Music>) {
                if (list.isEmpty())
                    showEmptyView()
                else {
                    showContentView()
                    MusicMoudle.likeList = list as ArrayList<Music>
                    showLikeMusic(list)
                    Caches.save("likeCount", "${list.size}")
                }
            }
        })

        ivBack.setOnClickListener {
            (activity as MainActivity).mPresenter.popFragment()
        }
    }


    private fun showLikeMusic(localMusicList: ArrayList<Music>) {
        var adapter = LocalMusicAdapter(mContext, localMusicList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun getLayoutId() = R.layout.fragment_local
}