package echo.client;

import echo.client.impl.EchoClient;
import echo.common.FileUtils;
import echo.shared.ClientConfig;
import io.netty.buffer.Unpooled;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
public class SingleThreadedMultiIterationClientApp {

    public static void main(String[] args) {

        // You'll see that the trustedCertificatePath is set twice. The first call is redundant, but we
        // leave it here so that it is easy to switch context without failing any checkstyle rules. To switch context
        // just change the order of the statement.
        ClientConfig config = ClientConfig.builder()
                .enableTls(true)
                .useSelfSignedTlsMaterial(true)
                .serverHost("localhost")
                .serverPort(6969)
                .trustStorePath("config/kafka-proxy.truststore.jks")
                .trustStoreType("pkcs12")
                .trustStorePassword("123456")
                .build();
        //ClientConfig config = ClientConfig.builder().build();

        EchoClient client = new EchoClient(config, channel -> {
            // Connection established successfully. Now, write the message(s).
            for (int i = 1; i < 51; i++) {
                try {
                    SslHandler sslhandler = (SslHandler) channel.pipeline().get("ssl");
                    try {
                        var cert =  (X509Certificate) sslhandler.engine().getSession().getPeerCertificates()[0];
                        log.info("Cert info: \n" +
                                " not after: {}, not before: {}", cert.getNotAfter(), cert.getNotBefore());
                        log.info("All cert info: {}", cert);
                    } catch (SSLPeerUnverifiedException e) {
//                        throw new RuntimeException(e);
                    };

                    channel.writeAndFlush(Unpooled.copiedBuffer("Ping no. " + i, CharsetUtil.UTF_8)).sync();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(client);
    }
}
