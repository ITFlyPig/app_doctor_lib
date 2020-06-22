package com.wyl.doctor;

import android.text.TextUtils;
import android.util.Log;

import com.wyl.doctor.file.FileManagerImpl;
import com.wyl.doctor.file.IFileManager;
import com.wyl.doctor.utils.LogUtil;
import com.wyl.doctor.utils.NumberUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：文件的管理，添加状态的记录和转换
 */
public class FileManager {
//    public static final String TAG = FileManager.class.getName();
//    private String dir;
//    private IFileManager iFileManager;
//
//    public FileManager(String dir) {
//        if (TextUtils.isEmpty(dir)) {
//            throw new IllegalArgumentException("构造FileManager异常，参数dir不能为空");
//        }
//        this.dir = dir;
//        iFileManager = FileManagerImpl.createDefaultManager(dir);
//    }
//
//
//
//    /**
//     * 为文件添加结束时间
//     *
//     * @param file
//     * @return
//     */
//    private File addEndTime(File file) {
//        if (file == null || !file.exists()) {
//            return null;
//        }
//        String oldName = file.getName();
//        if (TextUtils.isEmpty(oldName)) {
//            return null;
//        }
//        String newName = oldName + System.currentTimeMillis();
//        File newFile = new File(file.getParent() + File.separator + newName);
//        boolean result = file.renameTo(newFile);
//        return newFile;
//
//    }
//
//    /**
//     * 解析文件名的开始时间和结束时间，判断是否部分在需要的范围内
//     * @param anchor
//     * @param range
//     * @param fileName
//     * @return
//     */
//    private boolean isPartInRange(long anchor, long range, String fileName) {
//        if (TextUtils.isEmpty(fileName) || anchor <= 0 || range <= 0) {
//            return false;
//        }
//        String[] timeArray = fileName.split("-");
//        if (timeArray.length < 2) {
//            return false;
//        }
//        long start = NumberUtil.parseLong(timeArray[0], -1);
//        long end = NumberUtil.parseLong(timeArray[1], -1);
//        if (start < 0 || end < 0) {
//            return false;
//        }
//        long max = anchor + range;
//        long min = anchor - range;
//
//        return start >= min ;
//    }
//
//
//    //////////////一下为对外暴露的方法/////////////////
//
//
//    /**
//     * 可上传的文件集合
//     *
//     * @return
//     */
//    public List<File> findUploadFiles() {
//        return waitUploadFiles;
//    }
//
//    /**
//     * 重置可上传文件集合
//     */
//    public void resetUploadSet() {
//        Log.d(TAG, "FileManager--resetUploadSet: 重置待上传集合");
//        waitUploadFiles = new ArrayList<>();
//    }
//
//    /**
//     * 删除文件
//     *
//     * @param path
//     */
//    public void removeFile(String path) {
//        if (TextUtils.isEmpty(path)) {
//            return;
//        }
//        //删除磁盘上的文件
//        File f = new File(path);
//        f.delete();
//        //删除内存中的记录
//        remove(path, waitUploadFiles);
//        remove(path, writeableFiles);
//    }
//
//    /**
//     * 保存到文件
//     *
//     * @param obj
//     */
//    public void writeToFile(Serializable obj) {
//        if (obj == null || TextUtils.isEmpty(dir)) {
//            return;
//        }
//
//        //目录不可用
//        if (!makeSureDir(dir)) {
//            return;
//        }
//        //找到可写入的log文件
//        File logFile = findWritableFile();
//        if (logFile == null) {
//            LogUtil.e(TAG, "找不到合适的log文件写入");
//            return;
//        }
//        //开始写入
//        Log.d(TAG, "writeToFile: 开始写入，此时文件的大小：" + logFile.length());
//        FileUtils.writeToFile(logFile, obj);
//        Log.d(TAG, "writeToFile: 写入完成，此时文件的大小：" + logFile.length() + " 可写入集合的大小：" + writeableFiles.size());
//        //检查是否达到上传的大小，达到的放到可上传的集合里
//        if (logFile.length() >= oneFileMaxSize) {
//            remove(logFile.getAbsolutePath(), writeableFiles);
//            //重命名文件，添加结束时间
//            File renameFile = addEndTime(logFile);
//            //将文件添加到待上传集合
//            waitUploadFiles.add(renameFile);
//            Log.d(TAG, "writeToFile: 单文件大小达到要求，将文件添加到等待上传集合waitUploadFiles：");
//        }
//    }
//
//    /**
//     * 获取时间范围内的文件
//     *
//     * @param anchor 锚点时间
//     * @param range  时间范围 单位是 ms
//     * @return
//     */
//    public List<File> getFileByTime(long anchor, long range) {
//        ArrayList<File> files = new ArrayList<>();
//        int len = waitUploadFiles.size();
//        for (int i = 0; i < len; i++) {
//            File file = waitUploadFiles.get(i);
//            if (isPartInRange(anchor, range, file.getName())) {
//                files.add(file);
//            }
//        }
//        return files;
//    }

    //////////////////////////////////////////

}
