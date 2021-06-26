package com.demo.gateway.serde;

import com.demo.common.constant.ResponseCode;
import com.demo.common.message.StockPriceResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@ChannelHandler.Sharable
public class ResponseEncodeHandler extends MessageToByteEncoder<StockPriceResponse> {



    @Override
    protected void encode(ChannelHandlerContext ctx, StockPriceResponse msg, ByteBuf out) throws Exception {
        out.writeLong(msg.getRequestId().getMostSignificantBits());
        out.writeLong(msg.getRequestId().getLeastSignificantBits());
        out.writeByte('|');
        out.writeBytes(msg.getResponseCode().getBytes(StandardCharsets.UTF_8));
        if (Objects.equals(msg.getResponseCode(), ResponseCode.FAIL.getCode())){
            return;
        }
        out.writeByte('|');
        var nameRealBytes = msg.getStockItemName().getBytes(StandardCharsets.UTF_8);
        out.writeBytes(nameRealBytes);
        out.writeByte('|');
        out.writeLong(msg.getStockPrice());
    }
}
