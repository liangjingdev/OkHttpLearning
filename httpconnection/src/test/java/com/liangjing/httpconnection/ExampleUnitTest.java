package com.liangjing.httpconnection;

import com.liangjing.httpconnection.entity.Person;
import com.liangjing.httpconnection.services.MultiThreadProvider;
import com.liangjing.httpconnection.services.MultiThreadRequest;
import com.liangjing.httpconnection.services.MultiThreadResponse;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 * <p>
 * 在创建的module文件夹下会自动生成一个test文件夹，供开发人员测试所用，
 * 在这里可以编写一个简单的网络代码来测试以上编码工作是否正确
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
/*
    1、测试：发送Get请求

        OkHttpClient client = new OkHttpClient();
        OkHttpRequest request = new OkHttpRequest(client, HttpMethod.GET, "http://www.baidu.com");

        HttpResponse response = request.execute();

        String content = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
        while ((content = reader.readLine()) != null) {
            System.out.println(content);
        }
        //关闭输入流
        response.close();
    */


      /*
      2、测试：发送post请求，上传表单数据

        OkHttpClient client = new OkHttpClient();
        OkHttpRequest request = new OkHttpRequest(client, HttpMethod.POST, "http://localhost:8080/web/HelloServlet");

        //需要在write()方法中写入参数并将其转化为字节数组
        request.getBody().write("username=liangjing&userage=20".getBytes());
        HttpResponse response = request.execute();
        String content = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
        while ((content = reader.readLine()) != null) {
            System.out.println(content);

        }
    }*/


/*
   // 3、测试：利用工厂模式创建相对应的HttpRequest执行Get请求

        URI uri = new URI("http://www.baidu.com");
        OkHttpClient client = new OkHttpClient();

        //1、new出HttpRequestProvider的一个实例
        HttpRequestProvider httpRequestProvider = new HttpRequestProvider();

        //2、new出OkHttpRequestFactory的一个实例
        OkHttpRequestFactory okHttpRequestFactory = new OkHttpRequestFactory(client);

        //3、利用HttpRequestProvider的方法来设置需要进行生产的工厂
        httpRequestProvider.setHttpRequestFactory(okHttpRequestFactory);

        //4、调用HttpRequestProvider的方法获取相对应的HttpRequest实例
        HttpRequest request = httpRequestProvider.getHttpRequest(uri, HttpMethod.GET);

        HttpResponse response = request.execute();
        String content = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
        while ((content = reader.readLine()) != null) {
            System.out.println(content);
        }*/


/*   //  4、利用工厂模式创建相对应的HttpRequest执行Post请求上传表单数据

        URI uri = new URI("http://localhost:8080/web/HelloServlet");
        OkHttpClient client = new OkHttpClient();

        //1、new出HttpRequestProvider的一个实例
        HttpRequestProvider httpRequestProvider = new HttpRequestProvider();

        //2、new出OkHttpRequestFactory的一个实例
        OkHttpRequestFactory okHttpRequestFactory = new OkHttpRequestFactory(client);

        //3、利用HttpRequestProvider的方法来设置需要进行生产的工厂
        httpRequestProvider.setHttpRequestFactory(okHttpRequestFactory);

        //4、调用HttpRequestProvider的方法获取相对应的HttpRequest实例
        HttpRequest request = httpRequestProvider.getHttpRequest(uri, HttpMethod.POST);

        request.getBody().write("username=liangjing&userage=20".getBytes());
        HttpResponse response = request.execute();
        String content = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
        while ((content = reader.readLine()) != null) {
            System.out.println(content);
        }*/
        Map<String, String> map = new HashMap<>();
        map.put("username", "liangjing");
        map.put("userage", "20");

        MultiThreadProvider.carryOut("http://localhost:8080/web/HelloServlet", map, new MultiThreadResponse<Person>() {
            @Override
            public void success(MultiThreadRequest request, Person data) {
                System.out.println(" " + data.toString());
            }

            @Override
            public void fail(int errorCode, String errorMessage) {
            }
        });
    }
}