package echo.client;

import io.netty.handler.ssl.SslContext;
import lombok.extern.log4j.Log4j2;

import javax.net.ssl.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;

import static echo.shared.SslContextHelper.loadKeyStore;
import static java.util.Arrays.stream;
import static javax.net.ssl.TrustManagerFactory.getDefaultAlgorithm;

@Log4j2
public class SimpleTCPClient {

    public static void main(String[] args) {
//        System.setProperty("javax.net.ssl.trustStore", "config/kafka-proxy.truststore.jks");
//        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
//        SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        KeyStore keyStore = null;
        try {
            keyStore = loadKeyStore("config/kafka-proxy.truststore.jks", "123456", "pkcs12");
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        var sslContext = createSslContextForClient(keyStore);

        SSLSocketFactory ssf =   sslContext.getSocketFactory();
        Socket s = null;
        try {
            s = ssf.createSocket("localhost", 6969);
        } catch (IOException e) {
            log.error("Error happen when creating socket", e);
            throw new RuntimeException(e);
        }

        SSLSession session = ((SSLSocket) s).getSession();
        Certificate[] cchain = new Certificate[0];
        try {
            cchain = session.getPeerCertificates();
        } catch (SSLPeerUnverifiedException e) {
            throw new RuntimeException(e);
        }
        var first = cchain[0];
        System.out.println("The Certificates used by peer");
        for (int i = 0; i < cchain.length; i++) {
            var cert = (X509Certificate) cchain[i];
            log.info("number {}: {}", i + 1, cert);
            log.info("Cert info: \n" +
                    " not after: {}, not before: {}", cert.getNotAfter(), cert.getNotBefore());
        }
        System.out.println("Peer host is " + session.getPeerHost());
        System.out.println("Cipher is " + session.getCipherSuite());
        System.out.println("Protocol is " + session.getProtocol());
        System.out.println("ID is " + new BigInteger(session.getId()));
        System.out.println("Session created in " + session.getCreationTime());
        System.out.println("Session accessed in "
                + session.getLastAccessedTime());

    }


    public static SSLContext createSslContextForClient(KeyStore keyStore) {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            try {
                trustManagerFactory.init(keyStore);
            } catch (KeyStoreException e) {
                throw new RuntimeException(e);
            }
            final TrustManager[] trustManagers = createTrustManagers(keyStore);
            sc.init(null, trustManagers, new SecureRandom());
            return sc;
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize upstream SSL handler", e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    static X509TrustManager[] createTrustManagers(final KeyStore keyStore) {
        try {
            final TrustManagerFactory factory = TrustManagerFactory.getInstance(getDefaultAlgorithm());
            factory.init(keyStore);
            return stream(factory.getTrustManagers())
                    .filter(it -> it instanceof X509TrustManager)
                    .map(it -> (X509TrustManager) it)
                    .toArray(X509TrustManager[]::new);
        } catch (final KeyStoreException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to setup trust manager", e);
        }
    }


}
