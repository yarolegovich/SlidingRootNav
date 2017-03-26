package com.yarolegovich.slidingrootnav.util;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.yarolegovich.slidingrootnav.callback.DragListener;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

/**
 * Created by yarolegovich on 26.03.2017.
 */

public class DrawerListenerAdapter implements DragListener, DragStateListener {

    private DrawerLayout.DrawerListener adaptee;
    private View drawer;

    public DrawerListenerAdapter(DrawerLayout.DrawerListener adaptee, View drawer) {
        this.adaptee = adaptee;
        this.drawer = drawer;
    }

    @Override
    public void onDrag(float progress) {
        adaptee.onDrawerSlide(drawer, progress);
    }

    @Override
    public void onDragStart() {
        adaptee.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING);
    }

    @Override
    public void onDragEnd(boolean isMenuOpened) {
        if (isMenuOpened) {
            adaptee.onDrawerOpened(drawer);
        } else {
            adaptee.onDrawerClosed(drawer);
        }
        adaptee.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
    }
}
