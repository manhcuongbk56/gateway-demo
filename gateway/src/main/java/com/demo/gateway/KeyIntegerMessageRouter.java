package com.demo.gateway;

import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.decoder.Decoder;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.util.Map;

@Log4j2
@ChannelHandler.Sharable
public class KeyIntegerMessageRouter extends ChannelInboundHandlerAdapter implements MessageRouter<Integer> {

    private Decoder messageDecoder;

    @Inject
    public KeyIntegerMessageRouter(Decoder decoder) {
        this.messageDecoder = decoder;
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //Read incoming message
        DatagramPacket packet = (DatagramPacket) msg;
        ByteBuf data = packet.content();
        InetSocketAddress sender = packet.sender();
        if (data.readableBytes() <= 0){
            return;
        }
        //Read message type
        int messageType = data.readInt();
//        handleRaw(messageType, data)
//                //When handle done, write the result to socket to response to client
//                .thenAccept(response -> {
//                    ctx.writeAndFlush(new DatagramPacket(response, sender));
//                })
//                .exceptionally(ex -> {
//                    log.error("Error happen when handle input", ex);
//                    ctx.writeAndFlush("Error happen");
//                    return null;
//                });
    }

    @Override
    public Map<Integer, BusinessHandler> getProcessors() {
        return null;
    }
//
//    @Override
//    public Map<Integer, Processor> getProcessors() {
//        return prcessors;
//    }

}
