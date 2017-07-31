package com.liangjing.filedownload.utils;

import android.util.Log;

import java.util.Locale;

/**
 * Created by liangjing on 2017/7/31.
 * 功能：多线程下载文件--log日志打印工具类
 */

public class LoggerUtil {

    public static final boolean DEBUG = true;

    public static void debug(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void debug(String tag, String message, Object... args) {
        if (DEBUG) {
            Log.d(tag, String.format(Locale.getDefault(), message, args));
        }
    }

    public static void error(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void info(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void warn(String tag, String message) {
        if (DEBUG) {
            Log.w(tag, message);
        }
    }

}
