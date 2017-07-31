package com.liangjing.filedownload.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liangjing on 2017/7/31.
 * function:对url进行加密的一个工具类
 */

public class Md5Util {

    public static String generateCode(String url) {

        //首先对url进行校验，判断传进来的url是否为空
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        StringBuffer buffer = new StringBuffer();

        try {
            //MessageDigest--加密类 这里参数指定为md5,那么就会返回md5的一个加密的算法
            MessageDigest digest = MessageDigest.getInstance("md5");

            //对url进行加密
            digest.update(url.getBytes());

            //加密完成后，通过digest()进行计算可以拿到对应的二进制数据(加密后的数据)
            byte[] cipher = digest.digest();

            //由于加密后的数据是二进制数据，所以我们得把它转换成16进制的数据，我们才看得懂
            for (byte b : cipher) {
                String hexStr = Integer.toHexString(b & 0xff);
                buffer.append(hexStr.length() == 1 ? "0" + hexStr : hexStr);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }
}
