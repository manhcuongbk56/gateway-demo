package com.demo.gateway;

import com.demo.gateway.encodedecode.decoder.CancelStockOrderRequestDecoder;
import com.demo.gateway.encodedecode.decoder.GetStockOrderHistoryRequestDecoder;
import com.demo.gateway.encodedecode.decoder.GetStockPriceRequestDecoder;
import com.demo.gateway.encodedecode.decoder.OrderStockRequestDecoder;
import com.demo.gateway.encodedecode.encoder.*;
import com.demo.gateway.processor.CancelStockOrderProcessor;
import com.demo.gateway.processor.GetStockOrderHistoryProcessor;
import com.demo.gateway.processor.OrderStockProcessor;
import com.demo.gateway.processor.StockPriceProcessor;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class DependencyInjector extends AbstractModule {


    public DependencyInjector() {
    }

    @Override
    protected void configure() {
        bind(CancelStockOrderRequestDecoder.class).in(Singleton.class);
        bind(GetStockOrderHistoryRequestDecoder.class).in(Singleton.class);
        bind(GetStockPriceRequestDecoder.class).in(Singleton.class);
        bind(OrderStockRequestDecoder.class).in(Singleton.class);
        bind(GetStockOrderHistoryResponseEncoder.class).in(Singleton.class);
        bind(CancelStockOrderResponseEncoder.class).in(Singleton.class);
        bind(OrderStockCompletedEncoder.class).in(Singleton.class);
        bind(OrderStockResponseEncoder.class).in(Singleton.class);
        bind(StockPriceResponseEncoder.class).in(Singleton.class);
        bind(StockPriceResponseEncoder.class).in(Singleton.class);
        bind(CancelStockOrderProcessor.class).in(Singleton.class);
        bind(GetStockOrderHistoryProcessor.class).in(Singleton.class);
        bind(OrderStockProcessor.class).in(Singleton.class);
        bind(StockPriceProcessor.class).in(Singleton.class);
        bind(KeyIntegerMessageRouter.class).in(Singleton.class);
    }


}
