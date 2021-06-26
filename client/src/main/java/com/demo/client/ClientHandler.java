package com.demo.client;

import com.demo.common.constant.ResponseCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        log.info("receive total {} response", count.incrementAndGet());
        var response = (ByteBuf)msg;
        var requestId = new UUID(response.readLong(), response.readLong());
        response.readBytes(25);
        var responseCode = response.readBytes(4).toString(CharsetUtil.UTF_8);
        if (Objects.equals(responseCode, ResponseCode.FAIL.getCode())){
            log.info("Request fail: requestId {}, responseCode {}", requestId, responseCode);
            return;
        }
        response.readByte();
        var name = response.readBytes(20).toString(CharsetUtil.UTF_8).strip();
        response.readByte();
        var price = response.readDouble();
        log.info("Request Success: requestId {}, responseCode {} stock name {} stockPrice {}", requestId,
                responseCode, name, price);
    }
}