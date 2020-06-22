package com.wyl.doctor.upload.websocket;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/22
 * 描述    :
 */
public enum ConnectStatus {
    Connecting, // the initial state of each web socket.
    Open, // the web socket has been accepted by the remote peer
    Closing, // one of the peers on the web socket has initiated a graceful shutdown
    Closed, //  the web socket has transmitted all of its messages and has received all messages from the peer
    Failed // the web socket connection failed
}