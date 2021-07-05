package com.demo.gateway.encodedecode.decoder;

import com.demo.common.message.orderhistory.GetStockOrderHistoryRequest;
import com.demo.common.utils.ByteBufUtils;
import com.demo.gateway.encodedecode.Decoder;
import io.netty.buffer.ByteBuf;

import java.time.LocalDate;
import java.util.UUID;

public class OrderHistoryRequestDecoder implements Decoder<GetStockOrderHistoryRequest> {

    @Override
    public GetStockOrderHistoryRequest decode(ByteBuf byteBuf) {
        UUID requestId = ByteBufUtils.readUUID(byteBuf);
        byteBuf.readByte();
        long clientAccountNo = byteBuf.readLong();
        byteBuf.readByte();
        LocalDate fromDate = ByteBufUtils.readDate(byteBuf);
        byteBuf.readByte();
        LocalDate toDate = ByteBufUtils.readDate(byteBuf);
        return new GetStockOrderHistoryRequest(requestId, clientAccountNo, fromDate, toDate);
    }
}
