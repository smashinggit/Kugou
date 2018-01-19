package com.cs.kugou.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.ListFragment
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.ImageSpan
import com.cs.kugou.R

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class MainPagerAdapter(var context: Context, manager: FragmentManager) : FragmentPagerAdapter(manager) {
    override fun getItem(position: Int): Fragment {
        return ListFragment()
    }

    override fun getCount(): Int {
        return 3
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getPageTitle(position: Int): CharSequence? {
        var drawable1 = context.getDrawable(R.drawable.select_ic_menu)
        drawable1.setBounds(0, 0, drawable1.intrinsicWidth, drawable1.intrinsicHeight)

        val imageSpan = ImageSpan(drawable1, ImageSpan.ALIGN_BOTTOM)
        val spannableString = SpannableString("")
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }
}