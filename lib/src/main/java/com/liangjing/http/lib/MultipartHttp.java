package com.liangjing.http.lib;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liangjing on 2017/7/30.
 * post请求--上传文件（如图片）
 */

public class MultipartHttp {

    public static void main(String args[]) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), new File("C:\\Users\\asus\\Desktop\\008\\avatar.png"));

        //MultipartBody--(用来)上传文件(这里用来上传一个头像图片)
        MultipartBody multipartBody = new MultipartBody.Builder()
                //注意这里还需要指定上传的类型,否则(这里)去掉将会打印不出来上传者的名字。(这里上传的类型属于表单类型，故选择FORM.其它可进入MultipartBody源码查看)
                .setType(MultipartBody.FORM)
                .addFormDataPart("filename", "avatar.png", requestBody)
                .addFormDataPart("name","liangjing")
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.155.1:8080/web/UploadServlet")
                .post(multipartBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
