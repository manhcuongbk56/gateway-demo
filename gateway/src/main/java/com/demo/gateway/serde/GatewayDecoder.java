package com.demo.gateway.serde;

import io.netty.buffer.ByteBuf;

public interface GatewayDecoder<T> {

    T decode(ByteBuf byteBuf);

}
