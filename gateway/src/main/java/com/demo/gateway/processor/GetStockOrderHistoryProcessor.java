package com.demo.gateway.processor;

import com.demo.common.message.orderhistory.GetStockOrderHistoryRequest;
import com.demo.common.message.orderhistory.GetStockOrderHistoryResponse;
import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.encodedecode.decoder.GetStockOrderHistoryRequestDecoder;
import com.demo.gateway.encodedecode.encoder.GetStockOrderHistoryResponseEncoder;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetStockOrderHistoryProcessor implements Processor<GetStockOrderHistoryRequest, GetStockOrderHistoryResponse> {

    private GetStockOrderHistoryRequestDecoder decoder;
    private StockBusinessHandler handler;
    private GetStockOrderHistoryResponseEncoder encoder;


    @Override
    public GetStockOrderHistoryRequest decode(ByteBuf body) {
        return null;
    }

    @Override
    public ByteBuf encode(GetStockOrderHistoryResponse body) {
        return null;
    }

    @Override
    public BusinessHandler<GetStockOrderHistoryRequest, GetStockOrderHistoryResponse> getHandler() {
        return null;
    }

}
