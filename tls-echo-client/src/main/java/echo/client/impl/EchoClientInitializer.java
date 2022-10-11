package echo.client.impl;

import echo.shared.ClientConfig;
import echo.shared.SslContextHelper;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;

import javax.net.ssl.SSLException;

public class EchoClientInitializer extends ChannelInitializer<SocketChannel> {

    private final ClientConfig config;
    private final SslContext sslCtx;

    public EchoClientInitializer(ClientConfig config) throws SSLException {
        this.config = config;
        sslCtx = SslContextHelper.createClientSslContext(config);
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            var handler = sslCtx.newHandler(ch.alloc());
            p.addLast("ssl", handler);
        }
        p.addLast("logHandler", new LoggingHandler(LogLevel.WARN));
        p.remove("logHandler"); // Comment this line if you do want the log handler to be used.
        p.addLast(new EchoClientHandler());
    }

}
