package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpMethod;
import com.liangjing.httpconnection.http.HttpRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Created by liangjing on 2017/8/3.
 * function:(httpRequest创建--“生产”HttpRequest对象的具体实现类)实现类继承接口来“生产”Product，此实现类相当于工厂模式中的ConcreteCreator
 * --用于生产原生HttpRequest实例的工厂类（实现底层封装的接口方法来创建）
 * --工厂类是用于执行生产的一些具体操作的一个类，还可以在工厂类中定义一些创建实例时能够用到的API，让外界创建实例时可以选择性进行设置
 */

public class OriginHttpRequestFactory implements HttpRequestFactory {

    public HttpURLConnection mConnection;

    public OriginHttpRequestFactory() {

    }

    public void setReadTimeOut(int readTimeOut) {
        mConnection.setReadTimeout(readTimeOut);
    }

    public void setConnectionTimeOut(int connectionTimeOut){
        mConnection.setConnectTimeout(connectionTimeOut);
    }


    /**
     * function:创建原生的HttpRequest
     * @param uri
     * @param method
     * @return
     * @throws IOException
     */
    @Override
    public HttpRequest createHttpRequest(URI uri, HttpMethod method) throws IOException {

        mConnection = (HttpURLConnection) uri.toURL().openConnection();
        return new OriginHttpRequest(uri.toString(),mConnection,method);
    }
}
