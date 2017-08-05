package com.liangjing.httpconnection.services.convert;

import com.liangjing.httpconnection.http.HttpResponse;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by liangjing on 2017/8/5.
 * <p>
 * function:解析数据--底层接口封装（在对服务器返回来的数据进行解析的过程运用到的设计模式--包装模式）
 */

public interface Convert {

    /**
     * function:（根据当前服务器返回的数据解析成相对应的实体类对象）第二个参数利用到反射知识--它将泛型 T 转成.class
     * @param response
     * @param type
     * @return
     * @throws IOException
     */
    Object parse(HttpResponse response, Type type) throws IOException;


    /**
     * function:判断是否可以对服务器返回的数据类型进行相对应的解析
     * @param contentType
     * @return
     */
    boolean isCanParse(String contentType);


    /**
     * function:根据当前服务器返回的数据（String类型）解析成相对应的实体类对象
     * @param content
     * @param type
     * @return
     * @throws IOException
     */
    Object parse(String content, Type type) throws IOException;
}
