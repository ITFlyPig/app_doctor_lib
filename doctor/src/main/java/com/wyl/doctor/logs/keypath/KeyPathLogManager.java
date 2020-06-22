package com.wyl.doctor.logs.keypath;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：关键路径日志的管理
 */
public class KeyPathLogManager {
    private static KeyPathLogManager keyPathLogManager = Holder.manager;
    private KeyPathLogManager(){}
    private static class Holder {
        private static KeyPathLogManager manager = new KeyPathLogManager();
    }
    public static KeyPathLogManager instance() {
        return keyPathLogManager;
    }




}
