package com.wyl.doctor.upload;

import android.util.Log;

import com.wyl.doctor.ThreadHelper;
import com.wyl.doctor.UploadFileTask;
import com.wyl.doctor.upload.http.OkHttpUpload;
import com.wyl.doctor.upload.socket.SocketUpload;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：上传工具类
 */
public class UploadUtil {
    public static final String TAG = "wyl";
    //目前只提供了okhttp的上传实现
    private static IUpload iUpload = new OkHttpUpload();//上传器
    private static LinkedBlockingQueue<UploadBean> uploadQueue = new LinkedBlockingQueue<>();//待上传文件的容器
    private static volatile boolean isStartUploadTask = false;//是否开启了上传的任务
    private static volatile boolean isStopUpload = false;//是否停止上传
    private static IUpload socketUpload = new SocketUpload("10.0.2.2", 8088);

    /**
     * 异步上传，当队列中的文件超过Integer.MAX_VALUE时，要上传的文件直接被丢弃
     *
     * @param uploadBean
     */
    public static void uploadAsync(UploadBean uploadBean) {
        Log.d(TAG, "uploadAsync: 异步上传文件");

        if (uploadBean == null) {
            return;
        }
        if (!isStartUploadTask) {
            //开启上传任务
            isStartUploadTask = true;
            startUploadTask();
        }
        Log.d(TAG, "UploadUtil--uploadAsync: 添加到文件上传队列，线程： " + Thread.currentThread().getName());
        uploadQueue.offer(uploadBean);
    }

    /**
     * 停止上传
     */
    public static void stopUpload() {
        isStopUpload = true;
    }

    /**
     * 同步上传
     *
     * @param uploadBean
     */
    private static void upload(UploadBean uploadBean) {
        if (uploadBean == null) return;
        new UploadFileTask(uploadBean, iUpload).run();
    }

    /**
     * 开启上传文件的任务
     */
    private static void startUploadTask() {
        Log.d(TAG, "startUploadTask: 开启文件上传任务");
        ThreadHelper.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                while (!isStopUpload) {
                    try {
                        //获得文件
                        UploadBean uploadBean = uploadQueue.take();
                        File file = uploadBean.file;
                        Log.d(TAG, "startUploadTask  获得文件");
                        //上传
                        upload(uploadBean);
                        Log.d(TAG, "UploadUtil--run: 文件上传成功，线程： " + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    /**
     * socket 立即传输
     *
     * @param bean
     */
    public static void socketUploadNow(UploadBean bean) {
        if (bean == null) return;
        Log.d(TAG, "UploadUtil--socketUploadNow: 直接使用socket上传bean");
        socketUpload.upload(bean);
    }
}
