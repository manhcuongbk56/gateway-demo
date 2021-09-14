package com.demo.gateway.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ByteBufUtils {

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


}
