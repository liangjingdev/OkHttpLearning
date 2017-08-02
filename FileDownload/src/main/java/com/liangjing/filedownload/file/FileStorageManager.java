package com.liangjing.filedownload.file;

import android.content.Context;
import android.os.Environment;

import com.liangjing.filedownload.utils.Md5Util;

import java.io.File;
import java.io.IOException;

/**
 * Created by liangjing on 2017/7/31.
 * 功能：多线程下载文件--文件管理类(如比管理文件存储之类的问题)--单例模式
 */

public class FileStorageManager {

    private static volatile FileStorageManager sManager = new FileStorageManager();

    private Context mContext;

    private FileStorageManager() {
    }

    public static FileStorageManager getInstance() {
        if (sManager == null) {
            synchronized (FileStorageManager.class) {
                if (sManager == null) {
                    sManager = new FileStorageManager();
                }
            }
        }
        return sManager;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    /**
     * function:首先根据提供的url通过加密后生成对应的文件名字，然后再利用存储路径去创建其文件对象(根据URL去获取文件)
     *          此时文件还是空的，后续需要对其进行写入--HttpManager
     *
     * @param url
     * @return
     */
    public File getFileByName(String url) {

        File parent;

        //判断是否存在外置的SD卡，若存在则采用外置的SD卡的路径
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //拿到外置SD卡存放与某个应用程序相对应的临时文件的一个目录
            parent = mContext.getExternalCacheDir();
        }else {
            //本机系统
            parent = mContext.getCacheDir();
        }

        //对url进行加密
        String fileName = Md5Util.generateCode(url);

        File file = new File(parent,fileName);
        //判断文件是否存在
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
