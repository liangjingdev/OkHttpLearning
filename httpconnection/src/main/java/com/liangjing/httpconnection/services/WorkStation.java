package com.liangjing.httpconnection.services;

import android.support.annotation.NonNull;

import com.liangjing.httpconnection.HttpRequestProvider;
import com.liangjing.httpconnection.http.HttpRequest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liangjing on 2017/8/5.
 * function:（多线程处理请求--工作栈）
 * 该类用于处理我们整个网络请求多线程的控制和队列的管理（类似于工作栈）--在整个框架的设计的时候，考虑到的一个因素就是我们需要能够快速的响应整个客户端的请求
 * 内部维护一个线程池成员变量，这里为了能够快速响应多个线程的同时请求数据，将线程数量最大值设置为Integer.MAX_VALUE。
 * 再引入两个队列，一个队列存储着请求request，另一个存储着cache，即待执行的请求request队列（考虑到处理线程数量超过最大限制时）。
 * <p>
 * 提供构造方法初始化成员变量HttpRequestProvider，经过前期封装后，获取请求request对象由专门供上层调用的类HttpRequestProvider完成。
 * 提供add 方法将请求任务添加到队列中。注意在这里需要做一个开启线程最大数判断，例如最多同时开启60个线程处理网络请求：若超过则将request添加cache队列中，等待执行。
 * 若未超过，则通过HttpRequestProvider获取请求request对象，最后由线程池执行。注意，既然是由线程池执行，这里还需要一个Runnable。
 * 提供finish 方法，在线程池执行Runnable时，即请求结束会调用此方法，将完成的Request移除队列。
 */

public class WorkStation {

    //在同时刻一秒内最多只能够开启60个线程进行网络请求（定义最多请求数）
    private static final int MAX_REQUEST_SIZE = 60;

    //存储正在执行的网络请求
    private Deque<MultiThreadRequest> mRunning = new ArrayDeque<>();

    //存储待执行的网络请求
    private Deque<MultiThreadRequest> mCache = new ArrayDeque<>();

    //通过HttpRequestProvider来创建相对应的HttpRequest（原生的HttpRequest或者是OKHttpRequest）
    private HttpRequestProvider mRequestProvider;


    public WorkStation() {
        this.mRequestProvider = new HttpRequestProvider();
    }


    private static final ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
            new ThreadFactory() {

                private AtomicInteger index = new AtomicInteger();

                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r);
                    //给线程指定名字
                    thread.setName("http thread name is " + index.getAndIncrement());
                    return thread;
                }
            });

    /**
     * function:将网络请求任务添加到队列中（由外部调用
     *
     * @param request
     */
    public void add(MultiThreadRequest request) {

        //（只允许同一时刻最多只能够有60个线程进行网络请求）
        if (mRunning.size() > MAX_REQUEST_SIZE) {
            //如果正在运行的数目已经大于60了，那么我们需要把请求对象放入到mCache缓存队列中
            mCache.add(request);
        } else {
            mRunning.add(request);
            doHttpRequest(request);
        }
    }

    /**
     * function:将当前将要执行网络请求的线程（一个请求任务）放入到线程池中并且去执行
     *
     * @param request
     */
    private void doHttpRequest(MultiThreadRequest request) {

        HttpRequest httpRequest = null;
        try {
            httpRequest = mRequestProvider.getHttpRequest(URI.create(request.getUrl()), request.getMethod());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sThreadPool.execute(new HttpRunnable(httpRequest, request, this));
    }


    /**
     * function:移除执行网络请求的线程（在什么时候调用？--在我们整个网络请求结束的地方调用）
     *
     * @param request
     */
    public void finish(MultiThreadRequest request) {

        //把执行网络请求的线程从运行队列中给移除掉
        mRunning.remove(request);

        //这里需要进行判断，因为你移除了一个请求任务，缓存队列中可能还会有一些请求任务在排队，所以需要进行判断缓存队列中的请求任务是否可以进入到运行队列中去。
        //若运行队列中的线程数已经超过了极限，则不能进入。
        if (mRunning.size() > MAX_REQUEST_SIZE) {
            return;
        }

        //如果缓存队列中没有请求任务，则无需添加到运行队列中去。
        if (mCache.size() == 0) {
            return;
        }

        //当我们的缓存列表有请求任务时，并且当前时刻在运行的线程总数也没有达到极限，则将遍历缓存列表，将那些在缓存列表中的线程添加到运行列表当中
        Iterator<MultiThreadRequest> iterator = mCache.iterator();

        while (iterator.hasNext()) {
            MultiThreadRequest next = iterator.next();
            mRunning.add(next);
            //将刚刚添加到运行列表中的请求任务从缓存列表中删掉
            iterator.remove();
            doHttpRequest(next);
        }
    }
}
