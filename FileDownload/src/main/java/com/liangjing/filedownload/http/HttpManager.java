package com.liangjing.filedownload.http;

import android.content.Context;

import com.liangjing.filedownload.file.FileStorageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/31.
 * function:接口实现后，接下来需要创建一个简单的网络请求类HttpManager(DownloadCallback网络请求接口的实现类)
 */

public class HttpManager {

    private static volatile HttpManager sManager = new HttpManager();

    //errorCode
    public static final int NETWORK_ERROR_CODE = 1;
    public static final int CONTENT_LENGTH_ERROR_CODE = 2;
    public static final int TASK_RUNNING_ERROR_CODE = 3;//表示当前下载任务正在执行了，不需要重复提交

    private Context mContext;

    private OkHttpClient mClient;

    private HttpManager() {
        mClient = new OkHttpClient();
    }

    public static HttpManager getInstance() {
        if (sManager == null) {
            synchronized (HttpManager.class) {
                if (sManager == null) {
                    sManager = new HttpManager();
                }
            }
        }
        return sManager;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    /**
     * funtion:同步请求 (不需要callback)(无Range)
     *
     * @param url
     */
    public Response syncRequest(String url) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            return mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * funtion:同步请求(含Range--多线程)
     *
     * @param url
     */
    public Response syncRequestByRange(String url, long start, long end) {

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Range", "bytes=" + start + "-" + end)
                .build();

        try {
            return mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 异步请求
     *
     * @param url
     * @param callback 回调
     */
    public void asyncRequest(final String url, final DownloadCallback callback) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful() && callback != null) {
                    callback.fail(NETWORK_ERROR_CODE, "请求失败");
                }

                //首先通过URL生成文件对象,接下来对文件进行写入操作
                File file = FileStorageManager.getInstance().getFileByName(url);

                //定义缓冲区域大小（这里是500kb）
                byte[] buffer = new byte[1024 * 500];

                //创建FileOutputStream对象准备写入，通过response.body().byteStream()
                //可以获取到下载文件的输入流InputStream对象，利用while循环读取输入流的信息写入到文件中
                int len;
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                InputStream inputStream = response.body().byteStream();
                while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    fileOutputStream.flush();
                }

                callback.success(file);
            }
        });
    }

    /**
     * 异步请求
     *
     * @param url
     * @param callback
     */
    public void asyncRequest(final String url, Callback callback) {

        Request request = new Request.Builder()
                .url(url)
                .build();

        mClient.newCall(request).enqueue(callback);
    }


}
