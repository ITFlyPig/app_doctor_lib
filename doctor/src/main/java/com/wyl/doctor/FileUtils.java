package com.wyl.doctor;

import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * author : wangyuelin
 * time   : 2020/5/9 6:17 PM
 * desc   : 文件工具类
 */
public class FileUtils {
    /**
     * 将对象转为字节数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByte(Serializable obj) {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        byte[] buf = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            buf = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buf;
    }

    /**
     * 将字节数组转为对象
     *
     * @param buf
     * @param <T>
     * @return
     */
    public static <T> T toObj(byte[] buf) {
        if (buf == null) {
            return null;
        }
        T t = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            if (obj != null) {
                t = (T) obj;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }

    public static boolean writeToFile(File targetFile, Serializable obj) {
        boolean result = false;
        if (targetFile == null) {
            return result;
        }
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                //文件创建失败
                return result;
            }
        }
        byte[] buf = toByte(obj);
        int len = buf == null ? 0 : buf.length;
        if (len == 0) {
            return result;
        }
        ByteBuffer buffer = ByteBuffer.allocate(4 + len);
        buffer.putInt(len);//写入长度
        buffer.put(buf);//写数据

        //写入到文件
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(targetFile, true);
            bos = new BufferedOutputStream(fos);
            //将数据写入到文件
            bos.write(buffer.array());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;

    }

    public static boolean writeToFile(String path, Serializable obj) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return writeToFile(new File(path), obj);
    }


}
