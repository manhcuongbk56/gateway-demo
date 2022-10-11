package echo.server.impl;

import echo.shared.ServerConfig;
import echo.shared.SslContextHelper;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

@Log4j2
public class EchoServerInitializer extends ChannelInitializer<SocketChannel> {

    @NonNull
    private final ServerConfig config;
    private SslContext sslCtx;

    public EchoServerInitializer(@NonNull ServerConfig config) {
        this.config = config;
        try {
            this.sslCtx = SslContextHelper.createServerSslContext(config);
        } catch (Throwable e) {
            log.error("Error when load ssl context for server", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * This method is called once when the Channel is registered.
     *
     * @param ch
     * @throws Exception
     */
    @Override
    public void initChannel(final SocketChannel ch) throws Exception {
        log.info("Initializing channel");
        Channels.add(ch);

        final ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast("ssl", sslCtx.newHandler(ch.alloc()));
            log.info("Done adding SSL Context handler to the pipeline.");
        }
        pipeline.addLast("logHandler", new LoggingHandler(LogLevel.INFO));
        pipeline.remove("logHandler"); // Comment this line if you do want the log handler to be used.
        pipeline.addLast("app", new EchoServerHandler()); // business logic handler.
        log.info("Done adding App handler to the pipeline.");
        log.info(pipeline.toString());
    }

}
