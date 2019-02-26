package com.yarolegovich.slidingrootnav.callback

/**
 * Created by yarolegovich on 25.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

interface DragStateListener {

    fun onDragStart()

    fun onDragEnd(isMenuOpened: Boolean)
}
