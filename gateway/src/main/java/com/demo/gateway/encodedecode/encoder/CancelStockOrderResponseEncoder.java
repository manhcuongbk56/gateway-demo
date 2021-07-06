package com.demo.gateway.encodedecode.encoder;

import com.demo.common.message.cancelorder.CancelStockOrderResponse;
import com.demo.common.message.messagetype.MessageType;
import com.demo.common.utils.ByteBufUtils;
import com.demo.gateway.encodedecode.Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@Log4j2
@ChannelHandler.Sharable
public class CancelStockOrderResponseEncoder implements Encoder<CancelStockOrderResponse> {

    private static final byte[] PRICE_PADDING = new byte[2];

    @Override
    public ByteBuf encode(CancelStockOrderResponse stockPriceResponse) {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer(49);
        out.writeInt(MessageType.Response.CANCEL_STOCK_ORDER_RESPONSE);
        ByteBufUtils.writeUUID(out, stockPriceResponse.getRequestId());
        out.writeByte('|');
        out.writeBytes(stockPriceResponse.getResponseCode().getBytes(StandardCharsets.UTF_8));
        return out;
    }


}
