package com.yarolegovich.slidingrootnav.transform

import android.view.View

import com.yarolegovich.slidingrootnav.util.SideNavUtils

/**
 * Created by yarolegovich on 25.03.2017.
 */

class ScaleTransformation(private val endScale: Float) : RootTransformation {

    override fun transform(dragProgress: Float, rootView: View) {
        val scale = SideNavUtils.evaluate(dragProgress, START_SCALE, endScale)
        rootView.scaleX = scale
        rootView.scaleY = scale
    }

    companion object {

        private val START_SCALE = 1f
    }
}
