package com.wyl.doctor.upload.http;

import android.util.Log;

import com.wyl.doctor.constant.Server;
import com.wyl.doctor.constant.Urls;
import com.wyl.doctor.upload.IUpload;
import com.wyl.doctor.upload.UploadBean;
import com.wyl.doctor.utils.OkhttpHelper;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：Okhttp实现的文件上传器
 */
public class OkHttpUpload implements IUpload {
    public static final String TAG = OkHttpUpload.class.getName();

    @Override
    public boolean upload(UploadBean bean) {
        if (bean == null || bean.file == null) return false;
        Log.d(TAG, "upload: 开始上传文件");
        OkhttpHelper.instance().uploadFile(Server.host + Urls.UPLOAD_URL, bean.file, bean.type);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Log.d(TAG, "upload: 文件上传成功");
        return true;
    }


}
