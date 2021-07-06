package com.demo.gateway.processor;

import com.demo.common.message.cancelorder.CancelStockOrderRequest;
import com.demo.common.message.cancelorder.CancelStockOrderResponse;
import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.encodedecode.Decoder;
import com.demo.gateway.encodedecode.Encoder;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

public class CancelStockOrderProcessor implements Processor<CancelStockOrderRequest, CancelStockOrderResponse> {

    private final Decoder<CancelStockOrderRequest> decoder;
    private final StockBusinessHandler handler;
    private final Encoder<CancelStockOrderResponse> encoder;

    @Inject
    public CancelStockOrderProcessor(Decoder<CancelStockOrderRequest> decoder, StockBusinessHandler handler, Encoder<CancelStockOrderResponse> encoder) {
        this.decoder = decoder;
        this.handler = handler;
        this.encoder = encoder;
    }

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
