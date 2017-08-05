package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpHeader;
import com.liangjing.httpconnection.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by liangjing on 2017/8/2.
 * function：请求流抽象处理BufferHttpRequest（Http请求--上层封装）
 *           将输出流转化为输出字节流
 */

public abstract class BufferHttpRequest extends AbstractHttpRequest {

    //内部维护一个成员变量：输出字节流ByteArrayOutputStream(将输出流处理为输出字节流并返回)
    private ByteArrayOutputStream mByArray = new ByteArrayOutputStream();

    /**
     * function:返回输出字节流
     */
    protected OutputStream getBodyOutputStream() {
        return mByArray;
    }

    /**
     * function:获取HttpResponse实例
     *
     * @param header
     * @return
     */
    protected HttpResponse executeInternal(HttpHeader header) throws IOException {
        //首先把成员变量内存数据转化为一个字节数组。成员变量即请求时传递参数的信息，再定义抽象方法将HttpHeader、字节数组两个参数传出。
        //相当于在原理基础上对输出流多做了一步处理：转换为字节数组类型。
        byte[] data = mByArray.toByteArray();
        return executeInternal(header, data);
    }

    protected abstract HttpResponse executeInternal(HttpHeader header, byte[] data) throws IOException;
}
