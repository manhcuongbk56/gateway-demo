package com.demo.gateway.processor;

import com.demo.common.message.stockorder.OrderStockRequest;
import com.demo.common.message.stockorder.OrderStockResponse;
import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.business.StockBusinessHandler;
import com.demo.gateway.encodedecode.decoder.GetStockPriceRequestDecoder;
import com.demo.gateway.encodedecode.decoder.OrderStockRequestDecoder;
import com.demo.gateway.encodedecode.encoder.OrderStockResponseEncoder;
import com.demo.gateway.encodedecode.encoder.StockPriceResponseEncoder;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderStockProcessor implements Processor<OrderStockRequest, OrderStockResponse> {

    private OrderStockRequestDecoder decoder;
    private StockBusinessHandler handler;
    private OrderStockResponseEncoder encoder;


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
