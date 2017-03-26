package com.yarolegovich.slidingrootnav;

import android.support.v4.widget.ViewDragHelper;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public enum SlideGravity {

    LEFT {
        @Override
        Helper createHelper() {
            return new LeftHelper();
        }
    },
    RIGHT {
        @Override
        Helper createHelper() {
            return new RightHelper();
        }
    };

    abstract Helper createHelper();

    interface Helper {

        int getLeftAfterFling(float flingVelocity, int maxDrag);

        int getLeftToSettle(float dragProgress, int maxDrag);

        int getRootLeft(float dragProgress, int maxDrag);

        float getDragProgress(int viewLeft, int maxDrag);

        int clampViewPosition(int left, int maxDrag);

        void enableEdgeTrackingOn(ViewDragHelper dragHelper);
    }

    static class LeftHelper implements Helper {

        @Override
        public int getLeftAfterFling(float flingVelocity, int maxDrag) {
            return flingVelocity > 0 ? maxDrag : 0;
        }

        @Override
        public int getLeftToSettle(float dragProgress, int maxDrag) {
            return dragProgress > 0.5f ? maxDrag : 0;
        }

        @Override
        public int getRootLeft(float dragProgress, int maxDrag) {
            return (int) (dragProgress * maxDrag);
        }

        @Override
        public float getDragProgress(int viewLeft, int maxDrag) {
            return ((float) viewLeft) / maxDrag;
        }

        @Override
        public int clampViewPosition(int left, int maxDrag) {
            return Math.max(0, Math.min(left, maxDrag));
        }

        @Override
        public void enableEdgeTrackingOn(ViewDragHelper dragHelper) {
            dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        }
    }

    static class RightHelper implements Helper {

        @Override
        public int getLeftAfterFling(float flingVelocity, int maxDrag) {
            return flingVelocity > 0 ? 0 : -maxDrag;
        }

        @Override
        public int getLeftToSettle(float dragProgress, int maxDrag) {
            return dragProgress > 0.5f ? -maxDrag : 0;
        }

        @Override
        public int getRootLeft(float dragProgress, int maxDrag) {
            return (int) -(dragProgress * maxDrag);
        }

        @Override
        public float getDragProgress(int viewLeft, int maxDrag) {
            return ((float) Math.abs(viewLeft)) / maxDrag;
        }

        @Override
        public int clampViewPosition(int left, int maxDrag) {
            return Math.max(-maxDrag, Math.min(left, 0));
        }

        @Override
        public void enableEdgeTrackingOn(ViewDragHelper dragHelper) {
            dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT);
        }
    }

}
