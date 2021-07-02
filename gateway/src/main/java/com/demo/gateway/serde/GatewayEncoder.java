package com.demo.gateway.serde;

import io.netty.buffer.ByteBuf;

public interface GatewayEncoder<T> {

    ByteBuf encode(T t);

}
