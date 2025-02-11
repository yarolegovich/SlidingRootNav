package com.slidingrootnav.util;

import android.content.Context;
import androidx.drawerlayout.widget.DrawerLayout;
import com.slidingrootnav.SlidingRootNavLayout;

/**
 * Created by yarolegovich on 26.03.2017.
 */

public class ActionBarToggleAdapter extends DrawerLayout {

    private SlidingRootNavLayout adapter;

    public ActionBarToggleAdapter(Context context) {
        super(context);
    }

    @Override
    public void openDrawer(int gravity) {
        adapter.openMenu();
    }

    @Override
    public void closeDrawer(int gravity) {
        adapter.closeMenu();
    }

    @Override
    public boolean isDrawerVisible(int drawerGravity) {
        return !adapter.isMenuClosed();
    }

    @Override
    public int getDrawerLockMode(int edgeGravity) {
        if (adapter.isMenuLocked() && adapter.isMenuClosed()) {
            return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        } else if (adapter.isMenuLocked() && !adapter.isMenuClosed()) {
            return DrawerLayout.LOCK_MODE_LOCKED_OPEN;
        } else {
            return DrawerLayout.LOCK_MODE_UNLOCKED;
        }
    }

    public void setAdapter(SlidingRootNavLayout adapter) {
        this.adapter = adapter;
    }
}
