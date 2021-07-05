package io.kimmking.rpcfx.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author fzw
 * @description
 * @date 2021-07-05
 **/
public class HttpInHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HttpInHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("{},{}", msg.getClass().getName(), msg instanceof FullHttpResponse);

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            String reqId = response.headers().get("req_id");
            String content = response.content().toString(StandardCharsets.UTF_8);
            log.info("{}", content);
            NettyHttpClient.REQUEST_MAP.get(reqId).complete(content);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelReadComplete");
        this.releaseChannel(ctx);
    }

    private void releaseChannel(ChannelHandlerContext ctx) {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        NettyHttpClient.CHANNEL_POOL_MAP.get(socketAddress).release(ctx.channel());
        log.debug("{},{},{}", ctx.channel().remoteAddress().getClass().getName(), socketAddress.toString(), ctx.channel().id());
    }
}
