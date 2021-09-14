package com.demo.gateway.business;

import java.util.Map;
import java.util.concurrent.CompletionStage;

public interface BusinessHandler {

    public CompletionStage<Object> handle(Map<String, Object> data);

}
