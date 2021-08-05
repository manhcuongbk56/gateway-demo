package com.demo.gateway.processor;

import com.demo.common.message.stockorder.OrderStockRequest;
import com.demo.common.message.stockorder.OrderStockResponse;
import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.encodedecode.Decoder;
import com.demo.gateway.encodedecode.Encoder;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;

public class OrderStockProcessor implements Processor<OrderStockRequest, OrderStockResponse> {

    private final Decoder<OrderStockRequest> decoder;
    private final StockBusinessHandler handler;
    private final Encoder<OrderStockResponse> encoder;

    @Inject
    public OrderStockProcessor(Decoder<OrderStockRequest> decoder, StockBusinessHandler handler, Encoder<OrderStockResponse> encoder) {
        this.decoder = decoder;
        this.handler = handler;
        this.encoder = encoder;
    }

    @Override
    public OrderStockRequest decode(ByteBuf body) {
        return decoder.decode(body);
    }

    @Override
    public ByteBuf encode(OrderStockResponse body) {
        return encoder.encode(body);
    }

    @Override
    public BusinessHandler<OrderStockRequest, OrderStockResponse> getHandler() {
        return handler::orderStock;
    }
}
