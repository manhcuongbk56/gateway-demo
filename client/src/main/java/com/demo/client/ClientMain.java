package com.demo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Log4j2
public class ClientMain {
    private static final String GATEWAY_SERVER_HOST = "localhost";
    private static final int GATEWAY_SERVER_PORT = 6969;
    private static final int NUM_OF_CLIENT = 200;
    private static final byte[] ID_PADDING = new byte[24];

    public static void main(String[] args) {

        ClientHandler clientHandler = new ClientHandler();



        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap(); // (1)
            bootstrap.group(workerGroup); // (2)
            bootstrap.channel(NioSocketChannel.class); // (3)
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(clientHandler);
                }
            });
            Channel[] channels = new Channel[NUM_OF_CLIENT];
            try {
                for (int i = 0; i < NUM_OF_CLIENT; i++){
                    ChannelFuture f = bootstrap.connect(GATEWAY_SERVER_HOST, GATEWAY_SERVER_PORT).sync();
                    // Wait until the connection is closed.
                    channels[i] = f.channel();
                }
                for (int i = 0; i < NUM_OF_CLIENT; i++){
                    sendRequest(channels[i]);
                }
                channels[0].closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("Error when connect");
            }
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


    public static void sendRequest(Channel ch) {
        for (int a = 0; a < 1; a++){
            ByteBuf request = ch.alloc().buffer(42); // (2)
            UUID requestId = UUID.randomUUID();
            request.writeLong(requestId.getMostSignificantBits());
            request.writeLong(requestId.getLeastSignificantBits());
            request.writeBytes(ID_PADDING);
            request.writeByte('|');
            for (int i = 0; i < 4; i++){
                request.writeByte(' ');
            }
            request.writeByte('|');
            String stockName = "ABC";
            byte[] stockNameBytes = stockName.getBytes(StandardCharsets.UTF_8);
            byte[] padding = new byte[20 - stockNameBytes.length];
            Arrays.fill(padding, (byte) ' ');
            request.writeBytes(stockNameBytes);
            request.writeBytes(padding);
            ch.writeAndFlush(request);
        }
        log.info("Done send request to server");
    }


}
