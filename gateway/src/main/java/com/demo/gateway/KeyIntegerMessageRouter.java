package com.demo.gateway;

import com.demo.common.message.messagetype.IntegerMessageType;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.processor.PriceProcessor;
import com.demo.gateway.processor.Processor;
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

    public KeyIntegerMessageRouter(StockBusinessHandler stockBusinessHandler) {
        this.prcessors = new HashMap<>();
        prcessors.put(IntegerMessageType.Request.GET_PRICE, getPriceService);

    }

    private PriceProcessor getPriceService = new PriceProcessor();

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
                    log.error("Error happen when handle input");
                    ctx.writeAndFlush("Error happen");
                    return null;
                });
    }

    @Override
    public Map<Integer, Processor> getProcessors() {
        return prcessors;
    }

}
