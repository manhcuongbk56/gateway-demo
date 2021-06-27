# A Demo gateway written in Java using Netty
To run this app, you need install Java 11, Maven
## Simple server
Using Spring boot, accept http request and just return the response with error rate is 10%. Run on default port 8080.
## Gateway
Using Netty
Gateway address config variable live in *com.demo.gateway.NettyServer*
```java
    private static final int SERVER_PORT = 6969;
    private static final String HOST = "localhost";
```
We need to point gateway to server, change the variable in *com.demo.gateway.service.GetPriceService* if needed
```java
        public static final String PRICE_URL = "http://localhost:8080/price";
```
## Client
Using Netty.
Config variable to point to gateway and number of client live in ClientMain.java
```java
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 6969;
    private static int NUM_OF_CLIENT = 200;
```
Each client will send a message to gateway. 

