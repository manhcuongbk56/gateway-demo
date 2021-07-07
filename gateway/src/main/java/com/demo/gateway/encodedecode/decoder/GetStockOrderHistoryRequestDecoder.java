package com.demo.gateway.encodedecode.decoder;

import com.demo.common.message.orderhistory.GetStockOrderHistoryRequest;
import com.demo.common.utils.ByteBufUtils;
import com.demo.gateway.encodedecode.Decoder;
import io.netty.buffer.ByteBuf;

import java.time.LocalDate;
import java.util.UUID;

public class GetStockOrderHistoryRequestDecoder implements Decoder<GetStockOrderHistoryRequest> {

    @Override
    public GetStockOrderHistoryRequest decode(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.skipBytes(1);
        long clientAccountNo = byteBuf.readLong();
        byteBuf.skipBytes(1);
        LocalDate fromDate = ByteBufUtils.readDate(byteBuf);
        byteBuf.skipBytes(1);
        LocalDate toDate = ByteBufUtils.readDate(byteBuf);
        return new GetStockOrderHistoryRequest(requestId, clientAccountNo, fromDate, toDate);
    }
}
