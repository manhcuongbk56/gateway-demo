package com.demo.gateway.encodedecode;

import io.netty.buffer.ByteBuf;

public interface Encoder<T> {

    ByteBuf encode(T t);

}
