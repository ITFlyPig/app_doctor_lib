package com.wyl.doctor.utils;

import android.util.Log;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：log工具
 */
public class LogUtil {
    public static void e(String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        Log.d(tag, msg);
    }
}
