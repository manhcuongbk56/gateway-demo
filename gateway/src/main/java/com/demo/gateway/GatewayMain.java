package com.demo.gateway;

public class GatewayMain {

    public static void main(String[] args) {
        var server = new NettyServer();
        server.start();
    }
}
