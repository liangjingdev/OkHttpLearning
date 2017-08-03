package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpMethod;
import com.liangjing.httpconnection.http.HttpRequest;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by liangjing on 2017/8/2.
 * function:(httpRequest创建--“生产”HttpRequest对象的具体实现类)实现类继承接口来“生产”Product，此实现类相当于工厂模式中的ConcreteCreator
 * --用于生产OkHttpRequest实例的工厂类（实现底层封装的接口方法来创建）
 * --工厂类是用于执行生产的一些具体操作的一个类，还可以在工厂类中定义一些创建实例时能够用到的API，让外界创建实例时可以选择性进行设置
 */

public class OkHttpRequestFactory implements HttpRequestFactory {

    private OkHttpClient mClient;

    public OkHttpRequestFactory() {
        //对变量进行赋值
        this.mClient = new OkHttpClient();
    }

    /**
     * function:重构constructor方法，使得外部也可以传入OkHttpClient对象,增加灵活性
     *
     * @param client
     */
    public OkHttpRequestFactory(OkHttpClient client) {
        this.mClient = client;
    }

    /**
     * 注意：如果需要手动设置或修改缓存处理，即超时处理，使用以下方式：通过builder对象设置缓存及超时的处理
     * 目的：目的是将这些方法进行封装，让外层创建对象时可以利用，如果需要要扩展某些方法，可在此处进行创建即可
     * function:设置读取时间超时
     *
     * @param readTimeOut
     */
    public void setReadTimeOut(int readTimeOut) {
        this.mClient = mClient.newBuilder()
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .build();
    }

    /**
     * function:设置写入数据，上传数据超时时间
     *
     * @param writeTimeOut
     */
    public void setWriteTimeOut(int writeTimeOut) {
        this.mClient = mClient.newBuilder()
                .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
                .build();
    }


    /**
     * function:设置连接超时
     *
     * @param connectionTimeOut
     */
    public void setConnectionTimeOut(int connectionTimeOut) {
        this.mClient = mClient.newBuilder()
                .connectTimeout(connectionTimeOut, TimeUnit.SECONDS)
                .build();
    }


    /**
     * function：实现okHttpRequest对象创建的一个过程
     *
     * @param uri
     * @param method
     * @return
     */
    @Override
    public HttpRequest createHttpRequest(URI uri, HttpMethod method) {
        return new OkHttpRequest(mClient, method, uri.toString());
    }

}
