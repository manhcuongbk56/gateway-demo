package com.demo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClientManager {

    private Bootstrap bootstrap;
    private String serverHost;
    private int serverPort;

    public ClientManager(String serverHost, int serverPort) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        try {
            bootstrap = new Bootstrap(); // (1)
            bootstrap.group(workerGroup); // (2)
            bootstrap.channel(NioDatagramChannel.class); // (3)
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {
                @Override
                public void initChannel(NioDatagramChannel ch) {
                }
            });
        } catch (Exception ex){
            log.error("Error happen when initialize client manager");
        }
    }

    /**
     * this is a block method
     * do not call it on event loop thread.
     * @return a client
     */
    public Client newClient() throws InterruptedException {
        Channel channel = bootstrap.connect(serverHost, serverPort).sync().channel();
        return new Client(channel);
    }

}
