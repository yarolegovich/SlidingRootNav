package com.yarolegovich.slidingrootnav.sample.menu

import android.view.ViewGroup

/**
 * Created by yarolegovich on 25.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

abstract class DrawerItem<T : DrawerAdapter.ViewHolder> {

    var isChecked: Boolean = false
    open var isSelectable: Boolean = true

    fun setChecked(isChecked: Boolean): DrawerItem<T> {
        this.isChecked = isChecked
        return this
    }

    abstract fun createViewHolder(parent: ViewGroup): T

    abstract fun bindViewHolder(holder: T)
}
