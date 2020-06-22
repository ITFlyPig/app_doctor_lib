package com.wyl.doctor.utils;

import android.text.TextUtils;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/29
 * 描述     ：数字相关工具类
 */
public class NumberUtil {

    /**
     * 将字符串解析为long
     * @param s
     * @param def
     * @return
     */
    public static long parseLong(String s, long def) {
        long result = def;
        if (TextUtils.isEmpty(s)) {
            return result;
        }

        try {
          result = Long.parseLong(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }
}
