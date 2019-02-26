package com.yarolegovich.slidingrootnav.transform

import android.view.View

import com.yarolegovich.slidingrootnav.util.SideNavUtils

/**
 * Created by yarolegovich on 25.03.2017.
 * Edited by Mehdi on 27.02.2019
 */

class YTranslationTransformation(private val endTranslation: Float) : RootTransformation {

    override fun transform(dragProgress: Float, rootView: View) {
        val translation = SideNavUtils.evaluate(dragProgress
                , START_TRANSLATION
                , endTranslation)
        rootView.translationY = translation
    }

    companion object {

        private val START_TRANSLATION = 0f
    }
}
