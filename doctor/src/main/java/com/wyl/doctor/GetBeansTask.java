package com.wyl.doctor;

import android.util.Log;

import com.wyl.doctor.unchanged.BaseLogBean;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author : wangyuelin
 * time   : 2020/5/9 5:17 PM
 * desc   : 获取beans
 */
public class GetBeansTask implements Runnable {
    private ReentrantLock takeLock;
    private Condition full;

    GetBeansTask(ReentrantLock takeLock, Condition full) {
        this.takeLock = takeLock;
        this.full = full;
    }

    @Override
    public void run() {
        //具体的写入逻辑
        while (true) {
            ArrayList<BaseLogBean> beans = null;
            takeLock.lock();
            try {
                beans = BeansCache.getFullCachedBeans();
                while (beans == null) {
                    Log.d("wyl", "GetBeansTask 开始等待");
                    full.await();
                    beans = BeansCache.getFullCachedBeans();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                takeLock.unlock();
            }
            Log.d("tttttttttt", "GetBeansTask--run: 从cache中打包取出来放到写入文件的任务中，线程： " + Thread.currentThread().getName());
            //开始写入到文件
            Log.d("wyl", "GetBeansTask 开始提交写入的任务");
            ThreadHelper.getInstance().submit(new WriteTask(beans));
        }
    }
}
