package com.fzw.neonmqv3.client.util;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpVersion;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
public class HttpUtil {
    private static final IOReactorConfig IO_REACTOR_CONFIG;
    private static final RequestConfig REQUEST_CONFIG;
    private static final PoolingAsyncClientConnectionManager POOLING_ASYNC_CLIENT_CONNECTION_MANAGER;
    private static final CloseableHttpAsyncClient ASYNC_HTTP_CLIENT;

    static {
        IO_REACTOR_CONFIG = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(20))
                .build();
        REQUEST_CONFIG = RequestConfig.custom()
                .setConnectionRequestTimeout(10, TimeUnit.SECONDS)
                .setResponseTimeout(10, TimeUnit.SECONDS)
                .setConnectTimeout(10, TimeUnit.SECONDS)
                .build();
        POOLING_ASYNC_CLIENT_CONNECTION_MANAGER = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnTotal(25)
                .setMaxConnPerRoute(5)
                .setConnectionTimeToLive(TimeValue.ofSeconds(10))
                .setValidateAfterInactivity(TimeValue.ofSeconds(10))
                .build();
        ASYNC_HTTP_CLIENT = HttpAsyncClients.custom()
                .setConnectionManager(POOLING_ASYNC_CLIENT_CONNECTION_MANAGER)
                .setIOReactorConfig(IO_REACTOR_CONFIG)
                .setDefaultRequestConfig(REQUEST_CONFIG)
                .build();
        ASYNC_HTTP_CLIENT.start();
    }


    public static <T, S> S post(String url, HashMap<String, String> params, T body, Class<S> clazz) {
        String requestBody = JsonUtil.jsonSerialize(body);
        SimpleHttpRequest request = HttpUtil.build(url, params, requestBody, Method.POST);
        String responseBody = HttpUtil.http(request);
        return JsonUtil.jsonDeserialize(responseBody, clazz);
    }

    public static <T> T get(String url, HashMap<String, String> params, Class<T> clazz) {
        SimpleHttpRequest request = HttpUtil.build(url, params, null, Method.GET);
        String responseBody = HttpUtil.http(request);
        return JsonUtil.jsonDeserialize(responseBody, clazz);
    }

    private static String http(SimpleHttpRequest request) {
        SimpleHttpResponse response = HttpUtil.http0(request);
        return HttpUtil.extract(response);
    }

    private static SimpleHttpRequest build(String url, HashMap<String, String> params, String body, Method method) {
        SimpleRequestBuilder requestBuilder = SimpleRequestBuilder.create(method).setUri(url).setVersion(HttpVersion.HTTP_1_1).setCharset(StandardCharsets.UTF_8);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                requestBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        if (method.equals(Method.POST) && body != null) {
            requestBuilder.setBody(body, ContentType.APPLICATION_JSON);
        }
        return requestBuilder.build();
    }

    private static SimpleHttpResponse http0(SimpleHttpRequest request) {
        Future<SimpleHttpResponse> execute = ASYNC_HTTP_CLIENT.execute(request, null);
        SimpleHttpResponse response = null;
        try {
            response = execute.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String extract(SimpleHttpResponse response) {
        byte[] bodyBytes = response.getBodyBytes();
        if (bodyBytes == null) {
            throw new RuntimeException("响应体为空");
        }
        return new String(bodyBytes, StandardCharsets.UTF_8);
    }

}
