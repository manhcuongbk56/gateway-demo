package com.demo.gateway;

import com.demo.common.message.cancelorder.CancelStockOrderRequest;
import com.demo.common.message.cancelorder.CancelStockOrderResponse;
import com.demo.common.message.orderhistory.GetStockOrderHistoryRequest;
import com.demo.common.message.orderhistory.GetStockOrderHistoryResponse;
import com.demo.common.message.stockorder.OrderStockCompleted;
import com.demo.common.message.stockorder.OrderStockRequest;
import com.demo.common.message.stockorder.OrderStockResponse;
import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.gateway.business.*;
import com.demo.gateway.encodedecode.Decoder;
import com.demo.gateway.encodedecode.Encoder;
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
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

public class DependencyInjector extends AbstractModule {

    private final GatewayConfiguration gatewayConfiguration;

    public DependencyInjector(GatewayConfiguration gatewayConfiguration) {
        this.gatewayConfiguration = gatewayConfiguration;
    }

    @Override
    protected void configure() {

        TypeLiteral<Decoder<CancelStockOrderRequest>> cancelStockOrderRequest = new TypeLiteral<Decoder<CancelStockOrderRequest>>() {
        };
        TypeLiteral<Decoder<GetStockOrderHistoryRequest>> getStockOrderHistory = new TypeLiteral<Decoder<GetStockOrderHistoryRequest>>() {
        };
        TypeLiteral<Decoder<GetStockPriceRequest>> getStockPrice = new TypeLiteral<Decoder<GetStockPriceRequest>>() {
        };
        TypeLiteral<Decoder<OrderStockRequest>> orderStockRequest = new TypeLiteral<Decoder<OrderStockRequest>>() {
        };
        //Encoder
        TypeLiteral<Encoder<CancelStockOrderResponse>> cancelStockOrderResponse = new TypeLiteral<Encoder<CancelStockOrderResponse>>() {
        };
        TypeLiteral<Encoder<GetStockOrderHistoryResponse>> getStockOrderHistoryResponse = new TypeLiteral<Encoder<GetStockOrderHistoryResponse>>() {
        };
        TypeLiteral<Encoder<OrderStockCompleted>> orderStockCompleted = new TypeLiteral<Encoder<OrderStockCompleted>>() {
        };
        TypeLiteral<Encoder<OrderStockResponse>> orderStockResponse = new TypeLiteral<Encoder<OrderStockResponse>>() {
        };
        TypeLiteral<Encoder<StockPriceResponse>> stockPriceResponse = new TypeLiteral<Encoder<StockPriceResponse>>() {
        };

        bind(CancelStockOrderRequestDecoder.class).in(Singleton.class);
        bind(cancelStockOrderRequest).to(CancelStockOrderRequestDecoder.class).in(Singleton.class);
        bind(getStockOrderHistory).to(GetStockOrderHistoryRequestDecoder.class).in(Singleton.class);
        bind(getStockPrice).to(GetStockPriceRequestDecoder.class).in(Singleton.class);
        bind(orderStockRequest).to(OrderStockRequestDecoder.class).in(Singleton.class);
        bind(orderStockCompleted).to(OrderStockCompletedEncoder.class).in(Singleton.class);
        bind(orderStockResponse).to(OrderStockResponseEncoder.class).in(Singleton.class);
        bind(cancelStockOrderResponse).to(CancelStockOrderResponseEncoder.class).in(Singleton.class);
        bind(getStockOrderHistoryResponse).to(GetStockOrderHistoryResponseEncoder.class).in(Singleton.class);
        bind(stockPriceResponse).to(StockPriceResponseEncoder.class).in(Singleton.class);
        bind(CancelStockOrderProcessor.class).in(Singleton.class);
        bind(GetStockOrderHistoryProcessor.class).in(Singleton.class);
        bind(OrderStockProcessor.class).in(Singleton.class);
        bind(StockPriceProcessor.class).in(Singleton.class);
        bind(KeyIntegerMessageRouter.class).in(Singleton.class);
    }

    @Provides
    private StockBusinessHandler provideStockBusinessHandler() {
        GatewayConfiguration.HttpClientConfiguration httpConfig = gatewayConfiguration.getHttpClient();
        switch (httpConfig.getClientType()) {
            case OKHTTP_ASYNC:
                return new OkHttpAsyncStockBusinessHandler(httpConfig);
            case OKHTTP_SYNC:
                return new OkHttpSyncStockBusinessHandler(httpConfig);
            case APACHE_ASYNC:
                return new ApacheAsyncStockBusinessHandler(httpConfig);
            case APACHE_SYNC:
                return new ApacheSyncStockBusinessHandler(httpConfig);
            default:
                throw new IllegalArgumentException("client type of http config not match");
        }
    }


}
