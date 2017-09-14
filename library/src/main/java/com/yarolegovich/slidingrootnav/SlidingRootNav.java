package com.yarolegovich.slidingrootnav;

import com.yarolegovich.slidingrootnav.util.BadgeDrawerToggle;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public interface SlidingRootNav {

    boolean isMenuClosed();

    boolean isMenuOpened();

    boolean isMenuLocked();

    void closeMenu();

    void closeMenu(boolean animated);

    void openMenu();

    void openMenu(boolean animated);

    void setMenuLocked(boolean locked);

    SlidingRootNavLayout getLayout();

    BadgeDrawerToggle getBadgeDrawerToggle();
}
