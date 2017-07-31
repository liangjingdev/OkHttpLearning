package com.liangjing.filedownload;

import com.liangjing.filedownload.file.FileStorageManager;
import com.liangjing.filedownload.http.DownloadCallback;
import com.liangjing.filedownload.http.HttpManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/31.
 * function:需要完成对线程的管理，还要实现一个Runnable接口，因为所有的多线程下载核心都在此操作。
 */

public class DownloadRunnable implements Runnable {

    private long mStart;//下载起始位置
    private long mEnd;//下载终止位置
    private String mUrl;//下载URL
    private DownloadCallback mCallback;

    public DownloadRunnable(DownloadCallback mCallback, long mEnd, long mStart, String mUrl) {
        this.mCallback = mCallback;
        this.mEnd = mEnd;
        this.mStart = mStart;
        this.mUrl = mUrl;
    }

    /**
     * function:在这个方法当中去处理整个多线程的下载(比如每个线程应该去下载多大范围的内容)
     */
    @Override
    public void run() {
        Response response = HttpManager.getInstance().syncRequestByRange(mUrl, mStart, mEnd);
        //需要判断获取到的response是否为空
        if (response == null && mCallback != null) {
            mCallback.fail(HttpManager.NETWORK_ERROR_CODE, "网络出问题了");
            return;
        }

        //传入请求的URL到文件管理类FileStorageManager，生成对应的文件，准备进行读写。
        //此次写入同普通的有所不同，因为每个线程下载的具体位置不同，所以需要定位到下载的起始位置再进行写入，这里使用RandomAccessFile类来进行定位，其余部分大致相同。
        File file = FileStorageManager.getInstance().getFileByName(mUrl);
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(mStart);
            byte[] buffer = new byte[1024 * 500];
            int len;
            InputStream inputStream = response.body().byteStream();
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                randomAccessFile.write(buffer, 0, len);
            }
            mCallback.success(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
