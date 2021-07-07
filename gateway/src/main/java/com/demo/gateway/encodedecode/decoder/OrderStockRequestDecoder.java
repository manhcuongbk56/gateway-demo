package com.demo.gateway.encodedecode.decoder;

import com.demo.common.message.stockorder.OrderStockRequest;
import com.demo.common.utils.ByteBufUtils;
import com.demo.gateway.encodedecode.Decoder;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class OrderStockRequestDecoder implements Decoder<OrderStockRequest> {

    @Override
    public OrderStockRequest decode(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.skipBytes(1);
        String stockName = ByteBufUtils.read20BytesString(byteBuf);
        byteBuf.skipBytes(1);
        String sellOrBuy = ByteBufUtils.read20BytesString(byteBuf);
        byteBuf.skipBytes(1);
        long quantity = byteBuf.readLong();
        byteBuf.skipBytes(1);
        double price = byteBuf.readDouble();
        return new OrderStockRequest(requestId, stockName, sellOrBuy, quantity, price);
    }
}
