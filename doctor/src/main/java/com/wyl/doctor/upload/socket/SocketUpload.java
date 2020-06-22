package com.wyl.doctor.upload.socket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.wyl.doctor.FileUtils;
import com.wyl.doctor.upload.IUpload;
import com.wyl.doctor.upload.UploadBean;
import com.wyl.doctor.upload.websocket.ConnectStatus;
import com.wyl.doctor.upload.websocket.WebSocketHandler;

import okio.ByteString;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：socket上传，主要是上传实时的信息
 */
public class SocketUpload implements IUpload {
    public static final String TAG = SocketUpload.class.getName();
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private SocketHelper socketHelper;
    private WebSocketHandler webSocketHandler;

    public SocketUpload(String host, int port) {

//        socketHelper = new SocketHelper(port, host);
        mHandlerThread = new HandlerThread("socket_upload");
        mHandlerThread.start();
        webSocketHandler = WebSocketHandler.getInstance("ws://10.0.2.2:8080/doctor/websocket/Android_client_01");
        webSocketHandler.connect(null);
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                //使用socket发送
                UploadBean bean = (UploadBean) msg.obj;
                if (bean.logBean != null) {
//                    socketHelper.tryWrite(FileUtils.toByte(bean.logBean), bean.type);
                    if (webSocketHandler.getStatus() == ConnectStatus.Open) {
                        byte[] data = FileUtils.toByte(bean.logBean);
                        if (data == null || data.length == 0) {
                            Log.d(TAG, "SocketUpload--handleMessage: 将对象转为字节数组，字节数组为空");
                            return;
                        }
                        webSocketHandler.send(ByteString.of(data));
                    } else {
                        Log.d(TAG, "SocketUpload--handleMessage: WebSocket未连接，丢弃bean" );
//                        if (webSocketHandler.getStatus() != ConnectStatus.Connecting) {
//                            Log.d(TAG, "SocketUpload--handleMessage: 开始重新连接websocket");
//                            webSocketHandler.reConnect();
//                        }
                    }
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
