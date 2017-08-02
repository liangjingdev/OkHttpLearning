package com.liangjing.filedownload;

import android.os.Process;

import com.liangjing.filedownload.db.DownloadEntity;
import com.liangjing.filedownload.file.FileStorageManager;
import com.liangjing.filedownload.http.DownloadCallback;
import com.liangjing.filedownload.http.HttpManager;
import com.liangjing.filedownload.db.DownloadHelper;
import com.liangjing.filedownload.utils.LoggerUtil;

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
    private DownloadEntity mEntity;

    public DownloadRunnable(DownloadCallback mCallback, long mEnd, long mStart, String mUrl) {
        this.mCallback = mCallback;
        this.mEnd = mEnd;
        this.mStart = mStart;
        this.mUrl = mUrl;
    }

    public DownloadRunnable(long mStart, long mEnd, String mUrl, DownloadCallback mCallback, DownloadEntity mEntity) {
        this.mStart = mStart;
        this.mEnd = mEnd;
        this.mUrl = mUrl;
        this.mCallback = mCallback;
        this.mEntity = mEntity;
    }

    /**
     * function:在这个方法当中去处理整个多线程的下载(比如每个线程应该去下载多大范围的内容)
     */
    @Override
    public void run() {

        //设置线程优先级别为后台线程，为了减少系统调度时间，使UI线程会得到更多响应时间(属于线程优化)
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Response response = HttpManager.getInstance().syncRequestByRange(mUrl, mStart, mEnd);
        //需要判断获取到的response是否为空
        if (response == null && mCallback != null) {
            mCallback.fail(HttpManager.NETWORK_ERROR_CODE, "网络出问题了");
            return;
        }

        //传入请求的URL到文件管理类FileStorageManager，生成对应的文件，准备进行读写。
        //此次写入同普通的有所不同，因为每个线程下载的具体位置不同，所以需要定位到下载的起始位置再进行写入，这里使用RandomAccessFile类来进行定位，其余部分大致相同。
        File file = FileStorageManager.getInstance().getFileByName(mUrl);

        //每次下载时去获取本地数据库中相关信息（避免重复下载，实现断点续传） 如果finishProgress不为空，则是上一次完成的进度
        long finishProgress = mEntity.getProgressPosition() == null ? 0 : mEntity.getProgressPosition();
        long progress = 0; //经过本次下载所完成的进度

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(mStart);
            byte[] buffer = new byte[1024 * 500];
            int len;
            InputStream inputStream = response.body().byteStream();
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                randomAccessFile.write(buffer, 0, len);
                //记录写入文件的长度
                progress += len;
                //当前文件下载的进度大小
                mEntity.setProgressPosition(progress);
                LoggerUtil.debug("jing", "progress ------->" + progress);
            }
            //不论下载成功或失败），将读写到File的实际长度等相关信息记录到DownloadEntity实体类
            mEntity.setProgressPosition(mEntity.getProgressPosition() + finishProgress);//本次下载后的新的总进度
            randomAccessFile.close();
            mCallback.success(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //同步，即插入数据到数据库中(不管是成功还是失败)(原因：将少数据库的相关操作)
            DownloadHelper.getInstance().insert(mEntity);
        }
    }

}
