package com.demo.gateway;

import com.demo.gateway.serde.RequestDecoderHandler;
import com.demo.gateway.serde.ResponseEncodeHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Log4j2
public class NettyServer {
    private int serverPort = 6969;
    private String ip = "localhost";
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();
    private MessageHandler messageHandler = new MessageHandler();
    ServerBootstrap serverBootstrap = new ServerBootstrap();

    public void start() {
        try {
            serverBootstrap
                    .group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(ip, serverPort))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new IdleStateHandler(0, 0, 300, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(42));
                            ch.pipeline().addLast("decoder", new RequestDecoderHandler());
                            ch.pipeline().addLast("encoder", new  ResponseEncodeHandler());
                            ch.pipeline().addLast("gateway", messageHandler);
                        }
                    });
            Channel channel = serverBootstrap.bind().sync().channel();
            log.info("Start netty successful at address：{}", channel.localAddress());
            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("Error happen: ", e);
        } finally {
            work.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }

}
