package echo.common;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.security.cert.Certificate;

@Log4j2
public class CertUtils {

    /*public static String toString(SslContext ctx) {
        ctx.sessionContext()
    }*/

    public static String toString(Certificate[] certificates) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < certificates.length; i++) {
            builder.append("\nCertificate no: " + (i + 1));
            builder.append(toString(certificates[i]));
        }
        return builder.toString();
    }

    public static String toString(Certificate certificate) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\tCertificate for: " + certificate.getType());
        return builder.toString();
    }
}
