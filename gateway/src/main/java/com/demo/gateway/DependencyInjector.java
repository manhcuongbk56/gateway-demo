package com.demo.gateway;


import com.demo.gateway.decoder.Decoder;
import com.demo.gateway.decoder.FlatMessageDecoder;
import com.demo.gateway.schemaloader.SchemaProvider;
import com.demo.gateway.schemaloader.SimpleYamlSchemaLoader;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.io.IOException;

public class DependencyInjector extends AbstractModule {

    private final GatewayConfiguration gatewayConfiguration;

    public DependencyInjector(GatewayConfiguration gatewayConfiguration) {
        this.gatewayConfiguration = gatewayConfiguration;
    }

    @Override
    protected void configure() {
        bind(KeyIntegerMessageRouter.class).in(Singleton.class);
        bind(SchemaProvider.class).in(Singleton.class);
        bind(Decoder.class).to(FlatMessageDecoder.class).in(Singleton.class);
    }


    @Provides
    public SchemaProvider schemaProvider() throws IOException {
        return new SimpleYamlSchemaLoader("schema.yaml");
    }


}
