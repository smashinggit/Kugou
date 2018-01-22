package com.cs.kugou.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * author : ChenSen
 * data : 2018/1/22
 * desc: 播放列表
 */

@Table(database = KugouDB.class)
public class PlayList extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String name;

    @Column
    public String artist;

    @Column
    public String url;

    @Column
    public boolean like; //是否在喜欢列表

    @Column
    public boolean playing;//是否正在播放


}
