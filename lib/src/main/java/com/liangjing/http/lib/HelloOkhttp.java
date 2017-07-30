package com.liangjing.http.lib;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/29.
 * okhttp--实现简单的网络请求
 */

public class HelloOkhttp {

    public static void main(String args[]) {
        //1、创建OkHttpClient的对象
        OkHttpClient client = new OkHttpClient();
        //2、创建Request请求的对象
        Request request = new Request.Builder()
                .url("http://www.imooc.com")
                .build();
        try {
            //3、执行网络请求(返回一个Response对象)
            Response response = client.newCall(request).execute();
            //4、需要判断请求是否是可用的
            if (response.isSuccessful()){
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
