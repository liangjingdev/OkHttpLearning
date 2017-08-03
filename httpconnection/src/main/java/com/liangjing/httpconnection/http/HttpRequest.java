package com.liangjing.httpconnection.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * Created by liangjing on 2017/8/2.
 * function: 对Http请求进行封装(底层接口封装)--无论是原生的Http请求还是oKHttp请求，最终都要继承该接口--便于以后扩展
 * 封装实现的过程顺序：接口、枚举 –> 抽象类 –>实现类
 * （同时也是httpRequest创建工厂类的产品---Product）
 */

public interface HttpRequest extends Header {

    //获取请求方法
    HttpMethod getMethod();

    //获取Uri
    URI getUri();

    //获取输出流
    OutputStream getBody();

    //执行请求
    HttpResponse execute() throws IOException;

}
