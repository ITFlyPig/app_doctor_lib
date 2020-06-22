package com.wyl.doctor.unchanged;

import com.wyl.doctor.utils.TimeUtil;

import java.io.Serializable;
import java.util.List;

/**
 * author : wangyuelin
 * time   : 2020/5/9 3:38 PM
 * desc   : 方法信息
 */
public class MethodBean extends BaseLogBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public String methodName;//方法名字
    public Object[] args;//方法的参数
    public String classFullName;//所属的类：全类名
    public long startTime;//开始调用的时间
    public long endTime;//结束调用的时间
    public String methodSignature;//方法的签名


    //下面的表示父子关系
    public List<MethodBean> childs;
    public MethodBean parent;

    public ThreadInfo threadInfo;

    @Override
    public String toString() {
        return "[name = " + methodName + "] " +
                "[classFullName = " + classFullName + "] " +
                "[startTime = " + TimeUtil.getDate(startTime) + "] " +
                "[endTime = " + TimeUtil.getDate(endTime) + "] " +
                "[threadName = " + threadInfo.name + "] ";

    }
}
