package com.liangjing.httpconnection.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liangjing on 2017/8/2.
 * function ：对HttpResponse进行封装(底层接口封装)--封装实现的过程顺序接口、枚举 –> 抽象类 –>实现类
 * 对网络请求的响应进行封装实际上也就是要将response的状态、response的状态message、response以及response的输入流
 * 这些内容进行封装--必须要有获取状态码getStatus()方法、获取Body输入流getBody()方法、关闭输入流close()方法。
 * <p>
 * HttpResponse 接口定义好之后，可以定义实现类来实现，可是在此之前还需要定义一个抽象类AbstractHttpResponse，
 * 由它来实现HttpResponse接口。抽象类可以拥有自己的成员变量和已实现的方法，比接口的功能更加丰富。
 * 在封装框架过程中，可借由抽象类来实现一些内部的方法，更易扩展
 */

public interface HttpResponse extends Header, Closeable {

    //获取状态码getStatus()方法
    HttpStatus getStatus();

    //获取状态码代表信息
    String getStatusMsg();

    //获取Body输入流getBody()方法
    InputStream getBody() throws IOException;

    //关闭输入流close()方法
    void close();

    //获取内容字节长度
    long getContentLength();

}
