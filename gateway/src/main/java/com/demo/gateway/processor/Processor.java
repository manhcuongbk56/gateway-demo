package com.demo.gateway.processor;

import com.demo.gateway.business.BusinessHandler;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.CompletableFuture;

/**
 * an abstract processor class which use template method pattern
 * for inherited class, just need to install decode, encode, getHandler method
 * @param <I> the input, we will decode byteBuf to this  type
 * @param <O> the output, our response class, we will encode this to byteBuf
 */
public interface Processor<I, O> {

    /**
     * decode byteBuf to concrete request for business logic
     * @param body byteBuf input
     * @return the request as input for handler
     */
    I decode(ByteBuf body);

    /**
     * encode response from handler to byteBuf
     * @param body the response
     * @return a byteBuf to send to client
     */
    ByteBuf encode(O body);

    /**
     * return handler for this processor
     * @return a business handle
     */
    BusinessHandler<I, O> getHandler();

    /**
     * template method, decode the request input
     * call handle of handler
     * encode response
     * @param body request
     * @return {@link CompletableFuture} of byteBuf, which completed when the handler handle completed.
     */
    default CompletableFuture<ByteBuf> process(ByteBuf body) {
        I input = decode(body);
        body.release();
        return getHandler().handle(input)
                // when handle done, apply encoding response
                .thenApply(this::encode);
    }

}
