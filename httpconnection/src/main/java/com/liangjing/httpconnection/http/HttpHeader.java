package com.liangjing.httpconnection.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by liangjing on 2017/8/2.
 * function: 实现对http请求头或者响应头的包装以及访问
 */

public class HttpHeader implements NameValueMap<String, String> {

    //定义一些常见的请求头以及响应头的name
    public final static String ACCEPT = "Accept";
    public final static String PRAGMA = "Pragma";
    public final static String PROXY_CONNECTION = "Proxy-Connection";
    public final static String USER_AGENT = "User-Agent";
    public final static String ACCEPT_ENCODING = "accept-encoding";
    public final static String CACHE_CONTROL = "Cache-Control";
    public final static String CONTENT_ENCODING = "Content-Encoding";
    public final static String CONNECTION = "Connection";//服务器的连接状态
    public final static String CONTENT_LENGTH = "Content-length";
    public static final String CONTENT_TYPE = "Content-Type";

    //mMap用来存放请求头或者响应头
    private Map<String, String> mMap = new HashMap<>();

    /**
     * function:通过name来从mMap中获取到相关的请求头或者响应头的value
     *
     * @param name
     * @return
     */
    @Override
    public String get(String name) {
        return mMap.get(name);
    }

    /**
     * function:将设置好的请求头或者响应头put进mMap中
     *
     * @param name
     * @param value
     */
    @Override
    public void set(String name, String value) {
        mMap.put(name, value);
    }


    public String getAccept() {
        return get(ACCEPT);
    }

    public void setAccept(String value) {
        set(ACCEPT, value);
    }

    public String getPragma() {
        return get(PRAGMA);
    }

    public void setPragma(String value) {
        set(PRAGMA, value);
    }

    public String getUserAgent() {
        return get(USER_AGENT);
    }

    public void setUserAgent(String value) {
        set(USER_AGENT, value);
    }

    public String getProxyConnection() {
        return get(PROXY_CONNECTION);
    }

    public void setProxyConnection(String value) {
        set(PROXY_CONNECTION, value);
    }

    public String getAcceptEncoding() {
        return get(ACCEPT_ENCODING);
    }

    public void setAcceptEncoding(String value) {
        set(ACCEPT_ENCODING, value);
    }

    public String getCacheControl() {
        return get(CACHE_CONTROL);
    }

    public void setCacheControl(String value) {
        set(CACHE_CONTROL, value);
    }

    public String getContentEncoding() {
        return get(CONTENT_ENCODING);
    }

    public void setContentEncoding(String value) {
        set(CONTENT_ENCODING, value);
    }

    public String getConnection() {
        return get(CONNECTION);
    }

    public void setConnection(String value) {
        set(CONNECTION, value);
    }

    public String getContentLength() {
        return get(CONTENT_LENGTH);
    }

    public void setContentLength(String value) {
        set(CONTENT_LENGTH, value);
    }


    public String getContentType() {
        return get(CONTENT_TYPE);
    }

    public void setContentType(String value) {
        set(CONTENT_TYPE, value);
    }


    /**
     * function:将设置好的请求头或者响应头put进mMap中
     *
     * @param map
     */
    @Override
    public void setAll(Map<String, String> map) {
        mMap.putAll(map);
    }

    @Override
    public int size() {
        return mMap.size();
    }

    @Override
    public boolean isEmpty() {
        return mMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return mMap.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return mMap.containsValue(o);
    }

    @Override
    public String get(Object o) {
        return mMap.get(o);
    }

    @Override
    public String put(String s, String s2) {
        return mMap.put(s, s2);
    }

    @Override
    public String remove(Object o) {
        return mMap.remove(o);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
        mMap.putAll(map);
    }

    @Override
    public void clear() {
        mMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return mMap.keySet();
    }

    @Override
    public Collection<String> values() {
        return mMap.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return mMap.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return mMap.equals(o);
    }

    @Override
    public int hashCode() {
        return mMap.hashCode();
    }
}
