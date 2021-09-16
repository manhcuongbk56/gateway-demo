package com.demo.gateway.exceptions;

import com.demo.gateway.schema.DataType;

public class UnknownDataTypeException extends RuntimeException{

    private DataType dataType;

    public UnknownDataTypeException(DataType dataType) {
        this.dataType = dataType;
    }
}
