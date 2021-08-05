package com.demo.gateway.business;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.gateway.GatewayConfiguration;
import com.demo.gateway.util.CompletableFutureUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.demo.gateway.util.JsonUtils.MAPPER;

@Log4j2
public class OkHttpSyncStockBusinessHandler implements StockBusinessHandler {

    public static final String PRICE_URL = "http://localhost:8080/price";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Random rand = new Random();
    private final OkHttpClient client;

    public OkHttpSyncStockBusinessHandler(GatewayConfiguration.HttpClientConfiguration config) {
        ConnectionPool connectionPool = new ConnectionPool(config.getMaxConnection(), config.getKeepAliveTime());
        this.client = new OkHttpClient();
        client.setConnectionPool(connectionPool);
        client.setConnectTimeout(config.getConnectTimeOut(), TimeUnit.MILLISECONDS);
        client.setReadTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);
        client.setWriteTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public CompletableFuture<StockPriceResponse> getPrice(GetStockPriceRequest getStockPriceRequest) {
        log.info("Call from ok http sync");
        String body;
        try {
            body = MAPPER.writeValueAsString(getStockPriceRequest);
        } catch (JsonProcessingException ex) {
            return CompletableFutureUtils.failedFuture(ex);
        }
        RequestBody requestBody = RequestBody.create(JSON, body);
        Request request = new Request.Builder()
                .url(PRICE_URL)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return CompletableFutureUtils.failedFuture(new Exception("failed request"));

            }
            StockPriceResponse priceResponse = MAPPER.readValue(response.body().bytes(), StockPriceResponse.class);
            return CompletableFuture.completedFuture(priceResponse);
        } catch (IOException ex) {
            return CompletableFutureUtils.failedFuture(ex);
        }
    }


    public boolean isShouldSuccess() {
        int number = rand.nextInt(10);
        return number >= 2;
    }

}
