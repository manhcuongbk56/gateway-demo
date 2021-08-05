package com.demo.gateway.processor;

import com.demo.common.message.orderhistory.GetStockOrderHistoryRequest;
import com.demo.common.message.orderhistory.GetStockOrderHistoryResponse;
import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.encodedecode.Decoder;
import com.demo.gateway.encodedecode.Encoder;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;

public class GetStockOrderHistoryProcessor implements Processor<GetStockOrderHistoryRequest, GetStockOrderHistoryResponse> {

    private final Decoder<GetStockOrderHistoryRequest> decoder;
    private final StockBusinessHandler handler;
    private final Encoder<GetStockOrderHistoryResponse> encoder;

    @Inject
    public GetStockOrderHistoryProcessor(Decoder<GetStockOrderHistoryRequest> decoder, StockBusinessHandler handler, Encoder<GetStockOrderHistoryResponse> encoder) {
        this.decoder = decoder;
        this.handler = handler;
        this.encoder = encoder;
    }

    @Override
    public GetStockOrderHistoryRequest decode(ByteBuf body) {
        return decoder.decode(body);
    }

    @Override
    public ByteBuf encode(GetStockOrderHistoryResponse body) {
        return encoder.encode(body);
    }

    @Override
    public BusinessHandler<GetStockOrderHistoryRequest, GetStockOrderHistoryResponse> getHandler() {
        return handler::getStockOrderHistory;
    }

}
