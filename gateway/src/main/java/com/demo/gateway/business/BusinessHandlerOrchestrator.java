package com.demo.gateway.business;

import com.demo.gateway.exceptions.UnknownMessageType;
import com.demo.gateway.util.FieldConstants;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

public class BusinessHandlerOrchestrator {

    private Map<String, BusinessHandler> handlers;

    public BusinessHandlerOrchestrator(Map<String, BusinessHandler> handlers) {
        this.handlers = handlers;
    }


    public CompletionStage<Object> handle(Map<String, Object> data){
        String dataType = (String) data.get(FieldConstants.DATA_TYPE);
        BusinessHandler handler = handlers.get(dataType);
        if (Objects.isNull(handler)){
            throw new UnknownMessageType(dataType);
        }
        return handler.handle(data);
    }

}
