package com.liangjing.httpconnection.http;

import java.util.Map;

/**
 * Created by liangjing on 2017/8/3.
 * function:所定义的这些方法都是用来设置和获取http请求头或者响应头的。
 * 为什么继承Map?--因为我们的http请求头以及响应头都是key-value键值对的形式的
 * (请求头-底层接口封装)--为什么要做底层封装？--便于以后进行扩展
 */

public interface NameValueMap<K, V> extends Map<K, V> {

    //根据name获取header的value
    String get(String name);

    //设置请求头或者响应头
    void set(String name, String value);

    //设置请求头或者响应头
    void setAll(Map<String, String> map);
}
