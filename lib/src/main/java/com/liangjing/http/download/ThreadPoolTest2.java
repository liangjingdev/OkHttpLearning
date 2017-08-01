package com.liangjing.http.download;

/**
 * Created by liangjing on 2017/8/1.
 * function:测试多线程当中终止线程的方法
 */

public class ThreadPoolTest2 {

    public static void main(String args[]) {

        class MyRunnable implements Runnable {

            public volatile boolean flag = true;

            @Override
            public void run() {

                while (flag && !Thread.interrupted()) {
                    try {
                        System.out.println("running");
                        //让当前的线程休眠
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        MyRunnable runnable = new MyRunnable();
        //开启一个新的线程
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            //让当前的线程去休眠一下
            thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //终止线程
        runnable.flag = false;
        thread.interrupt();
    }
}
