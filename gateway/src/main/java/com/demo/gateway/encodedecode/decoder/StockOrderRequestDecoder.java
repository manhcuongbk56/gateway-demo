package com.demo.gateway.encodedecode.decoder;

import com.demo.common.message.stockorder.OrderStockRequest;
import com.demo.common.utils.ByteBufUtils;
import com.demo.gateway.encodedecode.Decoder;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class StockOrderRequestDecoder implements Decoder<OrderStockRequest> {

    @Override
    public OrderStockRequest decode(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.readByte();
        String stockName = ByteBufUtils.read20BytesString(byteBuf);
        byteBuf.readByte();
        String sellOrBuy = ByteBufUtils.read20BytesString(byteBuf);
        byteBuf.readByte();
        long quantity = byteBuf.readLong();
        byteBuf.readByte();
        double price = byteBuf.readDouble();
        return new OrderStockRequest(requestId, stockName, sellOrBuy, quantity, price);
    }
}
