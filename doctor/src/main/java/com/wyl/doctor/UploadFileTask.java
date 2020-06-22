package com.wyl.doctor;

import android.util.Log;

import com.wyl.doctor.upload.IUpload;
import com.wyl.doctor.upload.UploadBean;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：上传文件的任务
 */
public class UploadFileTask implements Runnable {
    public static final String TAG = UploadFileTask.class.getName();
    private UploadBean uploadBean;
    private IUpload iUpload;

    public UploadFileTask(UploadBean uploadBean, IUpload iUpload) {
        this.uploadBean = uploadBean;
        this.iUpload = iUpload;
    }

    @Override
    public void run() {
        if (uploadBean == null || iUpload == null || uploadBean.file == null) return;
        Log.d(TAG, "UploadFileTask--run: 开始上传文件：" + uploadBean.file.getAbsolutePath());
        if (iUpload.upload(uploadBean)) {
            //上传成功，删除文件
            uploadBean.file.delete();
        }
    }
}
