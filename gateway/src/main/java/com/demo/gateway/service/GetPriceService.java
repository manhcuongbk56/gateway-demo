package com.demo.gateway.service;

import com.demo.common.message.GetStockPriceRequest;
import com.demo.common.message.StockPriceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static com.demo.gateway.util.JsonUtils.MAPPER;

public class GetPriceService {

    OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public CompletableFuture<StockPriceResponse> getPrice(GetStockPriceRequest getStockPriceRequest) {
        byte[] body;
        try {
            body = MAPPER.writeValueAsBytes(getStockPriceRequest);
        } catch (JsonProcessingException e) {
            return CompletableFuture.failedFuture(e);
        }
        Request request = new Request.Builder()
                .url("http://localhost:8080/price")
                .post(RequestBody.create(JSON, body))
                .build();
        Call call = client.newCall(request);
        var result = new CompletableFuture<StockPriceResponse>();
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

}
