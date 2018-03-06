package com.cs.kugou.mvp.view

import android.view.View
import android.widget.SeekBar
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBaseView
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.contract.MusicContract
import com.cs.kugou.service.PlayerService
import com.cs.kugou.ui.MusicActivity
import kotlinx.android.synthetic.main.activity_music.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * author : ChenSen
 * data : 2018/2/22
 * desc: 播放音乐页面
 */
class MusicView(val activity: MusicActivity) : KBaseView<MusicContract.Presenter, MusicContract.View, MusicActivity>(activity), MusicContract.View {


    override fun showPlay() {
        activity.ivPlayInMUsic.visibility = View.VISIBLE
        activity.ivPauseInMUsic.visibility = View.INVISIBLE
    }

    override fun showPause() {
        activity.ivPlayInMUsic.visibility = View.INVISIBLE
        activity.ivPauseInMUsic.visibility = View.VISIBLE
    }

    override fun updateMusicInfo(music: Music?) {
        music?.let {
            activity.tvNameInMusic.text = it.musicName
            activity.tvSingerNameInMusic.text = it.singerName
            activity.seekBarInMusic.max = it.duration
            activity.tvTotalTime.text = it.duration.toString()
        }
    }

    override fun setProgress(progress: Int) {
        activity.seekBarInMusic.progress = progress
        activity.tvCurrentTime.text = progress.toString()
    }

    override fun init() {
        var music = PlayerService.getCurrentMusic()
        music?.let {
            activity.tvNameInMusic.text = it.musicName
            activity.tvSingerNameInMusic.text = it.singerName
            activity.seekBarInMusic.max = it.duration
            activity.tvTotalTime.text = it.duration.toString()
        }

        activity.ivPlayInMUsic.setOnClickListener { presenter?.play() }
        activity.ivPauseInMUsic.setOnClickListener { presenter?.pause() }
        activity.ivNextInMusic.setOnClickListener { presenter?.next() }
        activity.ivPreInMusic.setOnClickListener { presenter?.next() }
        activity.ivBackInMusic.setOnClickListener { activity.finish() }
        activity.ivShareInMusic.setOnClickListener { }

        activity.seekBarInMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Android.log("向Service发送拖动进度")
                EventBus.getDefault().post(MainView.SeekEvent(seekBar.progress))//向Service发送拖动进度
            }
        })
    }

    override fun destory() {
    }

}