package com.cs.kugou.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cs.kugou.R
import com.cs.kugou.db.Music
import com.cs.kugou.mvp.moudle.MusicMoudle
import com.cs.kugou.service.PlayerService
import org.greenrobot.eventbus.EventBus

/**
 *
 * author : ChenSen
 * data : 2018/1/22
 * desc:
 */
class LocalMusicAdapter(var context: Context, var list: ArrayList<Music>) : RecyclerView.Adapter<LocalMusicAdapter.MyHolder>() {

    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        val music = list[position]

        holder?.tvSingerName?.text = music.singerName
        holder?.tvMusicName?.text = music.musicName

        //点击后播放音乐，并该将列表加入播放列表，保存到数据库
        holder?.itemView?.setOnClickListener {
            MusicMoudle.savePlayListToDB(list, object : MusicMoudle.onCompletedListener {
                override fun onComplete() {
                    var event = PlayerService.MusicActionEvent()
                    event.action = PlayerService.ACTION_LOAD_PLAY
                    event.position = position
                    EventBus.getDefault().post(event)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_localmusic, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount() = list.size

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivAdd = itemView.findViewById<ImageView>(R.id.ivAdd)
        var tvMusicName = itemView.findViewById<TextView>(R.id.tvMusicNameItem)
        var tvSingerName = itemView.findViewById<TextView>(R.id.tvSingerNameItem)
    }
}