package com.wyl.doctor.utils;


import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/28
 * 描述     ：网络请求工具
 */
public class OkhttpHelper {
    private static final String TAG = OkhttpHelper.class.getName();
    private static OkhttpHelper okhttpHelper = Holder.okhttpHelper;
    private OkHttpClient okHttpClient;

    private OkhttpHelper() {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();
    }

    private static class Holder {
        private static OkhttpHelper okhttpHelper = new OkhttpHelper();
    }

    public static OkhttpHelper instance() {
        return okhttpHelper;
    }

    /**
     * 上传文件
     * @param url
     * @param file
     * @param type 文件类型
     */
    public void uploadFile(String url, File file, int type) {
        if (TextUtils.isEmpty(url) || file == null) {
            return;
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .addFormDataPart("type", String.valueOf(type))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d(TAG, "uploadFile: 上传文件成功" );
            } else {
                Log.d(TAG, "uploadFile: 上传文件失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
