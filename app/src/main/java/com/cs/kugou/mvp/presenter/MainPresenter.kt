package com.cs.kugou.mvp.presenter

import android.content.Context
import android.os.Handler
import android.support.v4.app.Fragment
import android.widget.Toast
import com.cs.framework.Android
import com.cs.framework.mvp.kt.KBasePresenter
import com.cs.kugou.R
import com.cs.kugou.db.Music
import com.cs.kugou.lyric.formats.krc.KrcLyricReader
import com.cs.kugou.mvp.contract.MainContract
import com.cs.kugou.mvp.moudle.MusicMoudle
import com.cs.kugou.service.PlayerService
import com.cs.kugou.ui.MainActivity
import com.cs.kugou.utils.Caches
import com.cs.kugou.utils.LyricUtils
import kotlinx.android.synthetic.main.activity_music.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainPresenter(var mContext: Context) : KBasePresenter<MainContract.Presenter, MainContract.View>(), MainContract.Presenter {

    override fun getPlayList() {
        MusicMoudle.getPlayList {
            MusicMoudle.playList = it as ArrayList<Music>
            //播放列表准备就绪
            Handler().postDelayed({
                var event = PlayerService.MusicActionEvent()
                event.action = PlayerService.ACTION_LOAD
                event.position = Caches.queryInt("playingIndex")
                EventBus.getDefault().post(event)
            }, 300)
        }
    }

    override fun popFragment() {
        (mContext as MainActivity).supportFragmentManager.popBackStack()
        (mContext as MainActivity).topCount--
    }

    override fun addFragment(fragment: Fragment, tag: String) {
        (mContext as MainActivity).supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_in)
                //  .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.flContent, fragment, tag)
                .addToBackStack(tag)
                .commit()

        (mContext as MainActivity).topCount++

    }


    override fun play() {
        //就绪状态和暂停状态可以播放
        if (PlayerService.mCurrentState == PlayerService.STATE_PREPRAED || PlayerService.mCurrentState == PlayerService.STATE_PAUSE) {
            sendMusicActionEvent(PlayerService.ACTION_PLAY)
        } else {
            Toast.makeText(mContext, "请选择一首音乐", Toast.LENGTH_SHORT).show()
        }
    }

    override fun pause() {
        //播放状态可以暂停
        if (PlayerService.mCurrentState == PlayerService.STATE_PLAYING) {
            sendMusicActionEvent(PlayerService.ACTION_PAUSE)
        }
    }

    override fun next() {
        if (!PlayerService.mPlayList.isEmpty()) {
            sendMusicActionEvent(PlayerService.ACTION_NEXT)
        }
    }

    override fun init() {
        EventBus.getDefault().register(this)
    }

    override fun destory() {
        EventBus.getDefault().unregister(this)
    }

    //更新播放栏信息 (发送源PlayerService)
    @Subscribe()
    fun onPlayingInfoEvent(event: PlayerService.PlayingInfoEvent) {
        ui?.updateMusicInfo(event.music)
        ui?.setProgress(event.progress)
    }

    //更新播放状态 (发送源PlayerService)
    @Subscribe
    fun onStateChangeEvent(event: PlayerService.StateChangeEvent) {
        when (event.state) {
            PlayerService.STATE_IDLE -> {
                var default = Music()
                default.musicName = "酷狗音乐"
                default.singerName = "hello kugou"
                default.duration = 100
                ui?.setProgress(0)
                ui?.updateMusicInfo(default)
                ui?.hidePlay()
            }
            PlayerService.STATE_LOADING -> {
                ui?.setProgress(0)
                ui?.showPlay()
                PlayerService.getCurrentMusic()?.let {
                    loadLyric(it) //加载歌词
                }
            }
            PlayerService.STATE_PREPRAED -> {
                ui?.showPlay()
            }
            PlayerService.STATE_PLAYING -> {
                ui?.showPause()
            }
            PlayerService.STATE_PAUSE -> {
                ui?.showPlay()
            }
        }
    }

    //加载歌词
    private fun loadLyric(music: Music) {
        var keyword = if (music.singerName == "未知") music.musicName else music.singerName + " - " + music.musicName

        var callBack = { result: Boolean, path: String ->
            if (result) {   //加载成功
               // PlayerService.mLyricPath = path
                var lyric = KrcLyricReader.readFile(File(path)) //解析歌词
                PlayerService.mLyric = lyric
                Android.log("歌词加载完成")
            } else {//加载失败
                Toast.makeText(mContext, "获取歌词失败", Toast.LENGTH_SHORT).show()
            }
        }
        LyricUtils.loadLyric(keyword, keyword, music.duration.toString(), music.hash, callBack)
    }

    /**
     * 向PlayerService发送指令，控制音乐
     */
    fun sendMusicActionEvent(action: Int) {
        var event = PlayerService.MusicActionEvent()
        event.action = action
        EventBus.getDefault().post(event)
    }
}