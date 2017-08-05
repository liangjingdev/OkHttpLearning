package com.liangjing.httpconnection;

import com.liangjing.httpconnection.http.HttpHeader;
import com.liangjing.httpconnection.http.HttpMethod;
import com.liangjing.httpconnection.http.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

/**
 * Created by liangjing on 2017/8/3.
 * 原生http网络请求--实现类
 */

public class OriginHttpRequest extends BufferHttpRequest {

    private String mUrl;
    private HttpURLConnection mConnection;
    private HttpMethod mMethod;

    public OriginHttpRequest(String mUrl, HttpURLConnection mConnection, HttpMethod mMethod) {
        this.mUrl = mUrl;
        this.mConnection = mConnection;
        this.mMethod = mMethod;
    }

    @Override
    public HttpMethod getMethod() {
        return mMethod;
    }

    @Override
    public URI getUri() {
        return URI.create(mUrl);
    }

    /**
     * function:(执行请求)获取原生Http网络请求所返回的Response
     *
     * @param header
     * @param data
     * @return
     * @throws IOException
     */
    @Override
    protected HttpResponse executeInternal(HttpHeader header, byte[] data) throws IOException {

        for (Map.Entry<String, String> entry : header.entrySet()) {
            mConnection.addRequestProperty(entry.getKey(), entry.getValue());
        }

        mConnection.setDoOutput(true);
        mConnection.setDoInput(true);
        mConnection.setRequestMethod(mMethod.name());
        mConnection.connect();
        if (data != null && data.length > 0) {
            //首先获取到输出流的实例
            OutputStream out = mConnection.getOutputStream();
            //然后进行写操作
            out.write(data, 0, data.length);
            //最後记得关闭输出流
            out.close();
        }
        OriginHttpResponse response = new OriginHttpResponse(mConnection);
        return response;
    }
}
