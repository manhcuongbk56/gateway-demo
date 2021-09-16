package com.demo.gateway;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class GatewayMain {

    public static void main(String[] args) {
        //Start a server
        NettyServer server = new NettyServer();
        YamlConfigReader<GatewayConfiguration> configReader = YamlConfigReader.forType(GatewayConfiguration.class);
        try {
            GatewayConfiguration config = configReader.readYamlInResources("config.yaml");
            server.start(config);
        } catch (IOException | InterruptedException e) {
            log.error("Error happen when read config file", e);
            System.exit(99);
        }
    }
}
