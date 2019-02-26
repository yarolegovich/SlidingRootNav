package com.yarolegovich.slidingrootnav.util

/**
 * Created by yarolegovich on 25.03.2017.
 */

object SideNavUtils {

    fun evaluate(fraction: Float, startValue: Float, endValue: Float): Float {
        return startValue + fraction * (endValue - startValue)
    }
}
