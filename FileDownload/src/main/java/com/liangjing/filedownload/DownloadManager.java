package com.liangjing.filedownload;

import com.liangjing.filedownload.http.DownloadCallback;
import com.liangjing.filedownload.http.HttpManager;
import com.liangjing.filedownload.utils.LoggerUtil;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/31.
 * 功能：既然要实现多线程下载，那么需要一个类来对多个线程进行统一管理和调度。
 * DownloadManager类是用来管理整个上层所调用的一些处理的统一的接口
 */

public class DownloadManager {

    //最大线程数
    private final static int MAX_THREAD = 2;
    private static DownloadManager sManager = new DownloadManager();
    private long mLength;

    //需要对多个线程进行管理，所以需要有一个线程池
    private static final ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {

        private AtomicInteger mInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "download thread #" + mInteger.getAndIncrement());
            return thread;
        }
    });

    private DownloadManager() {
    }

    public static DownloadManager getInstance() {

        if (sManager == null) {
            synchronized (DownloadManager.class) {
                if (sManager == null) {
                    sManager = new DownloadManager();
                }
            }
        }
        return sManager;
    }

    /**
     * function:供外部调用
     * 上层用户只需要传入请求Url和回调接口callback参数即可，至于具体文件大小，线程之间如何分配皆在此判断。
     *
     * @param url
     * @param callback
     */
    public void download(final String url, final DownloadCallback callback) {

        HttpManager.getInstance().asyncRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoggerUtil.debug("DownloadManager", "onFailure ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() && callback != null) {
                    callback.fail(HttpManager.NETWORK_ERROR_CODE, "网络出问题了");
                    return;
                }

                //拿到文件整体的长度
                mLength = response.body().contentLength();
                //需要及西宁判断length是否为-1，因为有些服务器不支持content-length字段
                if (mLength == -1) {
                    callback.fail(HttpManager.CONTENT_LENGTH_ERROR_CODE, "content-length -1");
                    return;
                }

                processDownload(url, mLength, callback);
            }

        });
    }

    /**
     * //判断每个线程所需要下载多长的数据
     *
     * @param url
     * @param length
     * @param callback
     */
    private void processDownload(String url, long length, DownloadCallback callback) {

        //每个线程平均下载多长的数据
        long threadDownloadSize = length / MAX_THREAD;

        // 100   2  50  0-49  50-99
        for (int i = 0; i < MAX_THREAD; i++) {

            long startSize = i * threadDownloadSize;
            long endSize = 0;
            if (endSize == MAX_THREAD - 1) {
                endSize = length - 1;
            } else {
                endSize = (i + 1) * threadDownloadSize - 1;
            }

            //提交线程进入线程池
            sThreadPool.execute(new DownloadRunnable(callback, startSize, endSize, url));

        }
    }
}
