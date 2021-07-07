package com.demo.client;

import com.demo.common.constant.ResponseCode;
import com.demo.common.utils.ByteBufUtils;
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
public class ClientRead extends ChannelInboundHandlerAdapter {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("receive total {} response", count.incrementAndGet());
        ByteBuf response = (ByteBuf)msg;
        UUID requestId = ByteBufUtils.readUUID(response);
        response.skipBytes(1);
        String responseCode = ByteBufUtils.readString(response, 4);
        if (Objects.equals(responseCode, ResponseCode.FAIL.getCode())){
            log.info("Request fail: requestId {}, responseCode {}", requestId, responseCode);
            return;
        }
        response.skipBytes(1);
        String name = ByteBufUtils.read20BytesString(response);
        response.skipBytes(1);
        double price = response.readDouble();
        response.skipBytes(2);
        response.release();
        log.info("Request Success: requestId {}, responseCode {} stock name {} stockPrice {}", requestId,
                responseCode, name, price);
    }
}