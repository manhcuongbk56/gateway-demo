package com.demo.gateway.business;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.gateway.GatewayConfiguration;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;

@Log4j2
public class SimpleBusinessHandler implements StockBusinessHandler {

    public SimpleBusinessHandler(GatewayConfiguration.HttpClientConfiguration config) {
    }

    @Override
    public CompletableFuture<StockPriceResponse> getPrice(GetStockPriceRequest getStockPriceRequest) {
        log.info("Call from SimpleBusinessHandler");
        return CompletableFuture.completedFuture(StockPriceResponse.success(getStockPriceRequest, 1.0d));
    }


}
