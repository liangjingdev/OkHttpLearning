package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;


/**
 * Created by liangjing on 2017/8/3.
 * function:对HttpResponse进行封装(上层封装)
 * 该抽象类主要是对响应数据多做了一层判断，相当于一个过滤网。
 * 为了处理数据被压缩了这种情况，该抽象类实现了HttpResponse接口中的getBody()、close()方法
 * 做了一些共性的预处理操作，同时为具体实现的子类留出了getBodyInternal()、closeInternal()抽象方法。
 */

public abstract class AbstractHttpResponse implements HttpResponse {

    //压缩标志
    private static final String GZIP = "gzip";

    //经处理后的压缩输入流
    private InputStream mGizpInputStream;

    /**
     * function:首先由isGzip()进行判断是否为压缩数据，若是则需要对数据流进行处理之后再作返回。若否则直接返回输入流
     *
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getBody() throws IOException {

        InputStream body = getBodyInternal();
        if (isGzip()) {
            return getBodyGzip(body);
        }
        return body;
    }

    /**
     * function:有压缩的数据流进行一个处理，然后再作返回
     *
     * @param body
     * @return
     */
    private InputStream getBodyGzip(InputStream body) throws IOException {
        if (mGizpInputStream == null) {
            this.mGizpInputStream = new GZIPInputStream(body);
        }
        return mGizpInputStream;
    }


    /**
     * function:关闭流
     */
    @Override
    public void close() {
        if (mGizpInputStream != null) {
            try {
                mGizpInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //关闭输入流
        closeInternal();
    }

    /**
     * function:判断数据是否为压缩数据，因为有些响应头中可能有压缩的标志
     */
    private boolean isGzip() {
        String contentEncoding = getHeaders().getContentEncoding();

        if (AbstractHttpResponse.GZIP.equals(contentEncoding)) {
            return true;
        }

        return false;
    }

    //关闭输入流
    protected abstract void closeInternal();

    //获取输入流
    protected abstract InputStream getBodyInternal();
}
