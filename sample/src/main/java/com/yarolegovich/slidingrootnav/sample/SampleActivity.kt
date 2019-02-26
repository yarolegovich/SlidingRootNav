package com.yarolegovich.slidingrootnav.sample


import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import com.yarolegovich.slidingrootnav.sample.fragment.CenteredTextFragment
import com.yarolegovich.slidingrootnav.sample.menu.DrawerAdapter
import com.yarolegovich.slidingrootnav.sample.menu.DrawerItem
import com.yarolegovich.slidingrootnav.sample.menu.SimpleItem
import com.yarolegovich.slidingrootnav.sample.menu.SpaceItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.menu_left_drawer.*
import java.util.*


/**
 * Created by yarolegovich on 25.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class SampleActivity : AppCompatActivity(), DrawerAdapter.OnItemSelectedListener {

    private var screenTitles: Array<String>? = null
    private var screenIcons: Array<Drawable?>? = null
    private var slidingRootNav: SlidingRootNav? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = toolbar
        setSupportActionBar(toolbar)

        val slidingRootNavBuilder = SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withMenuLayout(R.layout.menu_left_drawer)

        if (savedInstanceState != null) {
            slidingRootNavBuilder.withSavedState(savedInstanceState)
        }

        slidingRootNav = slidingRootNavBuilder.inject()
        screenIcons = loadScreenIconse()
        screenTitles = loadScreenTitles()

        val testList: List<DrawerItem<DrawerAdapter.ViewHolder>> = Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_CART),
                SpaceItem(48),
                createItemFor(POS_LOGOUT)) as List<DrawerItem<DrawerAdapter.ViewHolder>>

        val adapter = DrawerAdapter(testList)
        adapter.setListener(this)

        val list = list
        list.isNestedScrollingEnabled = false
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        adapter.setSelected(POS_DASHBOARD)
    }

    override fun onItemSelected(position: Int) {
        if (position == POS_LOGOUT) {
            finish()
        }
        slidingRootNav!!.closeMenu()
        val selectedScreen = CenteredTextFragment.createFor(screenTitles!![position])
        showFragment(selectedScreen)
    }

    private fun showFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
    }

    private fun createItemFor(position: Int): DrawerItem<*> {
        return SimpleItem(screenIcons!![position]!!
                , screenTitles!![position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent))
    }

    private fun loadScreenTitles(): Array<String> {
        return resources.getStringArray(R.array.ld_activityScreenTitles)
    }

    private fun loadScreenIconse(): Array<Drawable?> {
        val ta = resources.obtainTypedArray(R.array.ld_activityScreenIcons)
        val icons = arrayOfNulls<Drawable>(ta.length())
        for (i in 0 until ta.length()) {
            val id = ta.getResourceId(i, 0)
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id)
            }
        }
        ta.recycle()
        return icons
    }

    @ColorInt
    private fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(this, res)
    }

    companion object {

        private val POS_DASHBOARD = 0
        private val POS_ACCOUNT = 1
        private val POS_MESSAGES = 2
        private val POS_CART = 3
        private val POS_LOGOUT = 5
    }
}
