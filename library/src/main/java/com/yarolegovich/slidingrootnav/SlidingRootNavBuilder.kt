package com.yarolegovich.slidingrootnav

import android.app.Activity
import android.os.Bundle
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.yarolegovich.slidingrootnav.callback.DragListener
import com.yarolegovich.slidingrootnav.callback.DragStateListener
import com.yarolegovich.slidingrootnav.transform.CompositeTransformation
import com.yarolegovich.slidingrootnav.transform.ElevationTransformation
import com.yarolegovich.slidingrootnav.transform.RootTransformation
import com.yarolegovich.slidingrootnav.transform.ScaleTransformation
import com.yarolegovich.slidingrootnav.transform.YTranslationTransformation
import com.yarolegovich.slidingrootnav.util.ActionBarToggleAdapter
import com.yarolegovich.slidingrootnav.util.DrawerListenerAdapter
import com.yarolegovich.slidingrootnav.util.HiddenMenuClickConsumer

import java.util.ArrayList
import java.util.Arrays

/**
 * Created by yarolegovich on 24.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class SlidingRootNavBuilder(private val activity: Activity) {

    private var contentView: ViewGroup? = null

    private var menuView: View? = null
    private var menuLayoutRes: Int = 0

    private val transformations: MutableList<RootTransformation>

    private val dragListeners: MutableList<DragListener>

    private val dragStateListeners: MutableList<DragStateListener>

    private var dragDistance: Int = 0

    private var toolbar: Toolbar? = null

    private var gravity: SlideGravity? = null

    private var isMenuOpened: Boolean = false

    private var isMenuLocked: Boolean = false

    private var isContentClickableWhenMenuOpened: Boolean = false

    private var savedState: Bundle? = null

    init {
        this.transformations = ArrayList()
        this.dragListeners = ArrayList()
        this.dragStateListeners = ArrayList()
        this.gravity = SlideGravity.LEFT
        this.dragDistance = dpToPx(DEFAULT_DRAG_DIST_DP)
        this.isContentClickableWhenMenuOpened = true
    }

    fun withMenuView(view: View): SlidingRootNavBuilder {
        menuView = view
        return this
    }

    fun withMenuLayout(@LayoutRes layout: Int): SlidingRootNavBuilder {
        menuLayoutRes = layout
        return this
    }

    fun withToolbarMenuToggle(tb: Toolbar): SlidingRootNavBuilder {
        toolbar = tb
        return this
    }

    fun withGravity(g: SlideGravity): SlidingRootNavBuilder {
        gravity = g
        return this
    }

    fun withContentView(cv: ViewGroup): SlidingRootNavBuilder {
        contentView = cv
        return this
    }

    fun withMenuLocked(locked: Boolean): SlidingRootNavBuilder {
        isMenuLocked = locked
        return this
    }

    fun withSavedState(state: Bundle): SlidingRootNavBuilder {
        savedState = state
        return this
    }

    fun withMenuOpened(opened: Boolean): SlidingRootNavBuilder {
        isMenuOpened = opened
        return this
    }

    fun withContentClickableWhenMenuOpened(clickable: Boolean): SlidingRootNavBuilder {
        isContentClickableWhenMenuOpened = clickable
        return this
    }

    fun withDragDistance(dp: Int): SlidingRootNavBuilder {
        return withDragDistancePx(dpToPx(dp))
    }

    fun withDragDistancePx(px: Int): SlidingRootNavBuilder {
        dragDistance = px
        return this
    }

    fun withRootViewScale(@FloatRange(from = 0.01) scale: Float): SlidingRootNavBuilder {
        transformations.add(ScaleTransformation(scale))
        return this
    }

    fun withRootViewElevation(@IntRange(from = 0) elevation: Int): SlidingRootNavBuilder {
        return withRootViewElevationPx(dpToPx(elevation))
    }

    fun withRootViewElevationPx(@IntRange(from = 0) elevation: Int): SlidingRootNavBuilder {
        transformations.add(ElevationTransformation(elevation.toFloat()))
        return this
    }

    fun withRootViewYTranslation(translation: Int): SlidingRootNavBuilder {
        return withRootViewYTranslationPx(dpToPx(translation))
    }

    fun withRootViewYTranslationPx(translation: Int): SlidingRootNavBuilder {
        transformations.add(YTranslationTransformation(translation.toFloat()))
        return this
    }

    fun addRootTransformation(transformation: RootTransformation): SlidingRootNavBuilder {
        transformations.add(transformation)
        return this
    }

    fun addDragListener(dragListener: DragListener): SlidingRootNavBuilder {
        dragListeners.add(dragListener)
        return this
    }

    fun addDragStateListener(dragStateListener: DragStateListener): SlidingRootNavBuilder {
        dragStateListeners.add(dragStateListener)
        return this
    }

    fun inject(): SlidingRootNav {
        val contentView = getContentView()

        val oldRoot = contentView.getChildAt(0)
        contentView.removeAllViews()

        val newRoot = createAndInitNewRoot(oldRoot)

        val menu = getMenuViewFor(newRoot)

        initToolbarMenuVisibilityToggle(newRoot, menu)

        val clickConsumer = HiddenMenuClickConsumer(activity)
        clickConsumer.setMenuHost(newRoot)

        newRoot.addView(menu)
        newRoot.addView(clickConsumer)
        newRoot.addView(oldRoot)

        contentView.addView(newRoot)

        if (savedState == null) {
            if (isMenuOpened) {
                newRoot.openMenu(false)
            }
        }

        newRoot.isMenuLocked = isMenuLocked

        return newRoot
    }

    private fun createAndInitNewRoot(oldRoot: View): SlidingRootNavLayout {
        val newRoot = SlidingRootNavLayout(activity)
        newRoot.id = R.id.srn_root_layout
        newRoot.setRootTransformation(createCompositeTransformation())
        newRoot.setMaxDragDistance(dragDistance)
        newRoot.setGravity(gravity!!)
        newRoot.rootView = oldRoot
        newRoot.setContentClickableWhenMenuOpened(isContentClickableWhenMenuOpened)
        for (l in dragListeners) {
            newRoot.addDragListener(l)
        }
        for (l in dragStateListeners) {
            newRoot.addDragStateListener(l)
        }
        return newRoot
    }

    private fun getContentView(): ViewGroup {
        if (contentView == null) {
            contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
        }
        if (contentView!!.childCount != 1) {
            throw IllegalStateException(activity.getString(R.string.srn_ex_bad_content_view))
        }
        return contentView as ViewGroup
    }

    private fun getMenuViewFor(parent: SlidingRootNavLayout): View? {
        if (menuView == null) {
            if (menuLayoutRes == 0) {
                throw IllegalStateException(activity.getString(R.string.srn_ex_no_menu_view))
            }
            menuView = LayoutInflater.from(activity).inflate(menuLayoutRes, parent, false)
        }
        return menuView
    }

    private fun createCompositeTransformation(): RootTransformation {
        return if (transformations.isEmpty()) {
            CompositeTransformation(Arrays.asList(
                    ScaleTransformation(DEFAULT_END_SCALE),
                    ElevationTransformation(dpToPx(DEFAULT_END_ELEVATION_DP).toFloat())))
        } else {
            CompositeTransformation(transformations)
        }
    }

    protected fun initToolbarMenuVisibilityToggle(sideNav: SlidingRootNavLayout, drawer: View?) {
        if (toolbar != null) {
            val dlAdapter = ActionBarToggleAdapter(activity)
            dlAdapter.setAdaptee(sideNav)
            val toggle = ActionBarDrawerToggle(activity, dlAdapter, toolbar,
                    R.string.srn_drawer_open,
                    R.string.srn_drawer_close)
            toggle.syncState()
            val listenerAdapter = DrawerListenerAdapter(toggle, drawer!!)
            sideNav.addDragListener(listenerAdapter)
            sideNav.addDragStateListener(listenerAdapter)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return Math.round(activity.resources.displayMetrics.density * dp)
    }

    companion object {

        private val DEFAULT_END_SCALE = 0.65f
        private val DEFAULT_END_ELEVATION_DP = 8
        private val DEFAULT_DRAG_DIST_DP = 180
    }

}
