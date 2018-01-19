package com.cs.kugou.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cs.kugou.R

/**
 *
 * author : ChenSen
 * data : 2018/1/19
 * desc:
 */
class ListenerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_listener, null)
        return view
    }
}