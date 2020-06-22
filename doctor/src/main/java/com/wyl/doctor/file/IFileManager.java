package com.wyl.doctor.file;

import java.io.File;
import java.util.List;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：文件管理接口，实现类分为：1、一般的文件写入 2、mmap文件写
 */
public interface IFileManager {

    /**
     * 写入到文件，用户不用关心写入到哪个文件
     * @param bytes
     * @return
     */
    boolean writeToFile(byte[] bytes);

    /**
     * 删除文件
     * @param f
     * @return
     */
    void remove(File f);

    /**
     * 获取当前目录下的所有文件
     * @return
     */
    List<File> getAllFiles();

    /**
     * 获得待上传的文件
     * @return
     */
    List<File> getWaitUploadFiles();

    /**
     * 是否应该上传日志文件
     * @return
     */
    boolean shouldUpload();

}
