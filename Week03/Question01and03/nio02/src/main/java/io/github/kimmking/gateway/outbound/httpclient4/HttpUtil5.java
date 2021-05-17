package io.github.kimmking.gateway.outbound.httpclient4;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author fzw
 * @description
 **/
public class HttpUtil5 {

    private static final IOReactorConfig IO_REACTOR_CONFIG;
    private static final RequestConfig REQUEST_CONFIG;
    private static final PoolingAsyncClientConnectionManager POOLING_ASYNC_CLIENT_CONNECTION_MANAGER;
    private static final CloseableHttpAsyncClient ASYNC_HTTP_CLIENT;
    private static final Timer TIMER;
    private static final TimerTask TIMER_TASK;

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
        TIMER = new Timer();
        TIMER_TASK = new TimerTask() {
            @Override
            public void run() {
                try {
                    POOLING_ASYNC_CLIENT_CONNECTION_MANAGER.closeExpired();
                    POOLING_ASYNC_CLIENT_CONNECTION_MANAGER.closeIdle(TimeValue.ofSeconds(20));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        TIMER.schedule(TIMER_TASK, 30000, 30000);
    }

    public static String basicPost(String data, String url, Map<String, String> headers, ContentType contentType, FutureCallback<SimpleHttpResponse> callback) {
        SimpleHttpRequest httpPost = SimpleHttpRequests.post(url);
        if (headers != null) {
            headers.forEach(httpPost::addHeader);
        }
        httpPost.setBody(data, contentType);
        return HttpUtil5.executeHttp(httpPost, callback);
    }

    public static String basicGet(Map<String, String> data, String url, Map<String, String> headers, FutureCallback<SimpleHttpResponse> callback) {
        StringBuilder builder = new StringBuilder(url).append("?");
        if (data != null) {
            data.forEach((key, value) -> {
                builder.append(key).append("=").append(value).append("&");
            });
        }
        builder.deleteCharAt(builder.length() - 1);
        String dataUrl = builder.toString();
        SimpleHttpRequest httpGet = SimpleHttpRequests.get(dataUrl);
        if (headers != null) {
            headers.forEach(httpGet::addHeader);
        }
        return HttpUtil5.executeHttp(httpGet, callback);
    }

    private static String executeHttp(SimpleHttpRequest httpRequest, FutureCallback<SimpleHttpResponse> callback) {
        Future<SimpleHttpResponse> httpGetFuture = ASYNC_HTTP_CLIENT.execute(httpRequest, callback);
        if (callback == null) {
            SimpleHttpResponse simpleHttpResponse = null;
            try {
                simpleHttpResponse = httpGetFuture.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
            byte[] bodyBytes = simpleHttpResponse.getBodyBytes();
            String responseBodyText = new String(bodyBytes, StandardCharsets.UTF_8);
            return responseBodyText;
        }
        return null;
    }

}
