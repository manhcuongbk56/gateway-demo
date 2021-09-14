package com.demo.gateway.exceptions;

public class UnknownMessageType extends RuntimeException{

    private String messageType;

    public UnknownMessageType(String messageType) {
        this.messageType = messageType;
    }
}
