package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpMethod;
import com.liangjing.httpconnection.http.HttpRequest;
import com.liangjing.httpconnection.utils.JudgeUtil;

import java.io.IOException;
import java.net.URI;

/**
 * Created by liangjing on 2017/8/2.
 * function:(httpRequest创建--这里进行了一层外包装--供给上层调用)--根据你选择的对应的工厂类进行创建相应的HttpRequest
 * 最后，供给上层调用的就是HttpRequestProvider 类，它可以根据不同的条件创建不同Http请求library，这样便实现多个library类库切换的条件判断使用。
 * 因为这里有两个工厂类分别用于创建不同的HttpRequest实例，所以这里需要再进行一次封装，让外界自己选择哪一个工厂类并且进行创建相对应的实例
 */

public class HttpRequestProvider {

    //根据判断okhttp3.OkHttpClient这个类是否存在来返回true or false,在加载HttpRequestProvider.class该类的时候就进行判断
    private static boolean OKHTTP_REQUEST = JudgeUtil.isExist("okhttp3.OkHttpClient", HttpRequestProvider.class.getClassLoader());

    //外界所选择的工厂类
    private HttpRequestFactory mHttpRequestFactory;

    //用于判断用户选择的是哪一个工厂类
    public HttpRequestProvider() {
        if (OKHTTP_REQUEST) {
            mHttpRequestFactory = new OkHttpRequestFactory();
        } else {
            mHttpRequestFactory = new OriginHttpRequestFactory();
        }
    }

    /**
     * function:将该方法暴露给外界，让外界进行创建相对应的对象
     *
     * @param uri
     * @param httpMethod
     * @return
     * @throws IOException
     */
    public HttpRequest getHttpRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return mHttpRequestFactory.createHttpRequest(uri, httpMethod);
    }


    /**
     * function:获取当前所选择的工厂类对象
     *
     * @return
     */
    public HttpRequestFactory getHttpRequestFactory() {
        return mHttpRequestFactory;
    }


    /**
     * function:该方法的目的是让外界自己设置使用哪一个工厂类。灵活性更强
     *
     * @param httpRequestFactory
     */
    public void setHttpRequestFactory(HttpRequestFactory httpRequestFactory) {
        this.mHttpRequestFactory = httpRequestFactory;
    }
}
