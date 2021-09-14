package com.demo.gateway;

import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.exceptions.UnknownMessageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ChannelHandler.Sharable
public interface MessageRouter<K> {

    Map<K, BusinessHandler> getProcessors();


//    default CompletableFuture<ByteBuf> handleRaw(K key, ByteBuf input) {
//        //Get the right processor by key and then call process
//        if (getProcessors().containsKey(key)) {
//            return getProcessors().get(key).process(input);
//        }
//        throw new UnknownMessageType();
//    }


}
