package com.cs.kugou.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cs.kugou.R
import kotlinx.android.synthetic.main.item_listen_classify.view.*

/**
 * Created by chensen on 2018/1/20.
 */
class MusicClassifyAdapter(var context: Context) : RecyclerView.Adapter<MusicClassifyAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(context).inflate(R.layout.item_listen_classify, null))
    }

    override fun getItemCount() = 6

    override fun onBindViewHolder(holder: MyHolder?, position: Int) {
        holder?.itemView?.let {
            when (position) {
                0 -> {
                    it.tvName.text = "乐库"
                    it.ivIcon.setImageResource(R.drawable.select_music_store)
                }
                1 -> {
                    it.tvName.text = "歌单"
                    it.ivIcon.setImageResource(R.drawable.select_music_list)
                }
                2 -> {
                    it.tvName.text = "电台·酷群"
                    it.ivIcon.setImageResource(R.drawable.select_mucis_radio)
                }
                3 -> {
                    it.tvName.text = "猜你喜欢"
                    it.ivIcon.setImageResource(R.drawable.select_mucis_guss)
                }
                4 -> {
                    it.tvName.text = "每日推荐"
                    it.ivIcon.setImageResource(R.drawable.select_mucis_recommend)
                }
                5 -> {
                    it.tvName.text = "音乐圈"
                    it.ivIcon.setImageResource(R.drawable.select_mucis_circle)
                }
            }
            it.setOnClickListener {
               Toast.makeText(context,"dianj",0).show()
            }
        }
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivIcon = itemView.ivIcon
        var tvName = itemView.tvName
    }
}