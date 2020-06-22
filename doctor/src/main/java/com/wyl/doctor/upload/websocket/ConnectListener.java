package com.wyl.doctor.upload.websocket;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/22
 * 描述    : WebSocket连接回调
 */
public interface ConnectListener {
    void onSuccess();//成功
    void onFailed();//失败
    void onClosed();//关闭
}
