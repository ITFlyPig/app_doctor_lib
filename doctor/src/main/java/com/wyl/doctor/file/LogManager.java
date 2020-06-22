package com.wyl.doctor.file;

import com.wyl.doctor.FileUtils;
import com.wyl.doctor.LogType;
import com.wyl.doctor.upload.UploadBean;
import com.wyl.doctor.upload.UploadUtil;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：日志文件的管理
 */
public class LogManager {
    private static final String TAG = LogManager.class.getName();
    private IFileManager keyPathLogManager;//关键路径的日志管理
    private IFileManager cpuLogManager;//cpu日志的文件管理
    private IFileManager memLogManager;//内存日志管理
    private IFileManager netLogManager;//网络日志管理

    private LogManager() {
        keyPathLogManager = new FileManagerImpl.Builder()
                .setDays(1)//最多保存一天
                .setMaxNum(20)//最多保存20个日志文件
                .setOneFileSize(1024 * 1024)//单个文件最大1m
                .setDirPath("")
                .setStartUploadNum(Integer.MAX_VALUE)//不主动上传
                .build();
        cpuLogManager = new FileManagerImpl.Builder()
                .setDays(2)//最多保存2天
                .setMaxNum(10)//最多保存20个日志文件
                .setOneFileSize(1024 * 1024)//单个文件最大1m
                .setDirPath("")
                .setStartUploadNum(1)//1个文件开始上传
                .build();
        memLogManager = new FileManagerImpl.Builder()
                .setDays(2)//最多保存2天
                .setMaxNum(10)//最多保存20个日志文件
                .setOneFileSize(1024 * 1024)//单个文件最大1m
                .setDirPath("")
                .setStartUploadNum(1)//1个文件开始上传
                .build();


    }

    private static class Holder {
        private static LogManager logManager = new LogManager();
    }

    public static LogManager instance() {
        return Holder.logManager;
    }

    /**
     * 记录日志：
     * 先写入到文件、然后处理上传文件
     *
     * @param obj
     * @param type
     */
    public void record(Serializable obj, int type) {
        if (obj == null) return;
        byte[] bytes = FileUtils.toByte(obj);
        if (bytes == null) return;
        switch (type) {
            case LogType.CPU_LOG:
                cpuLogManager.writeToFile(bytes);
                handleUpload(cpuLogManager, type);
                break;
            case LogType.MEM_LOG:
                memLogManager.writeToFile(bytes);
                handleUpload(cpuLogManager, type);
                break;
            case LogType.KEY_PATH_LOG:
                keyPathLogManager.writeToFile(bytes);
                handleUpload(cpuLogManager, type);
                break;
        }

    }

    /**
     * 记录关键路劲的日志
     *
     * @param obj
     */
    public void recordKeyPath(Serializable obj) {
        record(obj, LogType.KEY_PATH_LOG);
    }

    /**
     * 记录cpu日志
     *
     * @param obj
     */
    public void recordCpu(Serializable obj) {
        record(obj, LogType.CPU_LOG);
    }


    /**
     * 处理文件的上传
     *
     * @param iFileManager
     * @param type
     */
    private void handleUpload(IFileManager iFileManager, int type) {
        if (iFileManager.shouldUpload()) {
            List<File> waitUploadFiles = iFileManager.getWaitUploadFiles();
            int len = waitUploadFiles == null ? 0 : waitUploadFiles.size();
            if (len > 0) {
                for (File waitUploadFile : waitUploadFiles) {
                    //将文件添加到队列，等待上传
                    UploadBean uploadBean = new UploadBean(waitUploadFile, type);
                    UploadUtil.uploadAsync(uploadBean);
                }
            }
        }

    }

}
