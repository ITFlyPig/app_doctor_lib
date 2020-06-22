package com.wyl.doctor.file;

import android.text.TextUtils;

import com.wyl.doctor.FileUtils;
import com.wyl.doctor.utils.LogUtil;
import com.wyl.doctor.utils.NumberUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：文件管理：提供配置文件的数量、保留的时间、每个文件的大小
 */
public class FileManagerImpl implements IFileManager {
    private static final String TAG = FileManagerImpl.class.getName();
    private int maxNum;//保存的最大文件数量
    private long oneFileSize;//一个文件的最大size
    private int days;//存放时长，单位为天
    private String dirPath;//目录路径
    private int startUploadNum;//触发开始上传到服务器的文件数量



    private List<FileInfo> files;
    private List<File> waitUploadFiles;//等待上传的文件

    /**
     * 可自定义配置的构造函数
     *
     * @param builder
     */
    private FileManagerImpl(Builder builder) {
        this.maxNum = builder.maxNum;
        this.oneFileSize = builder.oneFileSize;
        this.days = builder.days;
        this.startUploadNum = builder.startUploadNum;

        this.files = new ArrayList<>();
        this.waitUploadFiles = new ArrayList<>();
        scanDir();
        handleTimeLimit();
    }

    /**
     * 使用默认配置的文件管理
     */
    public static FileManagerImpl createDefaultManager(String dirPath) {
        return new FileManagerImpl.Builder()
                .setDays(1)//保存一天
                .setMaxNum(10)//最多保存10个日志文件
                .setOneFileSize(2 * 1024 * 1024)//单个文件最大5m
                .setDirPath(dirPath)
                .setStartUploadNum(4)
                .build();
    }

    public static class Builder {
        private int maxNum;//最大文件数量
        private long oneFileSize;//一个文件的最大size
        private int days;//存放时长，单位为天
        private String dirPath;//目录路径
        private int startUploadNum;//触发开始上传到服务器的文件数量

        public Builder setMaxNum(int maxNum) {
            this.maxNum = maxNum;
            return this;
        }

        public Builder setOneFileSize(long oneFileSize) {
            this.oneFileSize = oneFileSize;
            return this;
        }

        public Builder setDays(int days) {
            this.days = days;
            return this;
        }

        public Builder setDirPath(String dirPath) {
            this.dirPath = dirPath;
            return this;
        }

        public Builder setStartUploadNum(int startUploadNum) {
            this.startUploadNum = startUploadNum;
            return this;
        }

        public FileManagerImpl build() {
            return new FileManagerImpl(this);
        }
    }

    @Override
    public boolean writeToFile(byte[] bytes) {
        if (bytes == null) return false;
        if (!makeSureDir(dirPath)) return false;

        File file = findWritableFile();
        if (file == null) {
            LogUtil.e(TAG, "writeToFile: 文件写入失败");
            return false;
        }

        boolean result = FileUtils.writeToFile(file, bytes);
        //文件写入失败
        if (!result) {
            return false;
        }

        //当前文件的大小足够了，新建一个文件
        if (isReachSizeLimit(file)) {
            createNewFile();
            waitUploadFiles.add(file);
        }
        //处理文件数量限制
        handleNumLimit();
        //处理文件保留时间限制
        handleTimeLimit();
        return false;
    }

    @Override
    public void remove(File f) {
        if (f == null) return;
        Iterator<FileInfo> it = files.iterator();
        while (it.hasNext()) {
            File curFile = it.next().file;
            if (curFile != null && TextUtils.equals(curFile.getAbsolutePath(), f.getAbsolutePath())) {
                //磁盘删除
                curFile.delete();
                //内存删除
                it.remove();
                return;
            }
        }
    }

    @Override
    public List<File> getAllFiles() {
        ArrayList<File> fileList = new ArrayList<>();
        for (FileInfo fileInfo : files) {
            fileList.add(fileInfo.file);
        }
        return fileList;
    }

    @Override
    public List<File> getWaitUploadFiles() {
        List<File> files = null;
        if (waitUploadFiles.size() > 0) {
            files = waitUploadFiles;
            waitUploadFiles = new ArrayList<>();
        }
        return files;
    }

    @Override
    public boolean shouldUpload() {
        return waitUploadFiles.size() >= startUploadNum ;
    }

