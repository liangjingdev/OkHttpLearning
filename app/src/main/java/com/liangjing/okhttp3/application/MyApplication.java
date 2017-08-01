package com.liangjing.okhttp3.application;

import android.app.Application;

import com.liangjing.filedownload.file.FileStorageManager;
import com.liangjing.filedownload.http.HttpManager;
import com.liangjing.filedownload.utils.DownloadHelper;

/**
 * Created by liangjing on 2017/7/31.
 * function:入口 application
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileStorageManager.getInstance().init(this);
        HttpManager.getInstance().init(this);
        DownloadHelper.getInstance().init(this);
    }
}
