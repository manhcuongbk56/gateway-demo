package com.demo.gateway;

public class GatewayMain {

    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        server.start();
    }
}
