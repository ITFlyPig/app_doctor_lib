package com.wyl.doctor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author : wangyuelin
 * time   : 2020/5/9 5:19 PM
 * desc   : 线程池帮助类，为了最大限度的降低性能损耗，使用2线程
 */
public class ThreadHelper {
    private static ThreadHelper threadHelper = Holder.threadHelper;
    private static class Holder {
        private static ThreadHelper threadHelper = new ThreadHelper();

    }
    private ThreadHelper(){}

    public static ThreadHelper getInstance() {
        return threadHelper;
    }

    private ExecutorService executor = Executors.newFixedThreadPool(3);

    public void submit(Runnable task) {
        executor.submit(task);
    }
}
