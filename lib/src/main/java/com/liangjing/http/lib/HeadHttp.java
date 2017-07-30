package com.liangjing.http.lib;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/30.
 * 功能：使用请求头和响应头中的参数信息
 */

public class HeadHttp {

    public static void main(String args[]) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.imooc.com/")
                .addHeader("User-Agent", "from liangjing http")
                .addHeader("Accept","text/plain,text/html")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //headers就是我们响应头所定义的一个标识
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i) + ":" + headers.value(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
