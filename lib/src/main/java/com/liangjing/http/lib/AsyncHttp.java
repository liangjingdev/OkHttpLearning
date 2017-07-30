package com.liangjing.http.lib;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/30.
 * 功能：同步请求&&异步请求
 */

public class AsyncHttp {

    public static void main(String args[]) {

        sendAsyncRequest("http://www.imooc.com");
    }

    /**
     * function:同步请求
     *
     * @param url
     */
    public static void sendRequest(String url) {
        //1、创建OkHttpClient的对象
        OkHttpClient client = new OkHttpClient();
        //2、创建Request请求的对象
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            //3、执行网络请求(返回一个Response对象)
            Response response = client.newCall(request).execute();
            //4、需要判断请求是否是可用的
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function:异步请求
     *
     * @param url
     */
    public static void sendAsyncRequest(String url) {

        System.out.println(Thread.currentThread().getId());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //判断与之前打印出来的线程的id是否一样
                    System.out.println(Thread.currentThread().getId());
                }
            }
        });
    }
}
