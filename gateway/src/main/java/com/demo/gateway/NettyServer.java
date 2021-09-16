package com.demo.gateway;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetSocketAddress;

@Log4j2
public class NettyServer {
    private static final int GATEWAY_PORT = 6969;
    private static final String GATEWAY_HORT = "localhost";
    private static final LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(4);
    private final EventLoopGroup boss = new NioEventLoopGroup();
    private final Bootstrap bootstrap = new Bootstrap();

    public void start(GatewayConfiguration gatewayConfiguration) throws InterruptedException {
        try {
            ComponentContainer componentContainer = new ComponentContainer(gatewayConfiguration);
            FlatMessageRouter messageRouter = componentContainer.getFlatMessageRouter();
            //Start initialize server, with some options and handler
            try {
                bootstrap
                        .group(boss)
                        .channel(NioDatagramChannel.class)
                        .localAddress(new InetSocketAddress(GATEWAY_HORT, GATEWAY_PORT))
                        .option(ChannelOption.AUTO_CLOSE, true)
                        .option(ChannelOption.SO_BROADCAST, true)
                        //finish add option, start to add handler
                        .handler(new ChannelInitializer<DatagramChannel>() {
                            @Override
                            protected void initChannel(DatagramChannel ch) {
                                //Here is our main handler. which will parse request type and choosing the right handler to process the request
                                ch.pipeline().addLast(messageRouter);
                            }
                        });
                Channel channel = bootstrap.bind().sync().channel();
                log.info("Start netty successful at address：{}", channel.localAddress());
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("Error happen: ", e);
            } finally {
                boss.shutdownGracefully();
            }

        } catch (IOException e) {
            log.error("Error happen when initialize server", e);
            Thread.sleep(1000L);
            System.exit(69);
        }

    }

}
