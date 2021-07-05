package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelHealthChecker;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author fzw
 * @description
 * @date 2021-07-05
 **/
public class NettyHttpClient {

    private static final Logger log = LoggerFactory.getLogger(NettyHttpClient.class);
    private static final NioEventLoopGroup NIO_EVENT_LOOP_GROUP;
    private static final Bootstrap BOOTSTRAP;
    public static final ChannelPoolMap<InetSocketAddress, FixedChannelPool> CHANNEL_POOL_MAP;
    public static final ConcurrentMap<String, CompletableFuture<String>> REQUEST_MAP;

    static {
        NIO_EVENT_LOOP_GROUP = new NioEventLoopGroup();
        BOOTSTRAP = new Bootstrap();
        BOOTSTRAP.group(NIO_EVENT_LOOP_GROUP);
        BOOTSTRAP.channel(NioSocketChannel.class);
//        BOOTSTRAP.handler(new ChannelInitializer<NioSocketChannel>() {
//            @Override
//            protected void initChannel(NioSocketChannel ch) throws Exception {
//                ChannelPipeline pipeline = ch.pipeline();
//                pipeline.addLast(new HttpClientCodec());
//                pipeline.addLast(new HttpOutHandler());
//            }
//        });
        REQUEST_MAP = new ConcurrentHashMap<>();
        CHANNEL_POOL_MAP = new AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>() {
            @Override
            protected FixedChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(NettyHttpClient.BOOTSTRAP.remoteAddress(key), new RpcChannelPoolHandler(),
                        ChannelHealthChecker.ACTIVE, FixedChannelPool.AcquireTimeoutAction.FAIL,
                        10000, 1, 2000, true, false);
            }
        };
    }


    public static <T, S> S send(String ip, int port, String path, T t, Class<S> clazz) {
        log.info("content: {}", t.toString());
        String url = "http://" + ip + ":" + port + path;
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        FixedChannelPool fixedChannelPool = null;
        Future<Channel> channelFuture = null;
        UUID uuid = UUID.randomUUID();
        CompletableFuture<String> future = new CompletableFuture<>();
        REQUEST_MAP.put(uuid.toString(), future);
        try {
            fixedChannelPool = NettyHttpClient.CHANNEL_POOL_MAP.get(inetSocketAddress);
            channelFuture = fixedChannelPool.acquire();
            GenericFutureListener<Future<Channel>> futureListener = new GenericFutureListener<Future<Channel>>() {
                @Override
                public void operationComplete(Future<Channel> future) throws Exception {
                    if (future.isSuccess()) {
                        ObjectMapper omr = new ObjectMapper();
                        String content = omr.writeValueAsString(t);
//                        String jsonContent = JSON.toJSONString(t);
                        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
                        log.info("http version created");
                        HttpMethod httpMethod = HttpMethod.POST;
                        log.info("http method created");
                        DefaultFullHttpRequest request = new DefaultFullHttpRequest(httpVersion, httpMethod, url, Unpooled.wrappedBuffer(content.getBytes(
                                StandardCharsets.UTF_8)), false);
                        log.info("request created");
                        request.headers().set(HttpHeaderNames.HOST, ip);
                        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                        request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
                        request.headers().set("req_id", uuid.toString());
                        NioSocketChannel channel = (NioSocketChannel) future.get();
                        channel.writeAndFlush(request);
                    }
                }
            };
            channelFuture.addListener(futureListener);
        } catch (Exception e) {
            log.error("send 异常", e);
        }
        String o = null;
        try {
            o = (String) future.get(5000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            REQUEST_MAP.remove(uuid.toString());
        }
        return JSON.parseObject(o, clazz);
    }

    public static void stop() {
    }

}
