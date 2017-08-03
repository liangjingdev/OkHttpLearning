package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpHeader;
import com.liangjing.httpconnection.http.HttpRequest;
import com.liangjing.httpconnection.http.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by liangjing on 2017/8/3.
 * function:对Http请求进行封装（中层封装）
 * 该抽象类主要是对Http请求多做了一层判断，相当于一个过滤网。
 * 为了处理压缩这种情况，该抽象类实现了HttpRequest接口中的getBody()、execute(),Header接口中的getHeaders()，
 * 做了一些共性的预处理操作，同时为具体实现的子类留出了 getBodyOutputStream()、executeInternal()抽象方法。
 */

public abstract class AbstractHttpRequest implements HttpRequest {

    //压缩标志
    private static final String GZIP = "gzip";

    //请求头
    private HttpHeader mHeader = new HttpHeader();

    //输出压缩流
    private ZipOutputStream mZip;

    //默认为false
    private boolean executed;


    /**
     * function:获取请求头
     *
     * @return
     */
    @Override
    public HttpHeader getHeaders() {
        return mHeader;
    }


    /**
     * function:同样为了考虑压缩情况，在实现父类的方法中需要判断当前请求过程中是否支持Zip压缩，
     * 根据判断结果来决定是否需要进一步处理输出流。（获取输出流）
     * 最外层可以利用输出流来配置参数(write)，然后在发送post请求的时候带过去
     *
     * @return
     */
    @Override
    public OutputStream getBody() {
        OutputStream body = getBodyOutputStream();
        if (isGzip()) {
            return getGzipOutStream(body);
        }
        return body;
    }

    /**
     * functions：将输出压缩流作相应的处理再返回
     *
     * @return
     */
    private OutputStream getGzipOutStream(OutputStream body) {
        if (this.mZip == null) {
            this.mZip = new ZipOutputStream(body);
        }
        return mZip;
    }


    /**
     * function:判断是否为输出压缩流，若是则进行处理
     *
     * @return
     */
    private boolean isGzip() {
        String contentEncoding = getHeaders().getContentEncoding();
        if (GZIP.equals(contentEncoding)) {
            return true;
        }
        return false;
    }

    /**
     * function:执行请求操作，获取HttpResponse实例对象
     *
     * @return
     * @throws IOException
     */
    @Override
    public HttpResponse execute() throws IOException {
        //首先判断输出压缩流是否为空，若不为空，则先让它关闭一下
        if (mZip != null) {
            mZip.close();
        }
        HttpResponse response = executeInternal(mHeader);
        executed = true;
        return response;
    }

    //获取HttpResponse实例
    protected abstract HttpResponse executeInternal(HttpHeader mHeader) throws IOException;

    //获取输出流
    protected abstract OutputStream getBodyOutputStream();

}
