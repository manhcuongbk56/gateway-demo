package com.demo.gateway;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GatewayConfiguration {

    private HttpClientConfiguration httpClient;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum ClientType {
        OKHTTP_SYNC,
        OKHTTP_ASYNC,
        APACHE_SYNC,
        APACHE_ASYNC,

    }

    @Getter
    @NoArgsConstructor
    public static class HttpClientConfiguration {
        private ClientType clientType;
        private int maxConnection;
        private long keepAliveTime;
        private long connectTimeOut;
        private long readTimeout;
        private long writeTimeout;

    }

}

