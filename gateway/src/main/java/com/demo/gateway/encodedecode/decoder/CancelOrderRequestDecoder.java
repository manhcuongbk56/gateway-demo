package com.demo.gateway.encodedecode.decoder;

import com.demo.common.message.cancelorder.CancelOrderRequest;
import com.demo.common.message.stockorder.OrderRequest;
import com.demo.gateway.encodedecode.Decoder;
import com.demo.gateway.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class CancelOrderRequestDecoder implements Decoder<CancelOrderRequest> {

    @Override
    public CancelOrderRequest decode(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        long orderNo = byteBuf.readLong();
        return new CancelOrderRequest(requestId, orderNo);
    }
}
