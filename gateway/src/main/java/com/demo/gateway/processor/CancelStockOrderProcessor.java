package com.demo.gateway.processor;

import com.demo.common.message.cancelorder.CancelStockOrderRequest;
import com.demo.common.message.cancelorder.CancelStockOrderResponse;
import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.encodedecode.decoder.CancelStockOrderRequestDecoder;
import com.demo.gateway.encodedecode.encoder.CancelStockOrderResponseEncoder;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CancelStockOrderProcessor implements Processor<CancelStockOrderRequest, CancelStockOrderResponse> {

    private CancelStockOrderRequestDecoder decoder;
    private StockBusinessHandler handler;
    private CancelStockOrderResponseEncoder encoder;


    @Override
    public CancelStockOrderRequest decode(ByteBuf body) {
        return decoder.decode(body);
    }

    @Override
    public ByteBuf encode(CancelStockOrderResponse body) {
        return encoder.encode(body);
    }

    @Override
    public BusinessHandler<CancelStockOrderRequest, CancelStockOrderResponse> getHandler() {
        return handler::cancelStockOrder;
    }
}
