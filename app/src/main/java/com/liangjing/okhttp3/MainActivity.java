package com.liangjing.okhttp3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.liangjing.filedownload.DownloadManager;
import com.liangjing.filedownload.http.DownloadCallback;
import com.liangjing.filedownload.utils.LoggerUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Bitmap bitmap;
    private int count = 0;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image);
        mProgress = (ProgressBar) findViewById(R.id.progress);
/*
        //对Md5加密工作进行简单校验
        File file = FileStorageManager.getInstance().getFileByName("http:www.imooc.com");
        LoggerUtil.debug("liangjing", "file path = " + file.getAbsolutePath());*/

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

        String imageUrl = "http://img.mukewang.com/597948540001f00407500250.jpg";
        String apkUrl = "http://shouji.360tpcdn.com/160901/84c090897cbf0158b498da0f42f73308/com.icoolme.android.weather_2016090200.apk";
        DownloadManager.getInstance().download(apkUrl, new DownloadCallback() {
            @Override
            public void success(File file) {
                if (count < 1) {
                    count++;
                }
//                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });

                installApk(file);
                LoggerUtil.debug("jing", "success" + file.getAbsolutePath());
            }

            @Override
            public void fail(int errorCode, String errorMessage) {
                LoggerUtil.debug("jing", "fail" + errorCode + " " + errorMessage);
            }

            @Override
            public void progress(int progress) {
                LoggerUtil.debug("jing", "progress ------>" + progress);
                mProgress.setProgress(progress);
            }
        });
    }


/*
        Map<String, String> map = new HashMap<>();
        map.put("username", "liangjing");
        map.put("userage", "20");

        MultiThreadProvider.carryOut("http://192.168.51.2:8080/web/HelloServlet", map, new MultiThreadResponse<Person>() {
            @Override
            public void success(MultiThreadRequest request, Person data) {
                LoggerUtil.debug("jing", data.toString());
            }

            @Override
            public void fail(int errorCode, String errorMessage) {
                System.out.println("111");
            }
        });
    }*/


    /**
     * function:下载APK
     *
     * @param file
     */

    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsoluteFile().toString()), "application/vnd.android.package-archive");
        MainActivity.this.startActivity(intent);
    }
}





