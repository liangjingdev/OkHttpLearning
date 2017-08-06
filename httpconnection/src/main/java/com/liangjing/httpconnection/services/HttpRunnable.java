package com.liangjing.httpconnection.services;

import android.annotation.TargetApi;
import android.os.Build;

import com.liangjing.httpconnection.http.HttpRequest;
import com.liangjing.httpconnection.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by liangjing on 2017/8/5.
 * <p>
 * function:（多线程处理请求--处理操作层）
 * 业务层多线程分发处理：用于处理下载（请求）任务
 * <p>
 * 在专门用于处理多线程的控制和队列的管理类WorkStation中维护了一个线程池，用来执行网络请求，
 * 所以需要对应的Runnable来执行下载（请求）任务。
 * <p>
 * 成员变量有基本Http封装的接口HttpRequest、多线程请求的接口MultiThreadRequest、管理多线程和
 * 队列类WorkStation。
 * <p>
 * WorkStation主要用于run方法执行完后调用此对象中的方法，来移除队列中已执行完的request。
 * 将HttpRequest中的重要请求数据获取并封装到MultiThreadRequest中来执行请求。
 */

public class HttpRunnable implements Runnable {

    private HttpRequest mHttpRequest;

    private MultiThreadRequest mRequest;

    private WorkStation mWorkStation;

    public HttpRunnable(HttpRequest mHttpRequest, MultiThreadRequest mRequest, WorkStation mWorkStation) {
        this.mHttpRequest = mHttpRequest;
        this.mRequest = mRequest;
        this.mWorkStation = mWorkStation;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        try {
            //获取输出流来写入请求数据并传出去（请求数据由mRequest.getData()获取到，是一个字节数组，然后MoocRequest中的setData()方法是由外层来调用且设置数据的）
            mHttpRequest.getBody().write(mRequest.getData());
            //获取相应的HttpRequest所返回的HttpResponse对象
            HttpResponse response = mHttpRequest.execute();
            //获取服务器返回的内容类型
            String contentType = response.getHeaders().getContentType();
            //将服务器返回的内容类型保存到我们的业务层网络请求MultiThreadRequest中
            mRequest.setContentType(contentType);
            //判断response是否成功
            if (response.getStatus().isSuccess()) {
                //设置接口回调（业务层）
                if (mRequest.getResponse() != null) {
                    //把服务器返回的数据传递出去，传递出去的话我们需要得到它的一个流--getData()方法，然后再将其转化成String类型
                    mRequest.getResponse().success(mRequest, new String(getData(response), "UTF-8"));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //放在finally中，不管网络请求失败还是成功都将它移除掉
            mWorkStation.finish(mRequest);
        }

    }


    /**
     * function:获取服务器返回的数据的所对应的字节数组
     */
    private byte[] getData(HttpResponse response) {
        //创建一个支持写入字节类型数据的输出流来写入数据，并传出去（response.getContentLength()--获取响应数据的内容长度）
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) response.getContentLength());
        int len;
        byte[] data = new byte[512];
        try {
            //拿到HttpResponse所对应的输入流来读取响应数据（服务器返回的数据）并且通过先前创建好的输出流来写入，然后再传到其它地方去
            while ((len = response.getBody().read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
