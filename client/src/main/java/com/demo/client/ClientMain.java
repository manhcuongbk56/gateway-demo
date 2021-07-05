package com.demo.client;

import com.demo.common.message.stockprice.StockPriceResponse;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutionException;

@Log4j2
public class ClientMain {
    private static final String GATEWAY_SERVER_HOST = "localhost";
    private static final int GATEWAY_SERVER_PORT = 6969;


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ClientManager clientManager = new ClientManager(GATEWAY_SERVER_HOST, GATEWAY_SERVER_PORT);
        Client client = clientManager.newClient();
        StockPriceResponse response =   client.getStockPrice("LOL").get();
    }


}
