package com.cs.kugou.mvp.view

import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBaseView
import com.cs.kugou.R
import com.cs.kugou.adapter.MainPagerAdapter
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.ui.listen.ListenFragment
import com.cs.kugou.ui.MainActivity
import com.cs.kugou.widget.MyTabView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom.*
import kotlinx.android.synthetic.main.title.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainView(val mContext: MainActivity) : KBaseView<MainContract.Presenter, MainContract.View, MainActivity>(mContext), MainContract.View {


    var fragmentList = arrayListOf<Fragment>()

    override fun init() {
        fragmentList.add(ListenFragment())
        fragmentList.add(ListenFragment())
        fragmentList.add(ListenFragment())

        mContext.viewPager.adapter = MainPagerAdapter(mContext, fragmentList, mContext.supportFragmentManager)
        mContext.tablayout.setupWithViewPager(mContext.viewPager)
        for (i in 0..2) {
            mContext.tablayout.getTabAt(i)?.customView = getTabView(i)
        }

        mContext.ivPlay.setOnClickListener { presenter?.play() }
        mContext.ivPause.setOnClickListener { presenter?.pause() }
        mContext.ivNext.setOnClickListener { presenter?.next() }

        mContext.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Android.log("向Service发送拖动进度")
                EventBus.getDefault().post(SeekEvent(seekBar.progress))//向Service发送拖动进度
            }
        })
    }


    override fun showPlay() {
        mContext.ivPlay.visibility = View.VISIBLE
        mContext.ivPause.visibility = View.GONE
    }

    override fun showPause() {
        mContext.ivPlay.visibility = View.GONE
        mContext.ivPause.visibility = View.VISIBLE
    }
    override fun hidePlay() {
        mContext.ivPlay.visibility = View.GONE
        mContext.ivPause.visibility = View.GONE
    }

    override fun setProgress(progress: Int) {
        mContext.seekBar.progress = progress
    }

    override fun updateMusicInfo(music: Music?) {
        music?.let {
            mContext.tvMusicName.text = it.musicName
            mContext.tvArtist.text = it.singerName
            mContext.seekBar.max = it.duration
        }
    }

    override fun showFragment(isShow: Boolean) {
        mContext.flContent.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun getTabView(position: Int): View {
        var tabview = MyTabView(mContext)
        val tabIcon = tabview.findViewById<ImageView>(R.id.tabIcon)
        when (position) {
            0 -> tabIcon.setImageResource(R.drawable.select_tab0)
            1 -> tabIcon.setImageResource(R.drawable.select_tab1)
            2 -> tabIcon.setImageResource(R.drawable.select_tab2)
        }
        return tabview
    }

    override fun destory() {
    }

    class SeekEvent(var position: Int)
}