package com.liangjing.http.lib;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/30.
 * 功能：okHttp实现缓存功能测试类
 */

public class CacheHttp {

    public static void main(String args[]) throws IOException {

        //定义缓存的大小
        int maxCacheSize = 10 * 1024 * 1024;
        //第一个参数为缓存的目录，第二个参数为缓存的大小
        Cache cache = new Cache(new File("C:\\Users\\asus\\Desktop\\008"), maxCacheSize);
        //缓存能力(不进行缓存)
        CacheControl control = new CacheControl.Builder().noCache().build();

        /**
         *  缓存能力(如果你的缓存是过期的，，为了减少流量，那么像这样子指定就是强制使用过期的缓存)--一年
         *   CacheControl cacheControl = new CacheControl.Builder().maxStale(365, TimeUnit.DAYS).build();
         */

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        Request request = new Request.Builder()
                .url("http://www.qq.com") //该网站是支持缓存的
                .cacheControl(control) //在发起网络请求的时候也可以去指定缓存的能力(像这样子指定后那么在发送网络请求后将不会进行缓存)
                .build();
        Response response = client.newCall(request).execute();

        //注意：在我们对网络发起请求之后，我们还需要去读取整个网络请求所返回来的内容，否则的话还是会默认下次还是会发送网络请求，导致缓存失败。
        String body1 = response.body().string();
        //如果你这个请求是从网络上取回来的，那么response.networkResponse()返回的值则不为空。
        //如果你这个请求是从Cache中取回来的，那么response.networkResponse()返回的值为空。
        System.out.println("network response" + response.networkResponse());
        System.out.println("cache response" + response.cacheResponse());

        System.out.println("*********************************");

        Response response1 = client.newCall(request).execute();
        String body2 = response1.body().string();
        System.out.println("network response" + response1.networkResponse());
        System.out.println("cache response" + response1.cacheResponse());
    }
}
