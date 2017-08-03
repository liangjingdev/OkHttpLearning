package com.liangjing.httpconnection;


import com.liangjing.httpconnection.http.HttpMethod;
import com.liangjing.httpconnection.http.HttpRequest;

import java.io.IOException;
import java.net.URI;

/**
 * Created by liangjing on 2017/8/3.
 */

public class OriginHttpRequestFactory implements HttpRequestFactory {

    @Override
    public HttpRequest createHttpRequest(URI uri, HttpMethod method) throws IOException {
        return null;
    }
}
