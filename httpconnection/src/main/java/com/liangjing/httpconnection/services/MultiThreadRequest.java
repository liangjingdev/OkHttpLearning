package com.liangjing.httpconnection.services;

import com.liangjing.httpconnection.http.HttpMethod;

/**
 * Created by liangjing on 2017/8/5.
 * <p>
 * function:（多线程处理请求--业务层请求的对象）
 * 由于之前扩展了对原生Http请求(HttpRequestConnection)的支持，而这种请求方式并没有支持多线程去处理网络请求，
 * 所以现在需要去解决如何让这些网络请求能够在异步的线程当中去完成。
 * <p>
 * 首先在构建网络请求队列的之前，我们需要去定义一些业务层的接口，这些接口是用于上层业务人员去调用可能需要的。对于外层的业务人员来说，他们所关心的几个很关键的方法就是
 * 成功和失败，并不关心某个网络请求是通过何种方式去发送请求的(http或者socket等等)。
 * <p>
 * 因为在处理多线程请求时，不可能无限制的创建多个线程来处理，而一个队列中存储的是一个个网络请求对象。
 * 所以我们应该还需要定义一个业务层网络请求的对象，存储着请求Url、请求方式、数据等相关信息，再提供对应的get、set方法。
 * MultiThreadRequest就是业务层请求的对象（业务层多线程分发处理）。
 */

public class MultiThreadRequest {

    //下面定义一些必须要的成员变量
    private String mUrl;

    private HttpMethod mMethod;

    //请求数据(请求参数)
    private byte[] mData;

    private MultiThreadResponse mResponse;

    //服务器返回的内容类型
    private String mContentType;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public HttpMethod getMethod() {
        return mMethod;
    }

    public void setMethod(HttpMethod mMethod) {
        this.mMethod = mMethod;
    }

    public byte[] getData() {
        return mData;
    }

    public void setData(byte[] mData) {
        this.mData = mData;
    }

    public MultiThreadResponse getResponse() {
        return mResponse;
    }

    public void setResponse(MultiThreadResponse mResponse) {
        this.mResponse = mResponse;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(String mContentType) {
        this.mContentType = mContentType;
    }
}
