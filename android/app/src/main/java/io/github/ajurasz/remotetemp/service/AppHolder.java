package io.github.ajurasz.remotetemp.service;

import android.annotation.SuppressLint;

public class AppHolder {

    @SuppressLint("StaticFieldLeak")
    private static final android.app.Application APP;

    public static android.content.Context getContext() {
        return APP.getApplicationContext();
    }

    static {
        try {
            Class<?> c = Class.forName("android.app.ActivityThread");
            APP = (android.app.Application) c.getDeclaredMethod("currentApplication").invoke(null);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }
}
