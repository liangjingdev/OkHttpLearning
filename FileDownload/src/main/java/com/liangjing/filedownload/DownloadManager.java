package com.liangjing.filedownload;

import com.liangjing.filedownload.config.DownloadConfig;
import com.liangjing.filedownload.db.DownloadEntity;
import com.liangjing.filedownload.db.DownloadHelper;
import com.liangjing.filedownload.file.FileStorageManager;
import com.liangjing.filedownload.http.DownloadCallback;
import com.liangjing.filedownload.http.HttpManager;
import com.liangjing.filedownload.utils.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public final static int MAX_THREAD = 2;
    public final static int LOCAL_PROGRESS_SIZE = 1;
    private static volatile DownloadManager sManager = new DownloadManager();
    private long mLength;

    //线程池
    private static ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    //监控下载进度的线程(该过程比较耗时)
    private static ExecutorService sLocalProgressPool = Executors.newFixedThreadPool(LOCAL_PROGRESS_SIZE);

    //缓存(下载)数据
    private List<DownloadEntity> mCache;

    //需要维护一个HashSet任务集合,保证每一个下载任务都是唯一的值。
    private HashSet<DownloadTask> mHashSet = new HashSet<>();

    private DownloadManager() {
    }


    /**
     * function:暴露该处由外部初始化来决定核心线程数、最大线程数等...
     * @param config
     */
    public void init(DownloadConfig config) {

        //需要对多个线程进行管理，所以需要有一个线程池
        sThreadPool = new ThreadPoolExecutor(config.getCoreThreadSize(), config.getMaxThreadSize(), 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadFactory() {

            private AtomicInteger mInteger = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable runnable) {
                //对每个线程都指定了一个名字
                Thread thread = new Thread(runnable, "download thread #" + mInteger.getAndIncrement());
                return thread;
            }
        });

        sLocalProgressPool = Executors.newFixedThreadPool(config.getLocalProgressThreadSize());
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

        //把每个下载任务都添加到 HashSet<DownloadTask> 任务集合当中
        final DownloadTask task = new DownloadTask(url, callback);

        //查询当前下载(缓存)数据是否为空,若为空，则开启多线程去下载
        mCache = DownloadHelper.getInstance().getAll(url);
        if (mCache == null || mCache.size() == 0) {
            HttpManager.getInstance().asyncRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LoggerUtil.debug("DownloadManager", "onFailure ");
                    finish(task);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful() && callback != null) {
                        callback.fail(HttpManager.NETWORK_ERROR_CODE, "网络出问题了");
                        return;
                    }

                    //拿到文件整体的长度
                    mLength = response.body().contentLength();
                    //需要判断length是否为-1，因为有些服务器不支持content-length字段
                    if (mLength == -1) {
                        callback.fail(HttpManager.CONTENT_LENGTH_ERROR_CODE, "content-length -1");
                        return;
                    }

                    processDownload(url, mLength, callback);
                    finish(task);
                }

            });
        } else {

            // TODO: 2017/8/1 处理已经下载过的数据
            //由于每个DownloadEntity都已经知道自己要下载多大的数据，那么可以从其入手
            for (int i = 0; i < mCache.size(); i++) {
                DownloadEntity entity = mCache.get(i);
                if (i == mCache.size() - 1) {
                    mLength = entity.getProgressPosition() + 1;
                }
                long startSize = entity.getStartPosition() + entity.getProgressPosition();
                long endSize = entity.getEndPosition();
                sThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback, entity));
            }
        }


        //判断所提交的任务是否已经执行了
        if (mHashSet.contains(task)) {
            callback.fail(HttpManager.TASK_RUNNING_ERROR_CODE, "任务已经执行了");
            return;
        }
        mHashSet.add(task);

        /**
         * function:不断地从数据库中获取文件当前已下载的大小，然后不断的将它与总文件大小进行比较，获取进度的百分比
         */
        sLocalProgressPool.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        //拿到对应的文件
                        File file = FileStorageManager.getInstance().getFileByName(url);
                        //当前文件的大小
                        long fileSize = file.length();
                        //进度
                        int progress = (int) (fileSize * 100.0 / mLength);
                        //注意：此处需要进行判断，保证进度刚好等于100的时候，能够同步到上层，并且让当前线程处于终止状态
                        if (progress >= 100) {
                            callback.progress(progress);
                            return;
                        }
                        callback.progress(progress);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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

        //当mCache为空的情况下，那么我们就需要去为其创建相应的数据
        if (mCache == null || mCache.size() == 0) {
            mCache = new ArrayList<>();
        }

        // 100   2  50  0-49  50-99
        for (int i = 0; i < MAX_THREAD; i++) {

            //创建每个线程所对应的实体类
            DownloadEntity entity = new DownloadEntity();
            long startSize = i * threadDownloadSize;
            long endSize = 0;
            if (endSize == MAX_THREAD - 1) {
                endSize = length - 1;
            } else {
                endSize = (i + 1) * threadDownloadSize - 1;
            }
            //将每个线程下载的具体信息存储到DownloadEntity实体类中，后续在DownloadRunnable进行操作，存储到表中。
            entity.setDownloadUrl(url);
            entity.setStartPosition(startSize);
            entity.setEndPosition(endSize);
            entity.setThreadId(i + 1);

            //提交线程进入线程池
            sThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback, entity));
        }
    }

    /**
     * function:需要对下载任务进行清除操作(不管是下载成功了还是下载失败了)
     *
     * @param task
     */
    private void finish(DownloadTask task) {
        mHashSet.remove(task);
    }
}
