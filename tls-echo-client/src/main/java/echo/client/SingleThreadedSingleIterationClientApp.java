package echo.client;

import echo.client.impl.EchoClient;
import echo.common.FileUtils;
import echo.shared.ClientConfig;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadedSingleIterationClientApp {

    public static void main(String[] args) {

        // You'll see that the trustedCertificatePath is set twice. The first call is redundant, but we
        // leave it here so that it is easy to switch context without failing any checkstyle rules. To switch context
        // just change the order of the statement.
        ClientConfig config = ClientConfig.builder()
                .enableTls(true)
                .useSelfSignedTlsMaterial(false)
                .serverHost("localhost")
                .serverPort(6969)
                .trustStorePath("config/kafka-proxy.truststore.jks")
                .trustStoreType("pkcs12")
                .trustStorePassword("123456")
                .build();
        //ClientConfig config = ClientConfig.builder().build();

        EchoClient client = new EchoClient(config, channel -> {
            try {
                channel.writeAndFlush(Unpooled.copiedBuffer("Ping!! ", CharsetUtil.UTF_8)).sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(client);
    }
}
