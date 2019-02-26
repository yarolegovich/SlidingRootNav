package com.yarolegovich.slidingrootnav.sample.menu

import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup


/**
 * Created by yarolegovich on 25.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class DrawerAdapter(private var items: List<DrawerItem<DrawerAdapter.ViewHolder>>)
    : RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {

    private var viewTypes: HashMap<Class<out DrawerItem<*>>, Int> = HashMap()
    private var holderFactories: SparseArray<DrawerItem<*>> = SparseArray()

    private var listener: OnItemSelectedListener? = null

    init {
        processViewTypes()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = holderFactories.get(viewType).createViewHolder(parent)
        holder.adapter = this
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].bindViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypes[items[position].javaClass]!!
    }

    private fun processViewTypes() {
        var type = 0
        for (item in items) {
            if (!viewTypes.containsKey(item.javaClass)) {
                viewTypes[item.javaClass] = type
                holderFactories.put(type, item)
                type++
            }
        }
    }

    fun setSelected(position: Int) {
        val newChecked = items[position]
        if (!newChecked.isSelectable) {
            return
        }

        for (i in items.indices) {
            val item = items[i]
            if (item.isChecked) {
                item.isChecked = false
                notifyItemChanged(i)
                break
            }
        }

        newChecked.isChecked = true
        notifyItemChanged(position)

        if (listener != null) {
            listener!!.onItemSelected(position)
        }
    }

    fun setListener(listener: OnItemSelectedListener) {
        this.listener = listener
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var adapter: DrawerAdapter? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            adapter!!.setSelected(adapterPosition)
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }
}
