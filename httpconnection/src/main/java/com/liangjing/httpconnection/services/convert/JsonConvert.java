package com.liangjing.httpconnection.services.convert;

import com.google.gson.Gson;
import com.liangjing.httpconnection.http.HttpResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Created by liangjing on 2017/8/5.
 * <p>
 * function:（解析数据--转换实现类JsonConvert）--解析服务器返回来的Json数据
 */

public class JsonConvert implements Convert {

    private Gson gson = new Gson();

    //支持Json解析的标志
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    public JsonConvert() {
    }

    /**
     * function:将服务器返回的数据转化成所相对应的实体对象（什么样的实体对象有Type决定--Type
     *          在 WrapperResponse中进行指定，故这里直接返回一个Object类型的对象即可）
     *
     * @param response
     * @param type
     * @return
     * @throws IOException
     */
    @Override
    public Object parse(HttpResponse response, Type type) throws IOException {

        Reader reader = new InputStreamReader(response.getBody());
        return gson.fromJson(reader,type);
    }


    /**
     * function:判断是否可以对该类型数据(服务器返回的数据的类型)进行解析,在这里即该数据类型是否支持Json解析
     * @param contentType
     * @return
     */
    @Override
    public boolean isCanParse(String contentType) {
        return CONTENT_TYPE.equals(contentType);
    }

    /**
     * function:可将String类型的数据转化为相对应的实体类的对象
     * @param content
     * @param type
     * @return
     * @throws IOException
     */
    @Override
    public Object parse(String content, Type type) throws IOException {

        return gson.fromJson(content,type);
    }
}
