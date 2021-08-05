package com.demo.gateway.business;

import com.demo.common.constant.ResponseCode;
import com.demo.common.message.cancelorder.CancelStockOrderRequest;
import com.demo.common.message.cancelorder.CancelStockOrderResponse;
import com.demo.common.message.orderhistory.GetStockOrderHistoryRequest;
import com.demo.common.message.orderhistory.GetStockOrderHistoryResponse;
import com.demo.common.message.stockorder.OrderStockCompleted;
import com.demo.common.message.stockorder.OrderStockRequest;
import com.demo.common.message.stockorder.OrderStockResponse;
import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface StockBusinessHandler {

    CompletableFuture<StockPriceResponse> getPrice(GetStockPriceRequest getStockPriceRequest);

    default boolean isShouldSuccess() {
        return true;
    }


    default CompletableFuture<OrderStockResponse> orderStock(OrderStockRequest request) {
        if (isShouldSuccess()) {
            return CompletableFuture.completedFuture(new OrderStockResponse(request.getRequestId(), ResponseCode.SUCCESS.getCode(), 1234));
        }
        return CompletableFuture.completedFuture(OrderStockResponse.fail(request.getRequestId()));
    }

    default CompletableFuture<CancelStockOrderResponse> cancelStockOrder(CancelStockOrderRequest request) {
        if (isShouldSuccess()) {
            return CompletableFuture.completedFuture(new CancelStockOrderResponse(request.getRequestId(), ResponseCode.SUCCESS.getCode()));
        }
        return CompletableFuture.completedFuture(new CancelStockOrderResponse(request.getRequestId(), ResponseCode.FAIL.getCode()));
    }

    default CompletableFuture<GetStockOrderHistoryResponse> getStockOrderHistory(GetStockOrderHistoryRequest request) {
        if (isShouldSuccess()) {
            return CompletableFuture.completedFuture(generateSimpleHistory(request.getRequestId()));
        }
        return CompletableFuture.completedFuture(GetStockOrderHistoryResponse.builder()
                .requestId(request.getRequestId())
                .days(new ArrayList<>())
                .build());
    }

    default CompletableFuture<OrderStockCompleted> stockOrderCompleted(Object unused) {
        OrderStockCompleted orderStockCompleted = new OrderStockCompleted(123L);
        return CompletableFuture.completedFuture(orderStockCompleted);
    }


    default GetStockOrderHistoryResponse generateSimpleHistory(UUID requestId) {
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
                .requestId(requestId)
                .day(day1)
                .day(day2)
                .build();
    }


}
