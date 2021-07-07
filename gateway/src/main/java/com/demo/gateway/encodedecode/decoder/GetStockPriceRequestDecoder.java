package com.demo.gateway.encodedecode.decoder;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.utils.ByteBufUtils;
import com.demo.gateway.encodedecode.Decoder;
import io.netty.buffer.ByteBuf;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;


@Log4j2
public class GetStockPriceRequestDecoder implements Decoder<GetStockPriceRequest> {

    @Override
    public GetStockPriceRequest decode(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.skipBytes(1 + 4 + 1);
        String itemName = ByteBufUtils.read20BytesString(byteBuf);
        return new GetStockPriceRequest(requestId, itemName);
    }

}
