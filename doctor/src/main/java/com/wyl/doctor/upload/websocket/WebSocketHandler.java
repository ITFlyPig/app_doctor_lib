package com.wyl.doctor.upload.websocket;

import android.util.Log;

import androidx.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/22
 * 描述    :
 */
public class WebSocketHandler extends WebSocketListener {
    private static final String TAG = "WebSocketHandler ";

    private String wsUrl;

    private WebSocket webSocket;

    private ConnectStatus status;

    private ConnectListener listener;

    private OkHttpClient client = new OkHttpClient.Builder()
            .build();

    private WebSocketHandler(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    private static WebSocketHandler INST;

    public static WebSocketHandler getInstance(String url) {
        if (INST == null) {
            synchronized (WebSocketHandler.class) {
                INST = new WebSocketHandler(url);
            }
        }

        return INST;
    }

    public ConnectStatus getStatus() {
        return status;
    }

    public void connect(ConnectListener listener) {
        this.listener = listener;
        //构造request对象
        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        webSocket = client.newWebSocket(request, this);
        status = ConnectStatus.Connecting;
    }

    public void reConnect(ConnectListener listener) {
        this.listener = listener;
        if (webSocket != null) {
            webSocket = client.newWebSocket(webSocket.request(), this);
        }
    }

    public void send(String text) {
        if (webSocket != null) {
            Log.d(TAG, "WebSocketHandler--send: ");
            webSocket.send(text);
        }
    }

    public void send(ByteString byteString) {
        if (webSocket != null && byteString != null) {
            Log.d(TAG, "WebSocketHandler--send: ");
            webSocket.send(byteString);
        }
    }

    public void cancel() {
        if (webSocket != null) {
            webSocket.cancel();
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, null);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        Log.d(TAG, "WebSocketHandler--onOpen: ");
        this.status = ConnectStatus.Open;
        if (listener != null) {
            listener.onSuccess();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        Log.d(TAG, "WebSocketHandler--onMessage: ");
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        Log.d(TAG, "WebSocketHandler--onClosing: " + reason);
        this.status = ConnectStatus.Closing;
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        Log.d(TAG, "WebSocketHandler--onClosed: " + reason);
        this.status = ConnectStatus.Closed;
        if (listener != null) {
            listener.onClosed();
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        t.printStackTrace();
        Log.d(TAG, "WebSocketHandler--onFailure: " + t.getMessage());
        this.status = ConnectStatus.Failed;
        if (listener != null) {
            listener.onFailed();
        }
    }

}


