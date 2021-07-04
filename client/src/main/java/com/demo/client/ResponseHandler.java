package com.demo.client;

import io.netty.buffer.ByteBuf;

public interface ResponseHandler {

    void handle(ByteBuf byteBuf);

}
