package com.wyl.doctor.upload;

import com.wyl.doctor.unchanged.BaseLogBean;

import java.io.File;
import java.io.Serializable;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：上传bean
 */
public class UploadBean implements Serializable {
    public File file;//待上传的文件
    public int type;//数据类型

    public BaseLogBean logBean;


    public UploadBean(File file, int type) {
        this.file = file;
        this.type = type;
    }

    public UploadBean(int type, BaseLogBean logBean) {
        this.type = type;
        this.logBean = logBean;
    }
}
