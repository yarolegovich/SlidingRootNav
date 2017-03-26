package com.yarolegovich.slidingrootnav.transform;

import android.view.View;

import java.util.List;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class CompositeTransformation implements RootTransformation {

    private List<RootTransformation> transformations;

    public CompositeTransformation(List<RootTransformation> transformations) {
        this.transformations = transformations;
    }

    @Override
    public void transform(float dragProgress, View rootView) {
        for (RootTransformation t : transformations) {
            t.transform(dragProgress, rootView);
        }
    }
}
