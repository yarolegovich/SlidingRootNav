package com.yarolegovich.slidingrootnav.transform

import android.view.View

/**
 * Created by yarolegovich on 25.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class CompositeTransformation(private val transformations: List<RootTransformation>)
    : RootTransformation {

    override fun transform(dragProgress: Float, rootView: View) {
        for (t in transformations) {
            t.transform(dragProgress, rootView)
        }
    }
}
