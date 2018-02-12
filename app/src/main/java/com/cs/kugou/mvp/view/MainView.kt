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
import com.cs.kugou.view.MyTabView
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
class MainView(val activity: MainActivity) : KBaseView<MainContract.Presenter, MainContract.View, MainActivity>(activity), MainContract.View {


    var fragmentList = arrayListOf<Fragment>()

    override fun init() {
        fragmentList.add(ListenFragment())
        fragmentList.add(ListenFragment())
        fragmentList.add(ListenFragment())

        activity.viewPager.adapter = MainPagerAdapter(activity, fragmentList, activity.supportFragmentManager)
        activity.tablayout.setupWithViewPager(activity.viewPager)
        for (i in 0..2) {
            activity.tablayout.getTabAt(i)?.customView = getTabView(i)
        }

        activity.ivPlay.setOnClickListener { presenter?.play() }
        activity.ivPause.setOnClickListener { presenter?.pause() }
        activity.ivNext.setOnClickListener { presenter?.next() }

        activity.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
        activity.ivPlay.visibility = View.VISIBLE
        activity.ivPause.visibility = View.GONE
    }

    override fun showPause() {
        activity.ivPlay.visibility = View.GONE
        activity.ivPause.visibility = View.VISIBLE
    }
    override fun hidePlay() {
        activity.ivPlay.visibility = View.GONE
        activity.ivPause.visibility = View.GONE
    }

    override fun setProgress(progress: Int) {
        activity.seekBar.progress = progress
    }

    override fun updateMusicInfo(music: Music?) {
        music?.let {
            activity.tvMusicName.text = it.musicName
            activity.tvArtist.text = it.singerName
            activity.seekBar.max = it.duration
        }
    }

    override fun showFragment(isShow: Boolean) {
        activity.flContent.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun getTabView(position: Int): View {
        var tabview = MyTabView(activity)
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