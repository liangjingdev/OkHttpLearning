package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpHeader;
import com.liangjing.httpconnection.http.HttpMethod;
import com.liangjing.httpconnection.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/8/2.
 * function:okhttp3网络请求--实现类（实现一些之前经封装而未实现的方法--executeInternal(HttpHeader header, byte[] data)
 */

public class OkHttpRequest extends BufferHttpRequest {

    private OkHttpClient mClient;

    private HttpMethod mMethod;

    private String mUrl;

    public OkHttpRequest(OkHttpClient client, HttpMethod method, String url) {
        this.mClient = client;
        this.mMethod = method;
        this.mUrl = url;
    }


    /**
     * function:获取请求方法
     *
     * @return
     */
    @Override
    public HttpMethod getMethod() {
        return mMethod;
    }

    /**
     * function:获取网络请求所对应的URI
     *
     * @return
     */
    @Override
    public URI getUri() {
        return URI.create(mUrl);
    }

    /**
     * function:获取okhttp网络请求所对应的Response
     * @param header
     * @param data
     * @return
     * @throws IOException
     */
    @Override
    protected HttpResponse executeInternal(HttpHeader header, byte[] data) throws IOException {
        //首先判断请求方法是否为post。因为post请求有请求体(requestBody)，而get方法没有请求体。
        // 如果有请求体则还需要创建RequestBody实例。
        boolean isBody = mMethod == HttpMethod.POST;
        RequestBody requestBody = null;
        if (isBody) {
            requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), data);
        }
        //接着创建Request.Builder实例
        Request.Builder builder = new Request.Builder().url(mUrl).method(mMethod.name(), requestBody);

        //接着对header(请求头)进行处理，循环该参数将所有请求头封装至Request.Builder--添加请求头信息
        //Set entrySet()：返回Map中包含的key-value对所组成的Set集合，每个集合元素都是Map.Entry(Entry是Map的内部类)对象
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        //最後获取Response实例
        Response response = mClient.newCall(builder.build()).execute();

        return new OkHttpResponse(response);
    }
}
