package com.demo.gateway.processor;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.encodedecode.Decoder;
import com.demo.gateway.encodedecode.Encoder;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;

public class StockPriceProcessor implements Processor<GetStockPriceRequest, StockPriceResponse> {

    private final Decoder<GetStockPriceRequest> decoder;
    private final StockBusinessHandler handler;
    private final Encoder<StockPriceResponse> encoder;

    @Inject
    public StockPriceProcessor(Decoder<GetStockPriceRequest> decoder, StockBusinessHandler handler, Encoder<StockPriceResponse> encoder) {
        this.decoder = decoder;
        this.handler = handler;
        this.encoder = encoder;
    }

    @Override
    public GetStockPriceRequest decode(ByteBuf body) {
        return decoder.decode(body);
    }

    @Override
    public ByteBuf encode(StockPriceResponse body) {
        return encoder.encode(body);
    }

    @Override
    public BusinessHandler<GetStockPriceRequest, StockPriceResponse> getHandler() {
        return handler::getPrice;
    }
}
