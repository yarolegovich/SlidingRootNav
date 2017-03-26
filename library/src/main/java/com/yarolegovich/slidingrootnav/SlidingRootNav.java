package com.yarolegovich.slidingrootnav;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public interface SlidingRootNav {

    boolean isMenuHidden();

    boolean isMenuLocked();

    void closeMenu();

    void closeMenu(boolean animated);

    void openMenu();

    void openMenu(boolean animated);

    void setMenuLocked(boolean locked);

    SlidingRootNavLayout getLayout();

}
