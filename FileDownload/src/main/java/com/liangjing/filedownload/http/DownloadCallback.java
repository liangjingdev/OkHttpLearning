package com.liangjing.filedownload.http;

import java.io.File;

/**
 * Created by liangjing on 2017/7/31.
 * function: 该网络请求接口的作用是用来处理上层一些接口的回调
 */

public interface DownloadCallback {

    //文件下载成功之后的回调，下载完成之后需要把当前文件的File通过该回调方法给传进来，
    //若有需求可在此回调中获取文件具体存储位置等相关信息。
    void success(File file);

    //文件下载失败之后的回调
    void fail(int errorCode, String errorMessage);

    //文件下载的进度
    void progress(int progress);

}
