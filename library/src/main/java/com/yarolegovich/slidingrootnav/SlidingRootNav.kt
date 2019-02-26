package com.yarolegovich.slidingrootnav

/**
 * Created by yarolegovich on 25.03.2017.
 */

public interface SlidingRootNav {

    val isMenuClosed: Boolean

    val isMenuOpened: Boolean

    var isMenuLocked: Boolean

    val layout: SlidingRootNavLayout

    fun closeMenu()

    fun closeMenu(animated: Boolean)

    fun openMenu()

    fun openMenu(animated: Boolean)

}
