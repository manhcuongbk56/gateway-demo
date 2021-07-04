package com.demo.gateway.encodedecode.decoder;

import com.demo.common.message.stockorder.OrderRequest;
import com.demo.gateway.encodedecode.Decoder;
import com.demo.gateway.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class StockOrderRequestDecoder implements Decoder<OrderRequest> {

    @Override
    public OrderRequest decode(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        int stockNameLength = byteBuf.readInt();
        byte[] stockNameBytes = new byte[stockNameLength];
        byteBuf.readBytes(stockNameBytes);
        String stockName = new String(stockNameBytes).trim();
        boolean isSell = byteBuf.readBoolean();
        long quantity = byteBuf.readLong();
        double price = byteBuf.readDouble();
        return new OrderRequest(requestId, stockName, isSell, quantity, price);
    }
}
