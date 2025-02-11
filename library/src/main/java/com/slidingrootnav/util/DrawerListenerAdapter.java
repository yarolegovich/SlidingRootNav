package com.slidingrootnav.util;

import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;
import com.slidingrootnav.callback.DragListener;
import com.slidingrootnav.callback.DragStateListener;

/**
 * Created by yarolegovich on 26.03.2017.
 */

public class DrawerListenerAdapter implements DragListener, DragStateListener {

    private final DrawerLayout.DrawerListener adapter;
    private final View drawer;

    public DrawerListenerAdapter(DrawerLayout.DrawerListener adapter, View drawer) {
        this.adapter = adapter;
        this.drawer = drawer;
    }

    @Override
    public void onDrag(float progress) {
        adapter.onDrawerSlide(drawer, progress);
    }

    @Override
    public void onDragStart() {
        adapter.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING);
    }

    @Override
    public void onDragEnd(boolean isMenuOpened) {
        if (isMenuOpened) {
            adapter.onDrawerOpened(drawer);
        } else {
            adapter.onDrawerClosed(drawer);
        }
        adapter.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
    }
}
