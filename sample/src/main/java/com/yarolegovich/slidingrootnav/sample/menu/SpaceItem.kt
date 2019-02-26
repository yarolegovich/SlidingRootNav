package com.yarolegovich.slidingrootnav.sample.menu

import android.content.Context
import android.view.View
import android.view.ViewGroup

/**
 * Created by yarolegovich on 25.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class SpaceItem(private val spaceDp: Int) : DrawerItem<SpaceItem.ViewHolder>() {

    override var isSelectable: Boolean = false

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        val c = parent.context
        val view = View(c)
        val height = (c.resources.displayMetrics.density * spaceDp).toInt()
        view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height)
        return ViewHolder(view)
    }

    override fun bindViewHolder(holder: ViewHolder) {}

    class ViewHolder(itemView: View) : DrawerAdapter.ViewHolder(itemView)
}
