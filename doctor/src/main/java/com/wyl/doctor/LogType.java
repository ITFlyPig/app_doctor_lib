package com.wyl.doctor;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：日志分类
 */
public interface LogType {
    int KEY_PATH_LOG = 1; //关键路劲的方法调用日志
    int NET_LOG = 2;//网络日志
    int CPU_LOG = 3;//cpu日志
    int MEM_LOG = 4;//内存日志
    int ALL_PATH = 5;//所有路劲日志，只是为了排查问题，调试模式有效
}
