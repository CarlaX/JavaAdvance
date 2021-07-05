package io.kimmking.rpcfx.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fzw
 */

public class RpcChannelPoolHandler implements ChannelPoolHandler {
    private static final Logger log = LoggerFactory.getLogger(RpcChannelPoolHandler.class);

    @Override
    public void channelReleased(Channel ch) throws Exception {
        log.debug("channelReleased");
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {
        log.debug("channelAcquired");
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {
        log.debug("channelCreated");
        NioSocketChannel nioSocketChannel = (NioSocketChannel) ch;
        nioSocketChannel.config().setKeepAlive(true);
        nioSocketChannel.config().setTcpNoDelay(true);
        ChannelPipeline pipeline = ch.pipeline();
//        pipeline.addLast(new HttpClientCodec());
//        pipeline.addLast(new HttpInHandler());

        pipeline.addLast(new HttpRequestEncoder());
        pipeline.addLast(new HttpResponseDecoder());
        pipeline.addLast(new HttpObjectAggregator(65535));
        pipeline.addLast(new HttpInHandler());
    }
}
