package com.wyl.doctor.method;

import com.wyl.doctor.unchanged.ThreadInfo;

import java.util.HashMap;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/26
 * 描述     ：存储已有的线程的信息
 */
public class ThreadCache {
    private static HashMap<Long, ThreadInfo> mThreadMap = new HashMap<>();
    public static ThreadInfo getThreadInfo(long id, String name) {
        ThreadInfo info = mThreadMap.get(id);
        if (info == null) {
            //没有，构建一个
            info = new ThreadInfo(name, id);
            mThreadMap.put(id, info);
        }
        return info;
    }

}
