package echo.server;

import echo.server.impl.EchoServer;
import echo.shared.ServerConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {

    public static void main(String... args) {
        // You'll see that the certificatePath and keyPath are set twice. The first set of calls are redundant, but we
        // leave it here so that it is easy to switch context without failing any checkstyle rules. To switch context
        // just change the order.
        ServerConfig config = ServerConfig.builder()
                .port(6969)
                .tlsEnabled(true)
                .useSelfSignedTlsMaterial(false)
                .keyStorePath("config/kafka-proxy.keystore.jks")
                .keyStorePassword("123456")
                .keyStoreType("pkcs12")
                .keyPassword("123456")
                .build();
        EchoServer server = new EchoServer(config);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(server);
    }
}
