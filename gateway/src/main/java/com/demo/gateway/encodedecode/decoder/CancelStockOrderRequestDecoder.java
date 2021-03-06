package com.demo.gateway.encodedecode.decoder;

import com.demo.common.message.cancelorder.CancelStockOrderRequest;
import com.demo.common.utils.ByteBufUtils;
import com.demo.gateway.encodedecode.Decoder;
import io.netty.buffer.ByteBuf;

import java.util.UUID;


public class CancelStockOrderRequestDecoder implements Decoder<CancelStockOrderRequest> {

    @Override
    public CancelStockOrderRequest decode(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.skipBytes(1);
        long orderNo = byteBuf.readLong();
        return new CancelStockOrderRequest(requestId, orderNo);
    }
}
