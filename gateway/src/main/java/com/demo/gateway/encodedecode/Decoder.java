package com.demo.gateway.encodedecode;

import io.netty.buffer.ByteBuf;

public interface Decoder<T> {

    T decode(ByteBuf byteBuf);

}