    /**
     * 扫描当前目录，将文件信息添加记录到内存中
     */
    private void scanDir() {
        if (TextUtils.isEmpty(dirPath)) {
            return;
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }
        File[] fileArray = dir.listFiles();
        if (fileArray != null) {
            for (File file : fileArray) {
                FileInfo fileInfo = parseInfo(file);
                if (fileInfo != null) {
                    files.add(fileInfo);

                }

                //将文件添加到待上传集合
                if(isReachSizeLimit(file)) {
                    waitUploadFiles.add(file);
                }
            }
        }
    }

    /**
     * 解析得到文件相关信息
     *
     * @param f
     * @return
     */
    private FileInfo parseInfo(File f) {
        if (f == null) return null;
        FileInfo fileInfo = new FileInfo();
        fileInfo.file = f;
        fileInfo.path = f.getAbsolutePath();
        fileInfo.size = f.length();
        long[] timeArr = parseTime(f.getAbsolutePath());
        if (timeArr != null) {
            fileInfo.createTime = timeArr[0];
            fileInfo.lastModifyTime = timeArr[1];
        }
        return fileInfo;
    }

    /**
     * 解析得到开始时间和最后编辑时间
     *
     * @param path
     * @return
     */
    private long[] parseTime(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        //获得文件名
        String name = null;
        int start = path.lastIndexOf(File.separator);
        int end = path.length();
        if (start >= 0 && start < end) {
            name = path.substring(start, end);
        }
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        String[] timeArr = name.split("-");
        if (timeArr.length < 2) {
            return null;
        }

        long startTime = NumberUtil.parseLong(timeArr[0], -1);
        if (startTime <= 0) {
            return null;
        }
        long endTime = NumberUtil.parseLong(timeArr[1], -1);
        if (endTime <= 0) {
            return null;
        }
        return new long[]{startTime, endTime};
    }


    /**
     * 确保dir存在
     *
     * @param dirPath
     * @return
     */
    private boolean makeSureDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        //确保目录存在
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                //目录创建失败，返回
                LogUtil.e(TAG, "makeSureDir 目录创建失败：" + dirPath);
                return false;
            }
        }
        return true;
    }

    /**
     * 找到可写入的文件
     * 如果返回的文件不为空，那么文件一定是可以用的
     *
     * @return
     */
    private File findWritableFile() {
        File writableFile = null;
        //末尾开始查找，因为新添加的数据总是放在末尾
        for (int i = files.size() - 1; i >= 0; i--) {
            FileInfo fileInfo = files.get(i);
            if (isWritable(fileInfo.file)) {
                writableFile = fileInfo.file;
                return writableFile;
            }

        }
        //未找到，创建新的
        writableFile = createNewFile();
        return writableFile;
    }

    /**
     * 是否是可写的文件
     *
     * @param file
     * @return
     */
    private boolean isWritable(File file) {
        return file != null && file.length() < oneFileSize;
    }

    /**
     * 创建文件
     *
     * @return
     */
    private File createNewFile() {
        if (TextUtils.isEmpty(dirPath)) {
            return null;
        }
        long curTime = System.currentTimeMillis();
        String newFilepath = dirPath + File.separator + curTime + "-";
        File newFile = new File(newFilepath);
        try {
            //文件创建成功，记录下来
            if (newFile.createNewFile()) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.createTime = curTime;
                fileInfo.path = newFilepath;
                fileInfo.file = newFile;
                files.add(fileInfo);
                return newFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否达到文件限制的大小
     *
     * @param f
     * @return
     */
    private boolean isReachSizeLimit(File f) {
        return f.length() >= oneFileSize;
    }

    /**
     * 处理时间限制，超过保留时间的都删除了
     */
    private void handleTimeLimit() {
        long keepTime = days * 24 * 60 * 60 * 1000;
        long curTime = System.currentTimeMillis();
        Iterator<FileInfo> it = files.iterator();
        while (it.hasNext()) {
            FileInfo fileInfo = it.next();
            if (curTime - fileInfo.lastModifyTime >= keepTime) {
                //磁盘删除
                if (fileInfo.file != null) {
                    fileInfo.file.delete();
                }
                //内存删除
                it.remove();
            }
        }
    }

    /**
     * 超过数量限制的，将最久的删除
     */
    private void handleNumLimit() {
        int size = files.size();
        if (size <= maxNum) return;
        //排序
        Collections.sort(files, new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                long left = (o1.lastModifyTime - o2.lastModifyTime);
                if (left < 0) {
                    return -1;
                } else if (left > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        //删除最久的几个
        int count = maxNum - size;
        for (int i = 0; i < count; i++) {
            FileInfo fileInfo = files.get(files.size() - 1);
            if (fileInfo.file != null) {
                fileInfo.file.delete();
            }
            files.remove(files.size() - 1);
        }
    }

}
