package com.liangjing.okhttp3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.liangjing.filedownload.DownloadManager;
import com.liangjing.filedownload.http.DownloadCallback;
import com.liangjing.filedownload.utils.LoggerUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);

//        //对Md5加密工作进行简单校验
//        File file = FileStorageManager.getInstance().getFileByName("http:www.imooc.com");
//        LoggerUtil.debug("liangjing", "file path = " + file.getAbsolutePath());

        /**
         *function:测试异步请求
         *
         HttpManager.getInstance().asyncRequest("http://img.mukewang.com/597948540001f00407500250.jpg", new DownloadCallback() {
        @Override public void success(File file) {
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        runOnUiThread(new Runnable() {
        @Override public void run() {
        imageView.setImageBitmap(bitmap);
        }
        });
        LoggerUtil.debug("liangjing", "success" + file.getAbsolutePath());
        }

        @Override public void fail(int errorCode, String errorMessage) {
        LoggerUtil.error("liangjing", "fail" + errorCode + " " + errorMessage);
        }

        @Override public void progress(int progress) {

        }
        });
         */

        DownloadManager.getInstance().download("http://img.mukewang.com/597948540001f00407500250.jpg", new DownloadCallback() {
            @Override
            public void success(File file) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
                LoggerUtil.debug("jing", "success" + file.getAbsolutePath());
            }

            @Override
            public void fail(int errorCode, String errorMessage) {
                LoggerUtil.debug("jing", "fail" + errorCode + " " + errorMessage);
            }

            @Override
            public void progress(int progress) {

            }
        });
    }
}
