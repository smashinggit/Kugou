package com.cs.kugou.mvp.view

import android.view.View
import android.widget.SeekBar
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBaseView
import com.cs.kugou.bean.Lyric
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
class MusicView(private val mActivity: MusicActivity) : KBaseView<MusicContract.Presenter, MusicContract.View, MusicActivity>(mActivity), MusicContract.View {

    override fun showPlay() {
        mActivity.ivPlayInMUsic.visibility = View.VISIBLE
        mActivity.ivPauseInMUsic.visibility = View.INVISIBLE
    }

    override fun showPause() {
        mActivity.ivPlayInMUsic.visibility = View.INVISIBLE
        mActivity.ivPauseInMUsic.visibility = View.VISIBLE
    }

    override fun updateMusicInfo(music: Music?) {
        music?.let {
            mActivity.tvNameInMusic.text = it.musicName
            mActivity.tvSingerNameInMusic.text = it.singerName
            mActivity.seekBarInMusic.max = it.duration
            mActivity.tvTotalTime.text = it.duration.toString()
        }
    }

    override fun setProgress(progress: Int) {
        mActivity.seekBarInMusic.progress = progress
        mActivity.tvCurrentTime.text = progress.toString()
        mActivity.lyricView.setCurrentTimeMillis(progress.toLong())
    }

    override fun setLyric(lyric: Lyric) {
        mActivity.lyricView.setLyric(lyric)
    }

    override fun resetLyric() {
        mActivity.lyricView.reset()
    }

    override fun init() {
        var music = PlayerService.getCurrentMusic()
        music?.let {
            mActivity.tvNameInMusic.text = it.musicName
            mActivity.tvSingerNameInMusic.text = it.singerName
            mActivity.seekBarInMusic.max = it.duration
            mActivity.tvTotalTime.text = it.duration.toString()
        }

        mActivity.ivPlayInMUsic.setOnClickListener { presenter?.play() }
        mActivity.ivPauseInMUsic.setOnClickListener { presenter?.pause() }
        mActivity.ivNextInMusic.setOnClickListener { presenter?.next() }
        mActivity.ivPreInMusic.setOnClickListener { presenter?.next() }
        mActivity.ivBackInMusic.setOnClickListener { mActivity.finish() }
        mActivity.ivShareInMusic.setOnClickListener { }

        mActivity.seekBarInMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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