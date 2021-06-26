package com.demo.gateway;

import com.demo.common.message.GetStockPriceRequest;
import com.demo.gateway.service.GetPriceService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<GetStockPriceRequest> {

    private GetPriceService getPriceService = new GetPriceService();

    private AtomicInteger requestCount = new AtomicInteger(0);
    private AtomicInteger responseCount = new AtomicInteger(0);
    private AtomicInteger connections = new AtomicInteger(0);

    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) {
        if (ctx.channel().remoteAddress() != null) {
            log.info("total connection: {} ", connections.incrementAndGet());
        }
    }

    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) {
        if (ctx.channel().remoteAddress() != null) {
            log.info("total connection: {} ", connections.decrementAndGet());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GetStockPriceRequest msg){
        log.info("Receive total {} request", requestCount.incrementAndGet());
        getPriceService.getPrice(msg)
                .thenAccept(response -> {
                    log.info("Response total {}", responseCount.incrementAndGet());
                    ctx.writeAndFlush(response);
                } );
    }


}
