package com.demo.gateway.business;

import java.util.concurrent.CompletableFuture;

/**
 * Business handler, our business handles need to install this interface
 * @param <I>
 * @param <O>
 */
public interface BusinessHandler<I, O> {

    /**
     * handle logic here. Because we use async api so, this method return a {@link CompletableFuture}
     * @param input the input
     * @return a {@link CompletableFuture} of result
     */
    CompletableFuture<O> handle(I input);

}
