package com.demo.gateway.business;

import com.demo.common.constant.ResponseCode;
import com.demo.common.message.cancelorder.CancelStockOrderRequest;
import com.demo.common.message.cancelorder.CancelStockOrderResponse;
import com.demo.common.message.orderhistory.GetStockOrderHistoryRequest;
import com.demo.common.message.orderhistory.GetStockOrderHistoryResponse;
import com.demo.common.message.stockorder.OrderStockRequest;
import com.demo.common.message.stockorder.OrderStockResponse;
import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.demo.gateway.util.JsonUtils.MAPPER;

public class StockBusinessHandler {

    private Random rand = new Random();
    public static final String PRICE_URL = "http://localhost:8080/price";

    OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

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

    public CompletableFuture<OrderStockResponse> orderStock(OrderStockRequest request) {
        if (isShouldSuccess()) {
            return CompletableFuture.completedFuture(new OrderStockResponse(request.getRequestId(), ResponseCode.SUCCESS.getCode(), 1234));
        }
        return CompletableFuture.completedFuture(OrderStockResponse.fail(request.getRequestId()));
    }

    public CompletableFuture<CancelStockOrderResponse> cancelStockOrder(CancelStockOrderRequest request) {
        if (isShouldSuccess()) {
            return CompletableFuture.completedFuture(new CancelStockOrderResponse(request.getRequestId(), ResponseCode.SUCCESS.getCode()));
        }
        return CompletableFuture.completedFuture(new CancelStockOrderResponse(request.getRequestId(), ResponseCode.FAIL.getCode()));
    }

    public CompletableFuture<GetStockOrderHistoryResponse> getStockOrderHistory(GetStockOrderHistoryRequest request) {
        if (isShouldSuccess()) {
            return CompletableFuture.completedFuture(generateSimple());
        }
        return CompletableFuture.completedFuture(GetStockOrderHistoryResponse.builder()
                .build());
    }

    private GetStockOrderHistoryResponse generateSimple() {
        GetStockOrderHistoryResponse.StockOrderInfo info1 = GetStockOrderHistoryResponse.StockOrderInfo.builder()
                .stock("ABC")
                .sellOrBuy("sell")
                .quantity(10L)
                .price(50.3)
                .isSuccess(true)
                .build();
        GetStockOrderHistoryResponse.StockOrderInfo info2 = GetStockOrderHistoryResponse.StockOrderInfo.builder()
                .stock("DEF")
                .sellOrBuy("buy")
                .quantity(10L)
                .price(50.3)
                .isSuccess(false)
                .build();
        GetStockOrderHistoryResponse.StockOrderInfo info3 = GetStockOrderHistoryResponse.StockOrderInfo.builder()
                .stock("HIJ")
                .sellOrBuy("buy")
                .quantity(10L)
                .price(50.3)
                .isSuccess(false)
                .build();
        GetStockOrderHistoryResponse.StockOrderInfo info4 = GetStockOrderHistoryResponse.StockOrderInfo.builder()
                .stock("LOL")
                .sellOrBuy("sell")
                .quantity(10L)
                .price(50.3)
                .isSuccess(false)
                .build();
        GetStockOrderHistoryResponse.DayHistory day1 = GetStockOrderHistoryResponse.DayHistory.builder()
                .day(LocalDate.now())
                .order(info1)
                .order(info2)
                .build();
        GetStockOrderHistoryResponse.DayHistory day2 = GetStockOrderHistoryResponse.DayHistory.builder()
                .day(LocalDate.now().plusDays(1L))
                .order(info3)
                .order(info4)
                .build();
        return GetStockOrderHistoryResponse.builder()
                .day(day1)
                .day(day2)
                .build();
    }


    private boolean isShouldSuccess() {
        int number = rand.nextInt(10);
        return number >= 2;
    }

}
