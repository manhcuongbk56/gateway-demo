package com.demo.gateway.processor;

import com.demo.gateway.business.BusinessHandler;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.CompletableFuture;

public interface Processor<I, O> {

    I decode(ByteBuf body);

    ByteBuf encode(O body);

    BusinessHandler<I, O> getHandler();


    default CompletableFuture<ByteBuf> process(ByteBuf body) {
        I input = decode(body);
        return getHandler().handle(input)
                .thenApply(this::encode);
    }

}
