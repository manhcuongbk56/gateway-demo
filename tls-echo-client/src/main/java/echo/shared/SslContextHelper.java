package echo.shared;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.log4j.Log4j2;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Log4j2
public class SslContextHelper {

    public static SslContext createServerSslContext(ServerConfig config) throws CertificateException, SSLException {
        SslContext result = null;

        if (config.isTlsEnabled()) {
            log.trace("Creating an SSL Context.");
            if (config.isUseSelfSignedTlsMaterial()) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                result = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
                log.debug("Done creating SSL Context using auto-generated self-signed material");
            } else {
                KeyManagerFactory keyManagerFactor = null;
                try {
                    var keyStore = loadKeyStore(config);
                    keyManagerFactor = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    keyManagerFactor.init(keyStore, config.getKeyPassword().toCharArray());
                    result = SslContextBuilder.forServer(keyManagerFactor)
                            .build();
                } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | IOException e) {
                    log.error("Error happen when load key store for server", e);
                    throw new RuntimeException(e);
                }
                //result.
                log.debug("Done creating SSL Context using supplied material");
            }
        }
        return result;
    }

    public static KeyStore loadKeyStore(ServerConfig serverConfig)
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        return loadKeyStore(serverConfig.getKeyStorePath(), serverConfig.getKeyStorePassword(), serverConfig.getKeyStoreType());
    }


    public static KeyStore loadKeyStore(String path, String password, String type)
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        final char[] keyStorePassword = password.toCharArray();
        final KeyStore keyStore = KeyStore.getInstance(type);
        try (final InputStream is = new FileInputStream(new File(path))) {
            keyStore.load(is, keyStorePassword);
        } catch (Throwable throwable) {
            log.error("Error happen when load keystore", throwable);
            throw new RuntimeException(throwable);
        }
        return keyStore;
    }


    public static SslContext createClientSslContext(ClientConfig config) throws SSLException {
        final SslContext sslCtx;
        if (config.isEnableTls()) {
            log.debug("Creating an SSL Context...");
            if (config.isUseSelfSignedTlsMaterial()) {
                sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                log.debug("Done creating SSL Context using auto-generated self-signed material");
            } else {
                TrustManagerFactory trustManagerFactory = null;
                KeyStore trustStore = null;
                try {
                    trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustStore = loadKeyStore(config.getTrustStorePath(), config.getTrustStorePassword(), config.getTrustStoreType());
                    trustManagerFactory.init(trustStore);
                } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e) {
                    log.error("Error happen when load key store", e);
                    throw new RuntimeException(e);
                }
                sslCtx = SslContextBuilder.forClient().trustManager(trustManagerFactory).build();
                log.debug("Done creating SSL Context using supplied material");
            }
        } else {
            sslCtx = null;
        }
        return sslCtx;
    }

}
