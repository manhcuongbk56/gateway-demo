package com.demo.gateway;

import com.demo.gateway.exceptions.UnknownMessageType;
import com.demo.gateway.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ChannelHandler.Sharable
public interface MessageRouter<K> {

    Map<K, Processor> getProcessors();


    default CompletableFuture<ByteBuf> handleRaw(K key, ByteBuf input) {
        if (getProcessors().containsKey(key)) {
            return getProcessors().get(key).process(input);
        }
        throw new UnknownMessageType();
    }


}
