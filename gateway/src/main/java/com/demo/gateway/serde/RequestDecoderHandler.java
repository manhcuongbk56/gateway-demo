package com.demo.gateway.serde;

import com.demo.common.message.GetStockPriceRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;


@Log4j2
public class RequestDecoderHandler extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        UUID requestId = new UUID(in.readLong(), in.readLong());
        in.readBytes(6 + 24);
        byte[] itemNameByte = new byte[20];
        in.readBytes(itemNameByte);
        String itemName = new String(itemNameByte).trim();
        out.add(new GetStockPriceRequest(requestId, itemName));
    }
}
