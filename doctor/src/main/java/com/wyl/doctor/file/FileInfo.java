package com.wyl.doctor.file;

import java.io.File;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：文件信息
 */
public class FileInfo {
    public long size;
    public String path;
    public long createTime;//文件创建的时间
    public long lastModifyTime;//文件最后一次编辑的时间
    public File file;
}
