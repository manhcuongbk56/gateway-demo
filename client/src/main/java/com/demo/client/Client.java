package com.demo.client;

import com.demo.common.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
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
        ByteBuf request = ByteBufAllocator.DEFAULT.buffer(46);
        ByteBufUtils.writeString(request, "A3", 2);
        ByteBufUtils.writeString(request, "18", 2);
        ByteBufUtils.writeString(request, "4", 1);
        ByteBufUtils.writeString(request, "issueCode", 12);
        ByteBufUtils.writeString(request, "12", 4);
        ByteBufUtils.writeString(request, "aw", 2);
        ByteBufUtils.writeString(request, "123.3", 5);
        ByteBufUtils.writeString(request, "123.3", 7);
        ByteBufUtils.writeString(request, "si", 2);
        ByteBufUtils.writeString(request, "24h45", 8);
        ByteBufUtils.writeString(request, "123.3", 5);
        ByteBufUtils.writeString(request, "123.3", 5);
        ByteBufUtils.writeString(request, "123.3", 5);
        ByteBufUtils.writeString(request, "123.3", 5);
        ByteBufUtils.writeString(request, "123.3", 8);
        ByteBufUtils.writeString(request, "123.3", 11);
        ByteBufUtils.writeString(request, "123.3", 8);
        ByteBufUtils.writeString(request, "a", 1);
        ByteBufUtils.writeString(request, "023.3", 5);
        ByteBufUtils.writeString(request, "123.3", 5);
        ByteBufUtils.writeString(request, "x", 1);
        send(request);
    }


}
