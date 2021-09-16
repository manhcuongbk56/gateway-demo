package com.demo.gateway;

import io.netty.channel.ChannelHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

@ChannelHandler.Sharable
public interface MessageRouter<K> {

    CompletionStage<Object> routeToProcessor(Map<String, Object> data);

}
