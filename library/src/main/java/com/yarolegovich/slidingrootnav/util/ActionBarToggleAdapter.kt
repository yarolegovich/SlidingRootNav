package com.yarolegovich.slidingrootnav.util

import android.content.Context
import androidx.drawerlayout.widget.DrawerLayout

import com.yarolegovich.slidingrootnav.SlidingRootNavLayout

/**
 * Created by yarolegovich on 26.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class ActionBarToggleAdapter(context: Context) : DrawerLayout(context) {

    private var adaptee: SlidingRootNavLayout? = null

    override fun openDrawer(gravity: Int) {
        adaptee!!.openMenu()
    }

    override fun closeDrawer(gravity: Int) {
        adaptee!!.closeMenu()
    }

    override fun isDrawerVisible(drawerGravity: Int): Boolean {
        return !adaptee!!.isMenuClosed
    }

    override fun getDrawerLockMode(edgeGravity: Int): Int {
        return if (adaptee!!.isMenuLocked && adaptee!!.isMenuClosed) {
            DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        } else if (adaptee!!.isMenuLocked && !adaptee!!.isMenuClosed) {
            DrawerLayout.LOCK_MODE_LOCKED_OPEN
        } else {
            DrawerLayout.LOCK_MODE_UNLOCKED
        }
    }

    fun setAdaptee(adaptee: SlidingRootNavLayout) {
        this.adaptee = adaptee
    }
}
