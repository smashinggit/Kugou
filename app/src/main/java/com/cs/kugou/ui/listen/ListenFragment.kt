package com.cs.kugou.ui.listen

import android.annotation.SuppressLint
import android.support.v7.widget.GridLayoutManager
import com.cs.framework.Android
import com.cs.framework.base.BaseFragment
import com.cs.kugou.R
import com.cs.kugou.adapter.MusicClassifyAdapter
import com.cs.kugou.constant.Constant
import com.cs.kugou.ui.MainActivity
import com.cs.kugou.utils.Caches
import kotlinx.android.synthetic.main.fragment_listen.*


/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc: 听 页面
 */
@SuppressLint("ValidFragment")
class ListenFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_listen


    override fun init() {
        recyclerview.adapter = MusicClassifyAdapter(mContext)
        recyclerview.layoutManager = GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)

        tvLocalNum.text = if (Caches.query(Constant.LOCAL_COUNT).isEmpty()) "0" else Caches.query(Constant.LOCAL_COUNT)
        tvLikeNum.text = if (Caches.query(Constant.LIKE_COUNT).isEmpty()) "0" else Caches.query(Constant.LIKE_COUNT)
        tvDownNum.text = if (Caches.query(Constant.DOWN_COUNT).isEmpty()) "0" else Caches.query(Constant.DOWN_COUNT)
        tvRecentNum.text = if (Caches.query(Constant.RECENT_COUNT).isEmpty()) "0" else Caches.query(Constant.RECENT_COUNT)

        llLoacl.setOnClickListener { (activity as MainActivity).mPresenter.addFragment(LocalFragment(), "LocalMusicFragment") }
        llLike.setOnClickListener { (activity as MainActivity).mPresenter.addFragment(LikeFragment(), "LikeMusicFragment") }
        llDownLoad.setOnClickListener { (activity as MainActivity).mPresenter.addFragment(DownLoadFragment(), "DownLoadFragment") }
        llRecent.setOnClickListener { (activity as MainActivity).mPresenter.addFragment(RecentFragment(), "RecentFragment") }

        Android.log("ListenFragment  init()")
    }

}