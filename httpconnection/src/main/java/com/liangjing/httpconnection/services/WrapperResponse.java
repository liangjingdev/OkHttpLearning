package com.liangjing.httpconnection.services;

import com.liangjing.httpconnection.services.convert.Convert;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by liangjing on 2017/8/5.
 * <p>
 * function:封装好类型转换后(Convert,JsonConvert)，需要将此结合到网络请求中，
 * 再对MultiThreadResponse做一个上层封装，相当于一层过滤，将获取到的响应数据通过类型转换后再返回。
 * WrapperResponse 继承于MultiThreadResponse，实现其抽象方法success，在成功响应方法中对数据进行解析以及转换操作。
 * <p>
 * 为什么要将这里的泛型T指定为String类型？
 * --因为在HttpRunnable中回调MoocResponse接口的时候传出来的是String类型的Data数据(HttpRunnable)，
 * 然后WrapperResponse继承该抽象类，所以需要指定泛型为String,因为此时MultiThreadResponse的泛型已定。
 * 然后实现接口内的方法（Success/fail）,之后再修改success回调方法，在其中进行解析数据并转换为对应的实体类对象
 */

public class WrapperResponse extends MultiThreadResponse<String> {

    private MultiThreadResponse mMultiResponse;

    private List<Convert> mConvert;

    public WrapperResponse(MultiThreadResponse mResponse, List<Convert> mConvert) {
        this.mMultiResponse = mResponse;
        this.mConvert = mConvert;
    }


    /**
     * function:应在此处进行扩展(成功的回调中)，因为之前返回的数据是个String类型（HttpRunnable中可得知），
     * 所以在此处将要对其进行处理将其解析为JSon数据,利用JsonConvert--中的第二个与其相对应的解析方法）
     *
     * @param request
     * @param data
     */
    @Override
    public void success(MultiThreadRequest request, String data) {

        for (Convert convert : mConvert) {
            //contentType在HttpRunnable处通过response获取得到
            if (convert.isCanParse(request.getContentType())) {
                try {
                    Object object = convert.parse(data, getType());
                    //回调接口，此时在请求成功的回调中，返回的是解析完成后所相对应的实体类对象。--将该实体类对象传出去
                    mMultiResponse.success(request, object);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    /**
     * function:需要获取业务层创建MoocResponse对象时传递进来的泛型信息（.class）
     * --才能够得知要转化为哪一个实体类的对象
     *
     * @return
     */
    private Type getType() {
        //getGenericSuperclass() 通过反射获取当前类表示的实体（类，接口，基本类型或void）的直接父类的Type
        Type type = mMultiResponse.getClass().getGenericSuperclass();
        //Type[] params = ((ParameterizedType) type).getActualTypeArguments();这行代码的意思是，
        // 如果支持泛型，返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class，因为可能有多个，所以是数组。
        Type[] paramType = ((ParameterizedType) type).getActualTypeArguments();
        return paramType[0];
    }


    @Override
    public void fail(int errorCode, String errorMessage) {
    }
}
