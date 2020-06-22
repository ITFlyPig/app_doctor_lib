package com.wyl.doctor.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：时间工具类
 */
public class TimeUtil {

    /**
     * 时间戳转时间
     * @param time
     * @return
     */
    public static String getDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return  sdf.format(time);
    }
}
