package echo.shared;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ServerConfig {

    @Builder.Default
    private int port = 8080;

    @Builder.Default
    private boolean tlsEnabled = true;

    @Builder.Default
    private boolean useSelfSignedTlsMaterial = false;

    private String keyStorePath;
    private String keyPassword;
    private String keyStorePassword;
    private String keyStoreType;

    @Builder.Default
    private boolean loggingHandlerEnabled = false;
}
