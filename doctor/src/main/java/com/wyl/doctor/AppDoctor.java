package com.wyl.doctor;

import android.content.Context;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/28
 * 描述     ：对外暴露
 */
public class AppDoctor {
    private static String logDirPath;//日志文件的存储目录
    private static Context context;

    public static void init(Context context) {
        AppDoctor.context = context;
    }


}
