package echo.server.impl;

import echo.common.FileChangeWatcherService;
import echo.shared.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

/**
 * A TLS enabled server that echoes back any received data from a client.
 *
 * This example is based on the Echo example provided in Netty examples:
 * https://github.com/netty/netty/tree/4.0/example/src/main/java/io/netty/example/echo
 *
 */
@Log4j2
public final class EchoServer extends Thread {

    private final ServerConfig serverConfig;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public EchoServer(ServerConfig config) {
        this.serverConfig = config;
        log.info(this.serverConfig.toString());
    }

    public void close() {
        log.info("Shutting down event loops");
        if (!bossGroup.isShutdown()) {
            // Shut down all event loops to terminate all threads.
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
        }
    }

    @Override
    public void run() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        try {
            // Configure the server
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            log.debug("Done creating server bootstrap");
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    //.handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new EchoServerInitializer(serverConfig));

            // Start the server
            ChannelFuture f = serverBootstrap.bind(serverConfig.getPort()).sync();
            log.info("Server started");

            // Wait until the server socket is closed
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            close();
        } finally {
            close();
        }
    }


}
