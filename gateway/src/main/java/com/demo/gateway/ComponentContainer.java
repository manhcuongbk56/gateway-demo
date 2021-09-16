package com.demo.gateway;


import com.demo.gateway.business.BusinessHandler;
import com.demo.gateway.business.SimplePrintOutHandler;
import com.demo.gateway.decoder.FlatMessageDecoder;
import com.demo.gateway.schema.SchemaProvider;
import com.demo.gateway.schema.SimpleYamlSchemaLoader;
import com.demo.gateway.util.DataTypeConstants;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ComponentContainer  {

    private final GatewayConfiguration gatewayConfiguration;
    private final FlatMessageRouter flatMessageRouter;
    private final SchemaProvider schemaProvider;
    private final FlatMessageDecoder decoder;
    private final Map<String, BusinessHandler> handlerMap;

    public ComponentContainer(GatewayConfiguration gatewayConfiguration) throws IOException {
        this.gatewayConfiguration = gatewayConfiguration;
        this.schemaProvider = new SimpleYamlSchemaLoader(gatewayConfiguration.getSchemaFile());
        this.decoder = new FlatMessageDecoder(schemaProvider);
        this.handlerMap = new HashMap<>();
        SimplePrintOutHandler simplePrintOutHandler = new SimplePrintOutHandler();
        this.handlerMap.put(DataTypeConstants.A3, simplePrintOutHandler);
        this.flatMessageRouter = new FlatMessageRouter(decoder, handlerMap);
    }



}
