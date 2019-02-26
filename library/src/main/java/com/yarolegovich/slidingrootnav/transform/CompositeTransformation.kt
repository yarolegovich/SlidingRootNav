package com.yarolegovich.slidingrootnav.transform

import android.view.View

/**
 * Created by yarolegovich on 25.03.2017.
 */

class CompositeTransformation(private val transformations: List<RootTransformation>) : RootTransformation {

    override fun transform(dragProgress: Float, rootView: View) {
        for (t in transformations) {
            t.transform(dragProgress, rootView)
        }
    }
}
