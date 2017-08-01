package com.liangjing.http.download;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by liangjing on 2017/8/1.
 * function:测试多线程当中线程池、队列的操作
 */

public class ThreadPoolTest1 {

    public static void main(String args[]) {

        //队列
        ArrayBlockingQueue queue = new ArrayBlockingQueue<Runnable>(10);

        //线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 60,TimeUnit.SECONDS, queue);

        for (int i = 0; i < 5; i++) {

            final int index = i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("index" + index + "queue" + queue.size());
                }
            });
        }
    }
}
