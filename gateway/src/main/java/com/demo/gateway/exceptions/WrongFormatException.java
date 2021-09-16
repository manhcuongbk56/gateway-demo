package com.demo.gateway.exceptions;

import com.demo.gateway.schema.Field;
import io.netty.buffer.ByteBuf;

public class WrongFormatException extends RuntimeException{

    private ByteBuf data;
    private Field field;


    public WrongFormatException(ByteBuf data, Field field, Throwable ex) {
        super(ex);
        this.data = data;
        this.field = field;
    }
}
