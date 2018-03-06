package com.cs.kugou.ui.listen

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.cs.framework.Android
import com.cs.framework.base.BaseFragment
import com.cs.kugou.App
import com.cs.kugou.R
import com.cs.kugou.adapter.LocalMusicAdapter
import com.cs.kugou.constant.Constant
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
 * desc :  本地音乐
 */
class LocalFragment : BaseFragment() {

    override fun init() {
        var titleView = LayoutInflater.from(mContext).inflate(R.layout.title_common, null, false)
        setTitleView(titleView)

        //如果是第一次使用，首先扫描本地音乐
        if (Caches.queryBoolean(Constant.isFirstScan, true)) {
            var list = arrayListOf<Music>()
            var temp = System.currentTimeMillis()
            showLoadingView()
            //扫描本地歌曲
            MediaUtils.scanLocalMusicByContentResolver(mContext, object : MediaUtils.ForeachListener {
                override fun foreach(music: Music) {
                    list.add(music)
                }

                override fun filter(hash: String): Boolean {
                    return MusicMoudle.isExistMusic(hash)
                }

                override fun onComplete() {
                    //保存到数据库
                    if (list.size > 0) {
                        MusicMoudle.insert(list)
                        MusicMoudle.localList = list
                        showLocalMusic(list)
                        tvTitle.text = "本地音乐(${MusicMoudle.localList.size})"
                        showContentView()
                    }else{
                        showEmptyView()
                    }

                    Caches.save(Constant.LOCAL_COUNT, "${list.size}")
                    Caches.saveBoolean(Constant.isFirstScan, false)
                    App.needReadLocal = false

                    var def = System.currentTimeMillis() - temp
                    Android.log("扫描本地歌曲 共${list.size} 首   用时 $def 毫秒")
                }
            })
        } else if (!Caches.queryBoolean(Constant.isFirstScan, true)
                && App.needReadLocal) {//如果扫描过本地音乐，每次启动应用打开本页面的第一次从数据库中读取
            tvTitle.text = "本地音乐"
            showLoadingView()

            MusicMoudle.getLoacalList {
                if (it.isEmpty())
                    showEmptyView()
                else {
                    MusicMoudle.localList = it as ArrayList<Music>
                    showLocalMusic(it)
                    tvTitle.text = "本地音乐(${MusicMoudle.localList.size})"
                    Caches.save(Constant.LOCAL_COUNT, "${it.size}")
                    showContentView()
                }
                Caches.saveBoolean(Constant.isFirstScan, false)
                App.needReadLocal = false
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