package com.yarolegovich.slidingrootnav.transform

import android.os.Build
import android.view.View

import com.yarolegovich.slidingrootnav.util.SideNavUtils

/**
 * Created by yarolegovich on 25.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class ElevationTransformation(private val endElevation: Float)
    : RootTransformation {

    override fun transform(dragProgress: Float, rootView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val elevation = SideNavUtils.evaluate(dragProgress
                    , START_ELEVATION
                    , endElevation)
            rootView.elevation = elevation
        }
    }

    companion object {

        private val START_ELEVATION = 0f
    }
}
