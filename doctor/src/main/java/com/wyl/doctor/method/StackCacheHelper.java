package com.wyl.doctor.method;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.wyl.doctor.BeansCache;
import com.wyl.doctor.LogType;
import com.wyl.doctor.unchanged.MethodBean;
import com.wyl.doctor.upload.UploadBean;
import com.wyl.doctor.upload.UploadUtil;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/26
 * 描述     ：栈处理线程，主要是负责压栈和出栈，尽最大力减轻主线程的负担
 */
public class StackCacheHelper {
    public static final String TAG = "wyl";
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private static final int MSG_PUSH = 1;//压栈
    private static final int MSG_POP = 2;//出栈

    public StackCacheHelper() {
        mHandlerThread = new HandlerThread("handle_stack_" + System.currentTimeMillis());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                handle(msg);
            }
        };
        //初始化缓存
        BeansCache.init();
    }

    /**
     * 处理命令
     * @param msg
     */
    private void handle(Message msg) {
        switch (msg.what) {
            case MSG_POP:
                if (msg.obj instanceof MethodBean) {
                    MethodBean bean = (MethodBean) msg.obj;
                    if (bean.threadInfo == null) return;
                    MethodBean topBean = MethodRecordStack.getInstance().pop(bean.threadInfo.id, bean.classFullName, bean.methodName, bean.methodSignature, bean.endTime);
                    if (topBean == null) return;
//                    Log.d(TAG, "HandleStackHelper--handle: json数据：" + JSON.toJSONString(bean));
//                    Log.d(TAG, "endtime--handle: 结束时间：" + bean.endTime);
                    //将其放到内存缓存中
                    if (topBean.type == LogType.ALL_PATH) {
                        //直接使用socket传输的日志
//                        Log.d(TAG, "HandleStackHelper--handle: 出栈，取到ALL_PATH类型的记录");
                        UploadUtil.socketUploadNow(new UploadBean(LogType.ALL_PATH ,topBean));
                    } else {
                        //需要写入到文件的日志
                        BeansCache.put(topBean);
                    }

                }
                break;
            case MSG_PUSH:

                if (msg.obj instanceof MethodBean) {
                    MethodBean bean = (MethodBean) msg.obj;
                    if (MethodRecordStack.getInstance().isTopAndroidLib(bean.threadInfo.id)) return;
//                    Log.d(TAG, "tttttt--handle: 入栈：" + bean.classFullName + ":" + bean.methodName + ":" + bean.methodSignature);
                    MethodRecordStack.getInstance().push(bean);
                }
                break;
        }
    }

    /**
     * 发送压栈命令
     * @param bean
     */
    public void sendPush(MethodBean bean) {
        if (bean == null) {
            return;
        }
        Message msg = Message.obtain();
        msg.what = MSG_PUSH;
        msg.obj = bean;
        mHandler.sendMessage(msg);
    }

    /**
     * 发送出栈命令
     * @param endCall
     */
    public void sendPop(MethodBean endCall) {
        Message msg = Message.obtain();
        msg.what = MSG_POP;
        msg.obj = endCall;
        mHandler.sendMessage(msg);
    }



}
