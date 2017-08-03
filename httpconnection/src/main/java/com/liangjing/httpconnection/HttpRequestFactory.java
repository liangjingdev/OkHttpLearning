package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpMethod;
import com.liangjing.httpconnection.http.HttpRequest;

import java.io.IOException;
import java.net.URI;

/**
 * function:利用了设计模式--工厂模式--相当于工厂模式中的工厂模式中的Creator
 * (httpRequest创建)--无论是生产原生的originHttp网络请求实例还是okHttp网络请求实例，它们的工厂类Factory都需要实现该接口。然后实现该接口中所定义的方法。
 * 因为它们的工厂类都是同样可以通过实现该接口的方法进行创建相对应的实例的。
 * <p>
 * 思路：如果想要做这样的一个框架的话，实际上也就是做一些相关接口的设计，也就是在你编写代码的最开始时就需要通过
 * 一些设计模式（工厂模式或者模版模式）将这些相关接口给预留出来，也就是对相应的接口进行封装。
 */

public interface HttpRequestFactory {

    //需要传入一些相关参数，URL、方法.
    HttpRequest createHttpRequest(URI uri, HttpMethod method) throws IOException;

}
