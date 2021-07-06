package com.demo.gateway;

import com.demo.gateway.business.StockBusinessHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Log4j2
public class NettyServer {
    private static final int GATEWAY_PORT = 6969;
    private static final String GATEWAY_HORT = "localhost";
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup(10);
    private StockBusinessHandler stockBusinessHandler = new StockBusinessHandler();
    private ServerBootstrap serverBootstrap = new ServerBootstrap();
    private static LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(4);

    public void start() {
        //Dependency injector, I use Guice of Google
        Injector injector = Guice.createInjector(new DependencyInjector());
        KeyIntegerMessageRouter messageHandler = injector.getInstance(KeyIntegerMessageRouter.class);
        //Start initialize server, with some options and handler
        try {
            serverBootstrap
                    .group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(GATEWAY_HORT, GATEWAY_PORT))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                    //finish add option, start to add handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(lengthFieldPrepender);
                            ch.pipeline().addLast(new IdleStateHandler(0, 0, 300, TimeUnit.SECONDS));
                            ch.pipeline().addLast( new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            //above is just some handler to process common like bye buf length, prepend length...
                            //Here is our main handler. which will parse request type and choosing the right handler to process the request
                            ch.pipeline().addLast(messageHandler);
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
