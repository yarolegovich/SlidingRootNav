package com.yarolegovich.slidingrootnav.sample.menu

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.yarolegovich.slidingrootnav.sample.R

/**
 * Created by yarolegovich on 25.03.2017.
 * Edited by Mehdi on 27.02.2019
 */
class SimpleItem(private val icon: Drawable, private val title: String)
    : DrawerItem<SimpleItem.ViewHolder>() {

    private var selectedItemIconTint: Int = 0
    private var selectedItemTextTint: Int = 0

    private var normalItemIconTint: Int = 0
    private var normalItemTextTint: Int = 0

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_option, parent, false)
        return ViewHolder(v)
    }

    override fun bindViewHolder(holder: ViewHolder) {
        holder.title.text = title
        holder.icon.setImageDrawable(icon)

        holder.title.setTextColor(if (isChecked) selectedItemTextTint else normalItemTextTint)
        holder.icon.setColorFilter(if (isChecked) selectedItemIconTint else normalItemIconTint)
    }

    fun withSelectedIconTint(selectedItemIconTint: Int): SimpleItem {
        this.selectedItemIconTint = selectedItemIconTint
        return this
    }

    fun withSelectedTextTint(selectedItemTextTint: Int): SimpleItem {
        this.selectedItemTextTint = selectedItemTextTint
        return this
    }

    fun withIconTint(normalItemIconTint: Int): SimpleItem {
        this.normalItemIconTint = normalItemIconTint
        return this
    }

    fun withTextTint(normalItemTextTint: Int): SimpleItem {
        this.normalItemTextTint = normalItemTextTint
        return this
    }

    class ViewHolder(itemView: View) : DrawerAdapter.ViewHolder(itemView) {

        val icon: ImageView
        val title: TextView

        init {
            icon = itemView.findViewById<View>(R.id.icon) as ImageView
            title = itemView.findViewById<View>(R.id.title) as TextView
        }
    }
}
