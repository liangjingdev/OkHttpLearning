package com.liangjing.httpconnection.services;

import com.liangjing.httpconnection.http.HttpMethod;
import com.liangjing.httpconnection.services.convert.Convert;
import com.liangjing.httpconnection.services.convert.JsonConvert;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjing on 2017/8/5.
 * <p>
 * function ：定义一些供业务层调用的API接口（让外界进行访问）--最外层封装
 * 为了方便上层调用，需要在之前编写的基础上（MultiThreadRequest、MultiThreadResponse<T>、WorkStation、HttpRunnable）再封装一个专门提供接口API的一个类（方便，而且接口简洁明了），
 * 类似之前专门提供Request对象的HttpRequestProvider，此类名为HttpApiProvider
 */

public class MultiThreadProvider {

    //文件编码
    private static final String ENCODING = "UTF-8";

    private static WorkStation sWorkStation = new WorkStation();

    //可解析的数据类型的一个集合（比如说可以解析Json数据，或者Xml数据等等）
    // 所以这里就用Convert作为集合的泛型，然后比如说你还可以解析Xml数据的话，则可以创建一个类(用于解析Xml数据)来继承自Convert，即可达到扩展的效果
    private static final List<Convert> sConverts = new ArrayList<>();

    static {
        //添加可解析Json数据的操作
        sConverts.add(new JsonConvert());
    }

    /**
     * function:对Map<String,String>类型的请求参数进行URLEncode的编码的处理，返回一个字节数组（因为输出流的write()方法中是传入字节数组的，
     * 所以MultiThreadRequest的setData()方法要传入一个字节数组类型的请求参数）
     * 但是Map<String, String>类型能够让外层更加的方便进行定义请求参数，所以将其传进来的时候再进行处理转化为字节数组
     *
     * @return
     */
    public static byte[] encodeParam(Map<String, String> value) {

        if (value == null || value.size() == 0) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        int count = 0;
        for (Map.Entry<String, String> entry : value.entrySet()) {
            try {
                buffer.append(URLEncoder.encode(entry.getKey(), ENCODING)).append("=").
                        append(URLEncoder.encode(entry.getValue(), ENCODING));
                if (count != value.size() - 1) {
                    //将Post方式的网络请求所带有的一些参数进行组装，如：username=liangjing&userage=20
                    buffer.append("&");
                }
                count++;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString().getBytes();
    }


    /**
     * function:该方法(API接口)是供业务层调用的，是开启多线程网络请求任务的最终API接口
     * 传入的参数分别是：你要访问的url,你的数据、你当前网络请求的回调接口
     * <p>
     * （这里仅用一个API接口就可以获取到之前编写的那些与多线程网络请求相关的类中的必要的API接口中所需要传入的一些参数）
     * --对那些参数再次进行封装，仅需供一个接口给业务层调用.
     * <p>
     * 这里的MultiThreadResponse并不需要给它指定一个泛型，应该让业务层来对其进行指定。
     *
     * @param url
     * @param value
     * @param response
     */
    public static void carryOut(String url, Map<String, String> value, MultiThreadResponse response) {

        MultiThreadRequest request = new MultiThreadRequest();
        WrapperResponse wrapperResponse = new WrapperResponse(response, sConverts);
        request.setUrl(url);
        //默认情况下指定为HttpMethod.POST的请求方式
        request.setMethod(HttpMethod.POST);
        request.setData(encodeParam(value));
        request.setResponse(wrapperResponse);
        //添加请求任务
        sWorkStation.add(request);
    }

}
