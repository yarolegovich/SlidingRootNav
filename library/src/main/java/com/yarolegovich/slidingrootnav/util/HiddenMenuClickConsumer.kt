package com.yarolegovich.slidingrootnav.util

import android.content.Context
import android.view.MotionEvent
import android.view.View

import com.yarolegovich.slidingrootnav.SlidingRootNavLayout

/**
 * Created by yarolegovich on 26.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class HiddenMenuClickConsumer(context: Context) : View(context) {

    private var menuHost: SlidingRootNavLayout? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return menuHost!!.isMenuClosed
    }

    fun setMenuHost(layout: SlidingRootNavLayout) {
        this.menuHost = layout
    }
}
