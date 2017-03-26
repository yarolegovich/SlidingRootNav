package com.yarolegovich.slidingrootnav.transform;

import android.os.Build;
import android.view.View;

import com.yarolegovich.slidingrootnav.util.SideNavUtils;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class ElevationTransformation implements RootTransformation {

    private static final float START_ELEVATION = 0f;

    private final float endElevation;

    public ElevationTransformation(float endElevation) {
        this.endElevation = endElevation;
    }

    @Override
    public void transform(float dragProgress, View rootView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float elevation = SideNavUtils.evaluate(dragProgress, START_ELEVATION, endElevation);
            rootView.setElevation(elevation);
        }
    }
}
