package com.cs.kugou.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.ListFragment

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainPagerAdapter(var context: Context, var fragmentList: ArrayList<Fragment>, manager: FragmentManager) : FragmentPagerAdapter(manager) {
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return super.getPageTitle(position)
    }


}