package com.yarolegovich.slidingrootnav.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Original source: https://stackoverflow.com/a/43886113
 */
public class BadgeDrawerToggle extends ActionBarDrawerToggle {

    private BadgeDrawerArrowDrawable badgeDrawable;

    public BadgeDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                             int openDrawerContentDescRes,
                             int closeDrawerContentDescRes) {
        super(activity, drawerLayout, openDrawerContentDescRes,
                closeDrawerContentDescRes);
        init(activity);
    }

    public BadgeDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                             Toolbar toolbar, int openDrawerContentDescRes,
                             int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes,
                closeDrawerContentDescRes);
        init(activity);
    }

    private void init(Activity activity) {
        Context c = getThemedContext();
        if (c == null) {
            c = activity;
        }
        badgeDrawable = new BadgeDrawerArrowDrawable(c);
        setDrawerArrowDrawable(badgeDrawable);
    }

    public void setBadgeEnabled(boolean enabled) {
        badgeDrawable.setEnabled(enabled);
    }

    public boolean isBadgeEnabled() {
        return badgeDrawable.isEnabled();
    }

    public void setBadgeText(String text) {
        badgeDrawable.setText(text);
    }

    public String getBadgeText() {
        return badgeDrawable.getText();
    }

    public void setBadgeColor(int color) {
        badgeDrawable.setBackgroundColor(color);
    }

    public int getBadgeColor() {
        return badgeDrawable.getBackgroundColor();
    }

    public void setBadgeTextColor(int color) {
        badgeDrawable.setTextColor(color);
    }

    public int getBadgeTextColor() {
        return badgeDrawable.getTextColor();
    }

    private Context getThemedContext() {
        // Don't freak about the reflection. ActionBarDrawerToggle
        // itself is already using reflection internally.
        try {
            Field mActivityImplField = ActionBarDrawerToggle.class
                    .getDeclaredField("mActivityImpl");
            mActivityImplField.setAccessible(true);
            Object mActivityImpl = mActivityImplField.get(this);
            Method getActionBarThemedContextMethod = mActivityImpl.getClass()
                    .getDeclaredMethod("getActionBarThemedContext");
            return (Context) getActionBarThemedContextMethod.invoke(mActivityImpl);
        }
        catch (Exception e) {
            return null;
        }
    }
}