package com.cs.kugou.mvp.view

import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBaseView
import com.cs.kugou.R
import com.cs.kugou.adapter.MainPagerAdapter
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.ui.ListenFragment
import com.cs.kugou.ui.MainActivity
import com.cs.kugou.view.MyTavView
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

        activity.ivPlay.setOnClickListener {
            presenter?.play()
        }
        activity.ivPause.setOnClickListener { presenter?.pause() }
    }


    override fun showPlay() {
        activity.ivPlay.visibility = View.VISIBLE
        activity.ivPause.visibility = View.GONE
    }

    override fun showPause() {
        activity.ivPlay.visibility = View.GONE
        activity.ivPause.visibility = View.VISIBLE
    }

    override fun setProgress(progress: Int) {
        activity.seekBar.progress = progress
    }

    override fun shwoMusicInfo(music: Music?) {
        music?.let {
            activity.tvMusicName.text = it.name
            activity.tvName.text = it.artist
            activity.seekBar.max = it.duration!!
            Android.log("shwoMusicInfo ${it.name}")
            Android.log("shwoMusicInfo ${ activity.tvName.text}")
        }
    }

    override fun showFragment(isShow: Boolean) {
        activity.flContent.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun getTabView(position: Int): View {
        var tabview = MyTavView(activity)
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
}