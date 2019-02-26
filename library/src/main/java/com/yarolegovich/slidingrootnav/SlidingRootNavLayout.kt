package com.yarolegovich.slidingrootnav

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout

import com.yarolegovich.slidingrootnav.callback.DragListener
import com.yarolegovich.slidingrootnav.callback.DragStateListener
import com.yarolegovich.slidingrootnav.transform.RootTransformation

import java.util.ArrayList

/**
 * Created by yarolegovich on 24.03.2017.
 */
class SlidingRootNavLayout(context: Context) : FrameLayout(context), SlidingRootNav {

    private val FLING_MIN_VELOCITY: Float

    override var isMenuLocked: Boolean = false
    override var isMenuClosed: Boolean = false
        private set
    private var isContentClickableWhenMenuOpened: Boolean = false

    private var rootTransformation: RootTransformation? = null
    private var mRootView: View? = null

    var dragProgress: Float = 0.toFloat()
        private set
    private var maxDragDistance: Int = 0
    private var dragState: Int = 0

    private val dragHelper: ViewDragHelper
    private var positionHelper: SlideGravity.Helper? = null

    private val dragListeners: MutableList<DragListener>
    private val dragStateListeners: MutableList<DragStateListener>

    override val isMenuOpened: Boolean
        get() = !isMenuClosed

    override val layout: SlidingRootNavLayout
        get() = this

    init {
        dragListeners = ArrayList()
        dragStateListeners = ArrayList()

        FLING_MIN_VELOCITY = ViewConfiguration.get(context).scaledMinimumFlingVelocity.toFloat()

        dragHelper = ViewDragHelper.create(this, ViewDragCallback())

        dragProgress = 0f
        isMenuClosed = true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return !isMenuLocked && dragHelper.shouldInterceptTouchEvent(ev) || shouldBlockClick(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child === mRootView) {
                val rootLeft = positionHelper!!.getRootLeft(dragProgress, maxDragDistance)
                child.layout(rootLeft, top, rootLeft + (right - left), bottom)
            } else {
                child.layout(left, top, right, bottom)
            }
        }
    }

    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun changeMenuVisibility(animated: Boolean, newDragProgress: Float) {
        isMenuClosed = calculateIsMenuHidden()
        if (animated) {
            val left = positionHelper!!.getLeftToSettle(newDragProgress, maxDragDistance)
            if (dragHelper.smoothSlideViewTo(mRootView!!, left, mRootView!!.top)) {
                ViewCompat.postInvalidateOnAnimation(this)
            }
        } else {
            dragProgress = newDragProgress
            rootTransformation!!.transform(dragProgress, mRootView!!)
            requestLayout()
        }
    }

    override fun closeMenu() {
        closeMenu(true)
    }

    override fun closeMenu(animated: Boolean) {
        changeMenuVisibility(animated, 0f)
    }

    override fun openMenu() {
        openMenu(true)
    }

    override fun openMenu(animated: Boolean) {
        changeMenuVisibility(animated, 1f)
    }

    fun setRootView(view: View) {
        mRootView = view
    }

    fun setContentClickableWhenMenuOpened(contentClickableWhenMenuOpened: Boolean) {
        isContentClickableWhenMenuOpened = contentClickableWhenMenuOpened
    }

    fun setRootTransformation(transformation: RootTransformation) {
        rootTransformation = transformation
    }

    fun setMaxDragDistance(maxDragDistance: Int) {
        this.maxDragDistance = maxDragDistance
    }

    fun setGravity(gravity: SlideGravity) {
        positionHelper = gravity.createHelper()
        positionHelper!!.enableEdgeTrackingOn(dragHelper)
    }

    fun addDragListener(listener: DragListener) {
        dragListeners.add(listener)
    }

    fun addDragStateListener(listener: DragStateListener) {
        dragStateListeners.add(listener)
    }

    fun removeDragListener(listener: DragListener) {
        dragListeners.remove(listener)
    }

    fun removeDragStateListener(listener: DragStateListener) {
        dragStateListeners.remove(listener)
    }

    private fun shouldBlockClick(event: MotionEvent): Boolean {
        if (isContentClickableWhenMenuOpened) {
            return false
        }
        if (mRootView != null && isMenuOpened) {
            mRootView!!.getHitRect(tempRect)
            if (tempRect.contains(event.x.toInt(), event.y.toInt())) {
                return true
            }
        }
        return false
    }

    private fun notifyDrag() {
        for (listener in dragListeners) {
            listener.onDrag(dragProgress)
        }
    }

    private fun notifyDragStart() {
        for (listener in dragStateListeners) {
            listener.onDragStart()
        }
    }

    private fun notifyDragEnd(isOpened: Boolean) {
        for (listener in dragStateListeners) {
            listener.onDragEnd(isOpened)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = Bundle()
        savedState.putParcelable(EXTRA_SUPER, super.onSaveInstanceState())
        savedState.putInt(EXTRA_IS_OPENED, if (dragProgress > 0.5) 1 else 0)
        savedState.putBoolean(EXTRA_SHOULD_BLOCK_CLICK, isContentClickableWhenMenuOpened)
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as Bundle
        super.onRestoreInstanceState(savedState.getParcelable(EXTRA_SUPER))
        changeMenuVisibility(false, savedState.getInt(EXTRA_IS_OPENED, 0).toFloat())
        isMenuClosed = calculateIsMenuHidden()
        isContentClickableWhenMenuOpened = savedState.getBoolean(EXTRA_SHOULD_BLOCK_CLICK)
    }

    private fun calculateIsMenuHidden(): Boolean {
        return dragProgress == 0f
    }

    private inner class ViewDragCallback : ViewDragHelper.Callback() {

        private var edgeTouched: Boolean = false

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            if (isMenuLocked) {
                return false
            }
            val isOnEdge = edgeTouched
            edgeTouched = false
            if (isMenuClosed) {
                return child === mRootView && isOnEdge
            } else {
                if (child !== mRootView) {
                    dragHelper.captureChildView(mRootView!!, pointerId)
                    return false
                }
                return true
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            dragProgress = positionHelper!!.getDragProgress(left, maxDragDistance)
            rootTransformation!!.transform(dragProgress, mRootView!!)
            notifyDrag()
            invalidate()
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val left = if (Math.abs(xvel) < FLING_MIN_VELOCITY)
                positionHelper!!.getLeftToSettle(dragProgress, maxDragDistance)
            else
                positionHelper!!.getLeftAfterFling(xvel, maxDragDistance)
            dragHelper.settleCapturedViewAt(left, mRootView!!.top)
            invalidate()
        }

        override fun onViewDragStateChanged(state: Int) {
            if (dragState == ViewDragHelper.STATE_IDLE && state != ViewDragHelper.STATE_IDLE) {
                notifyDragStart()
            } else if (dragState != ViewDragHelper.STATE_IDLE && state == ViewDragHelper.STATE_IDLE) {
                isMenuClosed = calculateIsMenuHidden()
                notifyDragEnd(isMenuOpened)
            }
            dragState = state
        }

        override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
            edgeTouched = true
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return if (child === mRootView) maxDragDistance else 0
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return positionHelper!!.clampViewPosition(left, maxDragDistance)
        }
    }

    companion object {

        private val EXTRA_IS_OPENED = "extra_is_opened"
        private val EXTRA_SUPER = "extra_super"
        private val EXTRA_SHOULD_BLOCK_CLICK = "extra_should_block_click"

        private val tempRect = Rect()
    }
}