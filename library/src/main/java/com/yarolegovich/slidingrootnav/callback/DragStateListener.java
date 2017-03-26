package com.yarolegovich.slidingrootnav.callback;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public interface DragStateListener {

    void onDragStart();

    void onDragEnd(boolean isMenuOpened);
}
