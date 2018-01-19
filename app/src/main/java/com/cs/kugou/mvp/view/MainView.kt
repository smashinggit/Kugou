package com.cs.kugou.mvp.view

import android.widget.SeekBar
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBaseView
import com.cs.kugou.adapter.MainPagerAdapter
import com.cs.kugou.audio.Audio
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainView(val activity: MainActivity) : KBaseView<MainContract.Presenter, MainContract.View, MainActivity>(activity), MainContract.View {


    override fun play() {
    }

    override fun pause() {
    }

    override fun next() {
    }

    override fun pre() {
    }

    override fun init() {
        initAudio()

        activity.viewPager.adapter = MainPagerAdapter(activity, activity.supportFragmentManager)



        activity.btnPlay.setOnClickListener {
            Android.log(presenter == null)
            presenter?.play()
        }
        activity.btnPause.setOnClickListener { presenter?.pause() }

    }

    private fun initAudio() {
        Audio.setOnStateChangedListener(object : Audio.OnStateChangedListener {
            override fun onPrepared() {
                activity.seekBar.max = Audio.getDuration()
                activity.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            Audio.seekToPositon(progress)
                        }
                    }
                })
            }

            override fun onProgressChanged(progress: Int) {
                activity.seekBar.progress = progress
            }
        })
    }

    override fun destory() {
    }
}