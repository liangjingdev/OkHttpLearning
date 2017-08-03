package com.liangjing.httpconnection.http;

/**
 * Created by liangjing on 2017/8/3.
 *  function: 对网络请求的响应(HttpResponse)或者网络请求(HttpRequest)进行封装(最底层接口封装)
 *  目的：用于让HttpResponse或者HttpRequest对其进行继承。
 *       接着实现HttpResponse接口或者HttpRequest接口的类就可以实现以下方法直接获取到请求头或者响应头了
 */

public interface Header {

    HttpHeader getHeaders();
}
