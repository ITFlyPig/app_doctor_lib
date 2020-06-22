package com.wyl.doctor;

import android.util.Log;

import com.wyl.doctor.unchanged.BaseLogBean;
import com.wyl.doctor.file.LogManager;

import java.util.ArrayList;

/**
 * author : wangyuelin
 * time   : 2020/5/9 5:53 PM
 * desc   : 写入到文件的task
 */
public class WriteTask implements Runnable{
    private ArrayList<BaseLogBean> beans;

    public WriteTask(ArrayList<BaseLogBean> beans) {
        this.beans = beans;
    }

    @Override
    public void run() {
        Log.d("wyl", "WriteToFileTask 开始写入任务");
        if (beans != null) {
            for (BaseLogBean bean : beans) {
                Log.d("wyl", "开始写的bean：" + bean.toString());
                LogManager.instance().record(bean, bean.type);
                Log.d("tttttttttt", "WriteToFileTask--run: 写入到文件 "  + Thread.currentThread().getName());
            }
        }
        Log.d("wyl", "WriteToFileTask 写入任务完成");
    }
}
