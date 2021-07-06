package com.demo.gateway.processor;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.encodedecode.decoder.GetStockPriceRequestDecoder;
import com.demo.gateway.encodedecode.encoder.StockPriceResponseEncoder;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StockPriceProcessor implements Processor<GetStockPriceRequest, StockPriceResponse> {

    private GetStockPriceRequestDecoder decoder;
    private StockBusinessHandler handler;
    private StockPriceResponseEncoder encoder;

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
