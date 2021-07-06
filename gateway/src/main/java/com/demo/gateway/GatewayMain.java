package com.demo.gateway;

public class GatewayMain {

    public static void main(String[] args) {
        //Start a server
        NettyServer server = new NettyServer();
        server.start();
    }
}
