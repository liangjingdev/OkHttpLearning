package com.liangjing.okhttp3.application;

import android.app.Application;

import com.liangjing.filedownload.DownloadManager;
import com.liangjing.filedownload.config.DownloadConfig;
import com.liangjing.filedownload.db.DownloadHelper;
import com.liangjing.filedownload.file.FileStorageManager;
import com.liangjing.filedownload.http.HttpManager;

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

        //指定核心线程数、最大线程数..
        DownloadConfig config = new DownloadConfig.Builder()
                .coreThreadSize(2)
                .maxThreadSize(4)
                .localProgressThreadSize(1)
                .build();

        DownloadManager.getInstance().init(config);
    }
}
