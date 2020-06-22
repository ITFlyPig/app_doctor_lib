package com.wyl.doctor.upload.socket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.wyl.doctor.FileUtils;
import com.wyl.doctor.upload.IUpload;
import com.wyl.doctor.upload.UploadBean;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：socket上传，主要是上传实时的信息
 */
public class SocketUpload implements IUpload {
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private SocketHelper socketHelper;

    public SocketUpload(String host, int port) {
        socketHelper = new SocketHelper(port, host);
        mHandlerThread = new HandlerThread("socket_upload");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                //使用socket发送
                UploadBean bean = (UploadBean) msg.obj;
                if (bean.logBean != null) {
                    socketHelper.tryWrite(FileUtils.toByte(bean.logBean), bean.type);
                }

            }
        };
    }

    @Override
    public boolean upload(UploadBean bean) {
        Message message = Message.obtain();
        message.obj = bean;
        mHandler.sendMessage(message);
        return false;
    }


}
