package com.demo.gateway.business;

import java.util.concurrent.CompletableFuture;

public interface BusinessHandler<I, O> {

    CompletableFuture<O> handle(I input);

}
