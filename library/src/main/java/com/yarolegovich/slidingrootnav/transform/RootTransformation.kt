package com.yarolegovich.slidingrootnav.transform

import android.view.View

/**
 * Created by yarolegovich on 25.03.2017.
 */

interface RootTransformation {

    fun transform(dragProgress: Float, rootView: View)
}
