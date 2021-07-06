package com.demo.client;

import com.demo.common.message.cancelorder.CancelStockOrderResponse;
import com.demo.common.message.orderhistory.GetStockOrderHistoryResponse;
import com.demo.common.message.stockorder.OrderStockResponse;
import com.demo.common.message.stockprice.StockPriceResponse;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Log4j2
public class ClientMain {
    private static final String GATEWAY_SERVER_HOST = "localhost";
    private static final int GATEWAY_SERVER_PORT = 6969;


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ClientManager clientManager = new ClientManager(GATEWAY_SERVER_HOST, GATEWAY_SERVER_PORT);
        Client client = clientManager.newClient();
        StockPriceResponse stockPriceResponse =   client.getStockPrice("LOL").get();
        log.info("Stock price Resonse: {}", stockPriceResponse);
        OrderStockResponse orderStockResponse =   client.orderStock("ABC", "sell", 10L, 1.5).get();
        log.info("Order stock response: {}", orderStockResponse);
        CancelStockOrderResponse cancelStockOrderResponse =   client.cancelStockOrderRequest(123l).get();
        log.info("Cancel stock order response: {}", cancelStockOrderResponse);
        GetStockOrderHistoryResponse getStockOrderHistoryResponse =   client.getOrderHistory(123l, LocalDate.now(), LocalDate.now()).get();
        log.info("Get stock order history response: {}", getStockOrderHistoryResponse);
    }


}
