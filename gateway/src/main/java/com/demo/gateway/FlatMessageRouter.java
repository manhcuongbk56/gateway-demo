package com.demo.gateway;

import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.decoder.FlatMessageDecoder;
import com.demo.gateway.util.FieldConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

@Log4j2
@ChannelHandler.Sharable
public class FlatMessageRouter extends ChannelInboundHandlerAdapter implements MessageRouter<String> {

    private FlatMessageDecoder messageDecoder;
    private Map<String, BusinessHandler> handlerMap;


    public FlatMessageRouter(FlatMessageDecoder decoder, Map<String, BusinessHandler> handlerMap) {
        this.messageDecoder = decoder;
        this.handlerMap = handlerMap;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //Read incoming message
        DatagramPacket packet = (DatagramPacket) msg;
        ByteBuf data = packet.content();
        InetSocketAddress sender = packet.sender();
        if (data.readableBytes() <= 0) {
            return;
        }
        Map<String, Object> decodedData = messageDecoder.decode(data);
        routeToProcessor(decodedData);
    }


    @Override
    public CompletionStage<Object> routeToProcessor(Map<String, Object> data) {
        String dataType = (String) data.get(FieldConstants.DATA_TYPE);
        return handlerMap.get(dataType).handle(data);
    }


}
