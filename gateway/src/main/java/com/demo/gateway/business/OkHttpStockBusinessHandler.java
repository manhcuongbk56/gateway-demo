package com.demo.gateway.business;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.gateway.GatewayConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.demo.gateway.util.JsonUtils.MAPPER;

public class OkHttpStockBusinessHandler implements StockBusinessHandler {

    public static final String PRICE_URL = "http://localhost:8080/price";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Random rand = new Random();
    private final OkHttpClient client;

    public OkHttpStockBusinessHandler(GatewayConfiguration.HttpClientConfiguration config) {
        ConnectionPool connectionPool = new ConnectionPool(config.getMaxConnection(), config.getKeepAliveTime());
        this.client = new OkHttpClient();
        client.setConnectionPool(connectionPool);
        client.setConnectTimeout(config.getConnectTimeOut(), TimeUnit.MILLISECONDS);
        client.setReadTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS);
        client.setWriteTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public CompletableFuture<StockPriceResponse> getPrice(GetStockPriceRequest getStockPriceRequest) {
        byte[] body;
        try {
            body = MAPPER.writeValueAsBytes(getStockPriceRequest);
        } catch (JsonProcessingException e) {
            CompletableFuture<StockPriceResponse> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
        Request request = new Request.Builder()
                .url(PRICE_URL)
                .post(RequestBody.create(JSON, body))
                .build();
        Call call = client.newCall(request);
        CompletableFuture<StockPriceResponse> result = new CompletableFuture<>();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                result.completeExceptionally(e);

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        result.complete(MAPPER.readValue(response.body().bytes(), StockPriceResponse.class));
                        return;
                    }
                    result.completeExceptionally(new IllegalArgumentException("request is not in right format"));
                } catch (Exception ex) {
                    result.completeExceptionally(ex);
                }
            }

        });
        return result;
    }


    public boolean isShouldSuccess() {
        int number = rand.nextInt(10);
        return number >= 2;
    }

}
