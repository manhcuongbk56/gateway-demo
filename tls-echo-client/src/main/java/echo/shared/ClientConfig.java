package echo.shared;

import echo.common.FileUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ClientConfig {
    @Builder.Default
    private String serverHost = "localhost";
        // "127.0.0.1";

    @Builder.Default
    private int serverPort = ServerConfig.builder().build().getPort();


    @Builder.Default
    private boolean enableTls = true;

    @Builder.Default
    private boolean useSelfSignedTlsMaterial = false;

    @Builder.Default
    private boolean loggingHandlerEnabled = false;
    private String trustStorePath;
    private String trustStorePassword;
    private String trustStoreType;
}
