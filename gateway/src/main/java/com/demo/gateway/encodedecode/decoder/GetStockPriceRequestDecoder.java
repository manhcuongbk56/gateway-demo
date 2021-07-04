package com.demo.gateway.encodedecode.decoder;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.gateway.encodedecode.Decoder;
import io.netty.buffer.ByteBuf;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;


@Log4j2
public class GetStockPriceRequestDecoder implements Decoder<GetStockPriceRequest> {

    @Override
    public GetStockPriceRequest decode(ByteBuf byteBuf) {
        UUID requestId = new UUID(byteBuf.readLong(), byteBuf.readLong());
        byteBuf.readBytes(6 + 24);
        byte[] itemNameByte = new byte[20];
        byteBuf.readBytes(itemNameByte);
        String itemName = new String(itemNameByte).trim();
        return new GetStockPriceRequest(requestId, itemName);
    }

}
