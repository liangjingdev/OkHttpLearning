package com.liangjing.httpconnection.utils;

/**
 * Created by liangjing on 2017/8/2.
 * function:工具类--判断是否存在某个类
 */

public class JudgeUtil {

    public static boolean isExist(String className, ClassLoader loader) {

        try {
            Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
