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

/**
 *
 * author : ChenSen
 * data : 2018/1/22
 * desc:
 */
class LocalMusicAdapter(var context: Context, var list: ArrayList<Music>) : RecyclerView.Adapter<LocalMusicAdapter.MyHolder>() {


    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        val music = list[position]

        holder?.tvArtist?.text = music.artist
        holder?.tvMusicName?.text = music.name

    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_localmusic, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount() = list.size

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivAdd = itemView.findViewById<ImageView>(R.id.ivAdd)
        var tvMusicName = itemView.findViewById<TextView>(R.id.tvMusicName)
        var tvArtist = itemView.findViewById<TextView>(R.id.tvArtist)


    }
}