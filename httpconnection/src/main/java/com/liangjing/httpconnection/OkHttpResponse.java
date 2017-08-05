package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpHeader;
import com.liangjing.httpconnection.http.HttpStatus;

import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by liangjing on 2017/8/2.
 * function:HttpResponse--实现类（OkHttpResponse）
 */

public class OkHttpResponse extends AbstractHttpResponse {

    private Response mResponse;

    private HttpHeader mHeaders;

    public OkHttpResponse(Response response) {
        this.mResponse = response;
    }

    /**
     * function: 获取body相对应的输入流
     */
    @Override
    protected InputStream getBodyInternal() {
        return mResponse.body().byteStream();
    }

    /**
     * function:关闭输入流
     */
    @Override
    protected void closeInternal() {
        mResponse.body().close();
    }

    /**
     * function:获取响应状态码
     */
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.getValues(mResponse.code());
    }

    /**
     * function:获取响应状态对应信息
     */
    @Override
    public String getStatusMsg() {
        return mResponse.message();
    }

    /**
     * function:获取内容字节长度
     */
    @Override
    public long getContentLength() {
        return mResponse.body().contentLength();
    }

    /**
     * function:可通过HttpResponse实例来获取响应头
     */
    @Override
    public HttpHeader getHeaders() {
        if (mHeaders == null) {
            mHeaders = new HttpHeader();
        }

        for (String name : mResponse.headers().names()) {
            mHeaders.set(name, mResponse.headers().get(name));
        }
        return mHeaders;
    }
}
