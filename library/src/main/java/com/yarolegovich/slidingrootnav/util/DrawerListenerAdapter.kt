package com.yarolegovich.slidingrootnav.util

import androidx.drawerlayout.widget.DrawerLayout
import android.view.View

import com.yarolegovich.slidingrootnav.callback.DragListener
import com.yarolegovich.slidingrootnav.callback.DragStateListener

/**
 * Created by yarolegovich on 26.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class DrawerListenerAdapter(private val adaptee: DrawerLayout.DrawerListener, private val drawer: View) : DragListener, DragStateListener {

    override fun onDrag(progress: Float) {
        adaptee.onDrawerSlide(drawer, progress)
    }

    override fun onDragStart() {
        adaptee.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING)
    }

    override fun onDragEnd(isMenuOpened: Boolean) {
        if (isMenuOpened) {
            adaptee.onDrawerOpened(drawer)
        } else {
            adaptee.onDrawerClosed(drawer)
        }
        adaptee.onDrawerStateChanged(DrawerLayout.STATE_IDLE)
    }
}
