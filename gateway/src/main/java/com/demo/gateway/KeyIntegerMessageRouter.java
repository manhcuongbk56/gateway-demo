package com.demo.gateway;

import com.demo.common.message.messagetype.MessageType;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.encodedecode.decoder.GetStockPriceRequestDecoder;
import com.demo.gateway.processor.*;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@ChannelHandler.Sharable
public class KeyIntegerMessageRouter extends ChannelInboundHandlerAdapter implements MessageRouter<Integer> {

    private Map<Integer, Processor> prcessors;

    @Inject
    public KeyIntegerMessageRouter(CancelStockOrderProcessor cancelStockOrderProcessor,
                                   GetStockOrderHistoryProcessor getStockOrderHistoryProcessor,
                                   OrderStockProcessor orderStockProcessor,
                                   StockPriceProcessor stockPriceProcessor ) {
        this.prcessors = new HashMap<>();
        prcessors.put(MessageType.Request.GET_PRICE, stockPriceProcessor);
        prcessors.put(MessageType.Request.STOCK_ORDER, orderStockProcessor);
        prcessors.put(MessageType.Request.CANCEL_ORDER, cancelStockOrderProcessor);
        prcessors.put(MessageType.Request.GET_HISTORY, getStockOrderHistoryProcessor);

    }


    private AtomicInteger requestCount = new AtomicInteger(0);
    private AtomicInteger responseCount = new AtomicInteger(0);
    private AtomicInteger connections = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        int messageType = data.readInt();
        handleRaw(messageType, data)
                .thenApply(response -> ctx.writeAndFlush(response))
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
