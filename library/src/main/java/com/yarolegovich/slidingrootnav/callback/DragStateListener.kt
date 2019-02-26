package com.yarolegovich.slidingrootnav.callback

/**
 * Created by yarolegovich on 25.03.2017.
 */

interface DragStateListener {

    fun onDragStart()

    fun onDragEnd(isMenuOpened: Boolean)
}
