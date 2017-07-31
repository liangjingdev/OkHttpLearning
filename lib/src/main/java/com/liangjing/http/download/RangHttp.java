package com.liangjing.http.download;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/31.
 * 功能：多线程下载文件 --认识range、content-length等关键字段
 */

public class RangHttp {

    public static void main(String args[]) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://img.mukewang.com/597948540001f00407500250.jpg")
                .addHeader("Accept-Encoding","identity") //作用：若服务器能够支持content-length的处理结果，那么就会预先把content-length给返回出来，若不支持则就会采取回默认的编码方式(chunked)
                .addHeader("Range","bytes=0-2")//通过Range字段可以拿到相应的一部分数据(如果Range范围是bytes=0-2，那么返回的content-length字段内容是3,此时响应头有Content-Range : bytes 0-2/132714)
                .build();

        try {
            Response response = client.newCall(request).execute();

            //每个response都有一个body,body中都有一个content-length.如果访问该网址返回来的响应头中的Transfer-Encoding字段的内容是chunked
            //那么该网址的响应头中是没有content-length字段的，此时response.body().contentLength()返回的值是-1(一般图片或者静态网页都有content-length字段)
            System.out.println("content-length : " + response.body().contentLength());

            if (response.isSuccessful()) {
                //1、首先我们需要去拿到响应头，然后需要去遍历Headers(响应头里面所包含的所有字段及信息)，接下来看下是否有我们需要的信息(content-length)然后再去将其取出来
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i) + " : " + headers.value(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
