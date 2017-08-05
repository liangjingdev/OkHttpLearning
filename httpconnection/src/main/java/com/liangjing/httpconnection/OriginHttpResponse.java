package com.liangjing.httpconnection;

import com.liangjing.httpconnection.http.HttpHeader;
import com.liangjing.httpconnection.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjing on 2017/8/3.
 * function:HttpResponse--实现类(原生HttpResponse)
 */

public class OriginHttpResponse extends AbstractHttpResponse {

    //需要一个HttpURLConnection实例
    private HttpURLConnection mConnection;

    //从构造方法中传入HttpURLConnection实例
    public OriginHttpResponse(HttpURLConnection httpURLConnection) {
        this.mConnection = httpURLConnection;
    }

    /**
     * function:获取响应头
     * @return
     */
    @Override
    public HttpHeader getHeaders() {
        HttpHeader header = new HttpHeader();

        //通过mConnection.getHeaderFields()得到的是一个Map<String,String>.Entry对象，然后通过调用它的entrySet进行foreach循环
        for (Map.Entry<String, List<String>> entry : mConnection.getHeaderFields().entrySet()) {
            header.set(entry.getKey(),entry.getValue().get(0));
        }
        return header;
    }

    @Override
    public HttpStatus getStatus() {
        try {
            return HttpStatus.getValues(mConnection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getStatusMsg() {
        try {
            return mConnection.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * function:断开连接--关闭输入流
     */
    @Override
    protected void closeInternal() {
        mConnection.disconnect();
    }


    /**
     * function:获取输入流
     * @return
     */
    @Override
    protected InputStream getBodyInternal() {
        try {
            return mConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * function:获取内容字节长度
     * @return
     */
    @Override
    public long getContentLength() {
        return mConnection.getContentLength();
    }
}
