package com.demo.gateway;

import com.demo.common.message.messagetype.MessageType;
import com.demo.common.message.stockorder.OrderStockCompleted;
import com.demo.gateway.encodedecode.Encoder;
import com.demo.gateway.processor.*;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@ChannelHandler.Sharable
public class KeyIntegerMessageRouter extends ChannelInboundHandlerAdapter implements MessageRouter<Integer> {

    private Map<Integer, Processor> prcessors;
    private Encoder<OrderStockCompleted> orderStockCompletedEncoder;

    @Inject
    public KeyIntegerMessageRouter(CancelStockOrderProcessor cancelStockOrderProcessor,
                                   GetStockOrderHistoryProcessor getStockOrderHistoryProcessor,
                                   OrderStockProcessor orderStockProcessor,
                                   StockPriceProcessor stockPriceProcessor,
                                   Encoder<OrderStockCompleted> orderStockCompletedEncoder) {
        //Initialize a map with key is messae type, value is processor
        this.prcessors = new HashMap<>();
        prcessors.put(MessageType.Request.GET_PRICE, stockPriceProcessor);
        prcessors.put(MessageType.Request.STOCK_ORDER, orderStockProcessor);
        prcessors.put(MessageType.Request.CANCEL_ORDER, cancelStockOrderProcessor);
        prcessors.put(MessageType.Request.GET_HISTORY, getStockOrderHistoryProcessor);
        this.orderStockCompletedEncoder = orderStockCompletedEncoder;
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
        handleRaw(messageType, data)
                //When handle done, write the result to socket to response to client
                .thenAccept(response -> {
                    ctx.writeAndFlush(new DatagramPacket(response, sender));
                })
                .exceptionally(ex -> {
                    log.error("Error happen when handle input", ex);
                    ctx.writeAndFlush("Error happen");
                    return null;
                });
    }

    @Override
    public Map<Integer, Processor> getProcessors() {
        return prcessors;
    }

}
