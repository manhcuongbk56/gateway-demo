package com.demo.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SuppressWarnings({"unchecked", "rawtypes"})

public class Client {

    private static LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(4);
    private final Channel channel;



    public Client(Channel channel) {
        this.channel = channel;
    }

    private void send(ByteBuf byteBuf) {
        channel.writeAndFlush(byteBuf);
    }


    public void fillOrder(){

    }


}
