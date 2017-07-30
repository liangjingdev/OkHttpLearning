package com.liangjing.http.lib;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/30.
 * 功能:发送get请求（附加参数信息）去查询相关的数据
 *      （如果你想去使用这种get请求并且传递参数的话，那么就需要在你所创建的url当中去构建相应的参数--使用HttpUrl）
 */

public class QueryHttp {

    public static void main(String args[]) {

        OkHttpClient okHttpClient = new OkHttpClient();

        //HttpUrl是OkHttp提供的一个工具类
        HttpUrl httpUrl = HttpUrl.parse("https://api.heweather.com/x3/weather")
                .newBuilder()
                .addQueryParameter("city", "beijing") //添加参数信息.因为这里的参数信息并没有特殊字符(如中文)，所以不需要用到addEncoded..方法.
                .addQueryParameter("key","d17ce22ec5404ed883elcfcaca0ecaa7") //如果有特殊字符，那么要用addEncodedQueryParameter()方法
                .build();

        String url = httpUrl.toString();
        //打印网络请求路径
        System.out.println(httpUrl.toString());
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                //打印返回的数据
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
