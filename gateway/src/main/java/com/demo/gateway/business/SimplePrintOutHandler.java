package com.demo.gateway.business;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SimplePrintOutHandler implements BusinessHandler{
    @Override
    public CompletionStage<Object> handle(Map<String, Object> data) {
        System.out.println("DATA: ");
        System.out.println(data);
        return CompletableFuture.completedFuture("Done");
    }
}
