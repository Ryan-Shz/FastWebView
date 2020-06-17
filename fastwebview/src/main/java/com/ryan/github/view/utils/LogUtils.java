package com.ryan.github.view.utils;

import android.util.Log;

/**
 * Created by Ryan
 * at 2019/10/8
 */
public class LogUtils {

    private static final String TAG = "FastWebView";
    public static boolean DEBUG = false;

    public static void d(String message) {
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            Log.e(TAG, message);
        }
    }
}
