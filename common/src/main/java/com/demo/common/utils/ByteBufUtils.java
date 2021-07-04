package com.demo.common.utils;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class ByteBufUtils {


    private static final byte[] ID_PADDING = new byte[24];

    public static UUID readUUID(ByteBuf byteBuf) {
        UUID requestId = new UUID(byteBuf.readLong(), byteBuf.readLong());
        byteBuf.readBytes(24);
        return requestId;
    }

    public static UUID writeUUID(ByteBuf byteBuf){
        UUID requestId = UUID.randomUUID();
        writeUUID(byteBuf, requestId);
        return requestId;
    }

    public static void writeUUID(ByteBuf byteBuf, UUID uuid){
        byteBuf.writeLong(uuid.getMostSignificantBits());
        byteBuf.writeLong(uuid.getLeastSignificantBits());
        byteBuf.writeBytes(ID_PADDING);;
    }

    public static void write20BytesString(ByteBuf byteBuf, String stockName){
        byte[] stockNameBytes = stockName.getBytes(StandardCharsets.UTF_8);
        byte[] padding = new byte[20 - stockNameBytes.length];
        Arrays.fill(padding, (byte) ' ');
        byteBuf.writeBytes(stockNameBytes);
        byteBuf.writeBytes(padding);
    }

    public static String read20BytesString(ByteBuf byteBuf){
        byte[] itemNameByte = new byte[20];
        byteBuf.readBytes(itemNameByte);
        return new String(itemNameByte).trim();
    }


}
