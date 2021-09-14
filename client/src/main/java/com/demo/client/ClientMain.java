package com.demo.client;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log4j2
public class ClientMain {

    private static final String GATEWAY_SERVER_HOST = "localhost";
    private static final int GATEWAY_SERVER_PORT = 6969;
    private static final int CLIENT_QUANTITY = 1;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ClientManager clientManager = new ClientManager(GATEWAY_SERVER_HOST, GATEWAY_SERVER_PORT);
        //Create clients
        List<Client> clients = new ArrayList<>(CLIENT_QUANTITY);
        for (int i = 0; i < CLIENT_QUANTITY; i++) {
            clients.add(clientManager.newClient());
        }
        log.warn("Done create {} clients, connected to server", CLIENT_QUANTITY);
        List<CompletableFuture> responseFutures = new ArrayList<>();
        for (Client client : clients) {
            responseFutures.add(client.getStockPrice("LOL")
                    .thenAccept(rs -> {
                        log.info("Stock price Response: {}", rs);
                    })
                    .exceptionally(ex -> {
                        log.error("Error happen.", ex);
                        return null;
                    }));
        }
        CompletableFuture[] all = new CompletableFuture[responseFutures.size()];
        for (int i = 0; i < responseFutures.size(); i++) {
            all[i] = responseFutures.get(i);
        }
        //Gather all response futures, call get => only return when all request is done.
        CompletableFuture.allOf(all).get();
        log.warn("RECEIVE ALL RESPONSE");
        //never exit to monitoring connected client on gateway server
        while (true) {
            Thread.sleep(2000l);
        }
    }


}
