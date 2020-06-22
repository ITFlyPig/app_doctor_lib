package com.wyl.doctor.upload.socket;

import android.util.Log;

import com.wyl.doctor.LogType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：socket使用帮助类，提供了socket相关的操作
 */
public class SocketHelper {
    private static final String TAG = SocketHelper.class.getName();
    private int port;
    private String host;
    private Socket socket;
    private OutputStream os;
    private static final int MAX_TIMES = 5;//最大重连次数
    public static final int RETRY_WRITE_TIME = 3;//写重试次数
    private int count = 0;
    private int curRetryWriteTime = 0;

    public SocketHelper(int port, String host) {
        this.port = port;
        this.host = host;
    }

    /**
     * 具有重试的写入操作
     * @param bytes
     * @return
     */
    public boolean tryWrite(byte[] bytes, int type) {
        boolean ret = false;
        while (!(ret = write(bytes, type)) && curRetryWriteTime < RETRY_WRITE_TIME) {
            curRetryWriteTime++;
        }
        curRetryWriteTime = 0;
        return ret;

    }

    /**
     * socket写入数据
     *
     * @param bytes
     * @param type 数据的类型  必须的，不然后端不能识别
     * @return
     */
    private boolean write(byte[] bytes, int type) {
        if (bytes == null) return false;
        if (socket == null) {
            socket = new Socket();
        }
        if (!socket.isConnected()) {
            tryConnect();
        }
        if (!socket.isConnected()) {
            Log.e(TAG, "write: 不能写入数据，socket建立连接失败");
            return false;
        }
        Log.d(TAG, "SocketHelper--write: 开始往socket写数据");
        try {

            ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + bytes.length);
            //写入数据的类型
            buffer.putInt(type);
            //写入数据的大小
            buffer.putInt(bytes.length);
            //写入数据
            buffer.put(bytes);
            //写入到socket
            os.write(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "SocketHelper--write: 写入失败");
            release();
            return false;
        }
        Log.d(TAG, "SocketHelper--write: 写入成功");
        return true;
    }

    /**
     * 尝试建立连接
     */
    private void tryConnect() {
        count = 0;
        while (count < MAX_TIMES && !socket.isConnected()) {
            try {
                socket.connect(new InetSocketAddress(host, port));
                os = socket.getOutputStream();
                Log.d(TAG, "SocketHelper--tryConnect: socket连接成功");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                count++;
            }
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (socket != null) {

            Log.d(TAG, "SocketHelper--release: 释放socket资源");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }

}
