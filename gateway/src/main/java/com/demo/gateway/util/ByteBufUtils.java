package com.demo.gateway.util;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class ByteBufUtils {

    public static UUID readUUID(ByteBuf byteBuf) {
        return new UUID(byteBuf.readLong(), byteBuf.readLong());
    }

}
