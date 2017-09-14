package com.yarolegovich.slidingrootnav;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.yarolegovich.slidingrootnav.callback.DragListener;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;
import com.yarolegovich.slidingrootnav.transform.RootTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yarolegovich on 24.03.2017.
 */
public class SlidingRootNavLayout extends FrameLayout implements SlidingRootNav {

    private static final String EXTRA_IS_OPENED = "extra_is_opened";
    private static final String EXTRA_SUPER = "extra_super";
    private static final String EXTRA_SHOULD_BLOCK_CLICK = "extra_should_block_click";

    private static final Rect tempRect = new Rect();

    private final float FLING_MIN_VELOCITY;

    private boolean isMenuLocked;
    private boolean isMenuHidden;
    private boolean isContentClickableWhenMenuOpened;

    private RootTransformation rootTransformation;
    private View rootView;

    private float dragProgress;
    private int maxDragDistance;
    private int dragState;

    private ViewDragHelper dragHelper;
    private SlideGravity.Helper positionHelper;

    private List<DragListener> dragListeners;
    private List<DragStateListener> dragStateListeners;

    public SlidingRootNavLayout(Context context) {
        super(context);
        dragListeners = new ArrayList<>();
        dragStateListeners = new ArrayList<>();

        FLING_MIN_VELOCITY = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        dragHelper = ViewDragHelper.create(this, new ViewDragCallback());

        dragProgress = 0f;
        isMenuHidden = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return (!isMenuLocked
            && dragHelper.shouldInterceptTouchEvent(ev))
            || shouldBlockClick(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == rootView) {
                int rootLeft = positionHelper.getRootLeft(dragProgress, maxDragDistance);
                child.layout(rootLeft, top, rootLeft + (right - left), bottom);
            } else {
                child.layout(left, top, right, bottom);
            }
        }
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void changeMenuVisibility(boolean animated, float newDragProgress) {
        isMenuHidden = calculateIsMenuHidden();
        if (animated) {
            int left = positionHelper.getLeftToSettle(newDragProgress, maxDragDistance);
            if (dragHelper.smoothSlideViewTo(rootView, left, rootView.getTop())) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            dragProgress = newDragProgress;
            rootTransformation.transform(dragProgress, rootView);
            requestLayout();
        }
    }

    @Override
    public boolean isMenuClosed() {
        return isMenuHidden;
    }

    @Override public boolean isMenuOpened() {
        return !isMenuHidden;
    }

    @Override
    public SlidingRootNavLayout getLayout() {
        return this;
    }

    @Override
    public boolean isMenuLocked() {
        return isMenuLocked;
    }

    @Override
    public void closeMenu() {
        closeMenu(true);
    }

    @Override
    public void closeMenu(boolean animated) {
        changeMenuVisibility(animated, 0f);
    }

    @Override
    public void openMenu() {
        openMenu(true);
    }

    @Override
    public void openMenu(boolean animated) {
        changeMenuVisibility(animated, 1f);
    }

    @Override
    public void setMenuLocked(boolean locked) {
        isMenuLocked = locked;
    }

    public void setRootView(View view) {
        rootView = view;
    }

    public void setContentClickableWhenMenuOpened(boolean contentClickableWhenMenuOpened) {
        isContentClickableWhenMenuOpened = contentClickableWhenMenuOpened;
    }

    public void setRootTransformation(RootTransformation transformation) {
        rootTransformation = transformation;
    }

    public void setMaxDragDistance(int maxDragDistance) {
        this.maxDragDistance = maxDragDistance;
    }

    public void setGravity(SlideGravity gravity) {
        positionHelper = gravity.createHelper();
        positionHelper.enableEdgeTrackingOn(dragHelper);
    }

    public void addDragListener(DragListener listener) {
        dragListeners.add(listener);
    }

    public void addDragStateListener(DragStateListener listener) {
        dragStateListeners.add(listener);
    }

    public void removeDragListener(DragListener listener) {
        dragListeners.remove(listener);
    }

    public void removeDragStateListener(DragStateListener listener) {
        dragStateListeners.remove(listener);
    }

    public float getDragProgress() {
        return dragProgress;
    }

    private boolean shouldBlockClick(MotionEvent event) {
        if (isContentClickableWhenMenuOpened) {
            return false;
        }
        if (rootView != null && isMenuOpened()) {
            rootView.getHitRect(tempRect);
            if (tempRect.contains((int) event.getX(), (int) event.getY())) {
                return true;
            }
        }
        return false;
    }

    private void notifyDrag() {
        for (DragListener listener : dragListeners) {
            listener.onDrag(dragProgress);
        }
    }

    private void notifyDragStart() {
        for (DragStateListener listener : dragStateListeners) {
            listener.onDragStart();
        }
    }

    private void notifyDragEnd(boolean isOpened) {
        for (DragStateListener listener : dragStateListeners) {
            listener.onDragEnd(isOpened);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle savedState = new Bundle();
        savedState.putParcelable(EXTRA_SUPER, super.onSaveInstanceState());
        savedState.putInt(EXTRA_IS_OPENED, dragProgress > 0.5 ? 1 : 0);
        savedState.putBoolean(EXTRA_SHOULD_BLOCK_CLICK, isContentClickableWhenMenuOpened);
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle savedState = (Bundle) state;
        super.onRestoreInstanceState(savedState.getParcelable(EXTRA_SUPER));
        changeMenuVisibility(false, savedState.getInt(EXTRA_IS_OPENED, 0));
        isMenuHidden = calculateIsMenuHidden();
        isContentClickableWhenMenuOpened = savedState.getBoolean(EXTRA_SHOULD_BLOCK_CLICK);
    }

    private boolean calculateIsMenuHidden() {
        return dragProgress == 0f;
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        private boolean edgeTouched;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (isMenuLocked) {
                return false;
            }
            boolean isOnEdge = edgeTouched;
            edgeTouched = false;
            if (isMenuClosed()) {
                return child == rootView && isOnEdge;
            } else {
                if (child != rootView) {
                    dragHelper.captureChildView(rootView, pointerId);
                    return false;
                }
                return true;
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            dragProgress = positionHelper.getDragProgress(left, maxDragDistance);
            rootTransformation.transform(dragProgress, rootView);
            notifyDrag();
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left = Math.abs(xvel) < FLING_MIN_VELOCITY ?
                    positionHelper.getLeftToSettle(dragProgress, maxDragDistance) :
                    positionHelper.getLeftAfterFling(xvel, maxDragDistance);
            dragHelper.settleCapturedViewAt(left, rootView.getTop());
            invalidate();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (dragState == ViewDragHelper.STATE_IDLE && state != ViewDragHelper.STATE_IDLE) {
                notifyDragStart();
            } else if (dragState != ViewDragHelper.STATE_IDLE && state == ViewDragHelper.STATE_IDLE) {
                isMenuHidden = calculateIsMenuHidden();
                notifyDragEnd(isMenuOpened());
            }
            dragState = state;
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            edgeTouched = true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return child == rootView ? maxDragDistance : 0;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return positionHelper.clampViewPosition(left, maxDragDistance);
        }
    }
}