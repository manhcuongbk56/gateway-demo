package com.demo.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ByteBufUtils {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final byte[] ID_PADDING = new byte[24];
    private static final byte[] PRICE_PADDING = new byte[2];

    public static UUID readUUID(ByteBuf byteBuf) {
        UUID requestId = new UUID(byteBuf.readLong(), byteBuf.readLong());
        byteBuf.skipBytes(24);
        return requestId;
    }

    public static String readString(ByteBuf byteBuf, int length) {
        ByteBuf stringByteBuf = byteBuf.readBytes(length);
        String aString = stringByteBuf.toString(CharsetUtil.UTF_8);
        stringByteBuf.release();
        return aString;
    }

    public static double readNumber(ByteBuf byteBuf, int length) {
        ByteBuf stringByteBuf = byteBuf.readBytes(length);
        String aString = stringByteBuf.toString(CharsetUtil.UTF_8);
        stringByteBuf.release();
        return Double.parseDouble(aString.trim());
    }

    public static UUID writeUUID(ByteBuf byteBuf){
        UUID requestId = UUID.randomUUID();
        writeUUID(byteBuf, requestId);
        return requestId;
    }

    public static void writeDate(ByteBuf byteBuf, LocalDate localDate){
        byte[] dateBytes =  DATE_FORMAT.format(localDate).getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(dateBytes);
    }

    public static void write10BytesDouble(ByteBuf byteBuf, double doubleNumber){
        byteBuf.writeDouble(doubleNumber);
        byteBuf.writeBytes(PRICE_PADDING);
    }

    public static LocalDate readDate(ByteBuf byteBuf){
        byte[] dateBytes = new byte[8];
        byteBuf.readBytes(dateBytes);
        return LocalDate.parse(new String(dateBytes), DATE_FORMAT);
    }

    public static void writeUUID(ByteBuf byteBuf, UUID uuid){
        byteBuf.writeLong(uuid.getMostSignificantBits());
        byteBuf.writeLong(uuid.getLeastSignificantBits());
        byteBuf.writeBytes(ID_PADDING);
    }

    public static void write20BytesString(ByteBuf byteBuf, String stockName){
        byte[] stockNameBytes = stockName.getBytes(StandardCharsets.UTF_8);
        byte[] padding = new byte[20 - stockNameBytes.length];
        Arrays.fill(padding, (byte) ' ');
        byteBuf.writeBytes(stockNameBytes);
        byteBuf.writeBytes(padding);
    }

    public static void writeString(ByteBuf byteBuf, String stockName, int length){
        byte[] stockNameBytes = stockName.getBytes(StandardCharsets.UTF_8);
        byte[] padding = new byte[length - stockNameBytes.length];
        Arrays.fill(padding, (byte) ' ');
        byteBuf.writeBytes(stockNameBytes);
        byteBuf.writeBytes(padding);
    }


    public static void writeDouble(ByteBuf byteBuf, double doubleNumber, int length){
        byteBuf.writeDouble(doubleNumber);
        byte[] padding = new byte[length];
        Arrays.fill(padding, (byte) ' ');
        byteBuf.writeBytes(padding);
    }

    public static String read20BytesString(ByteBuf byteBuf){
        byte[] itemNameByte = new byte[20];
        byteBuf.readBytes(itemNameByte);
        return new String(itemNameByte).trim();
    }

}
