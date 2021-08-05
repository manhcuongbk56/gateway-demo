package com.demo.gateway.business;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.gateway.GatewayConfiguration;
import com.demo.gateway.util.CompletableFutureUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.MediaType;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.io.SocketConfig;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.demo.gateway.util.JsonUtils.MAPPER;

@Log4j2
public class ApacheAsyncStockBusinessHandler implements StockBusinessHandler {

    public static final String PRICE_URL = "http://localhost:8080/price";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Random rand = new Random();
    private final CloseableHttpAsyncClient client;
    private final PoolingAsyncClientConnectionManager connectionManager;
    private final RequestConfig requestConfig;
    private final SocketConfig socketConfig;

    public ApacheAsyncStockBusinessHandler(GatewayConfiguration.HttpClientConfiguration config) {

        connectionManager = new PoolingAsyncClientConnectionManager();
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(config.getConnectTimeOut(), TimeUnit.MILLISECONDS)
                .setDefaultKeepAlive(config.getKeepAliveTime(), TimeUnit.MILLISECONDS)
                .setResponseTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .build();
        socketConfig = SocketConfig.custom()
                .setTcpNoDelay(config.isTcpNoDelay())
                .setSoKeepAlive(config.isTcpKeepAlive())
                .build();
        connectionManager.setMaxTotal(config.getMaxConnection());
        client = HttpAsyncClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        client.start();
    }

    @Override
    public CompletableFuture<StockPriceResponse> getPrice(GetStockPriceRequest getStockPriceRequest) {
        log.info("Call from Apache async");
        String body;
        try {
            body = MAPPER.writeValueAsString(getStockPriceRequest);
        } catch (JsonProcessingException ex) {
            return CompletableFutureUtils.failedFuture(ex);
        }
        SimpleHttpRequest request1 = SimpleRequestBuilder.create(Method.POST)
                .setHeader("Accept", "application/json")
                .setHeader("Content-type", "application/json")
                .setBody(body, ContentType.APPLICATION_JSON)
                .setUri(PRICE_URL).build();
        CompletableFuture<StockPriceResponse> result = new CompletableFuture<>();
        client.execute(request1, new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse response) {
                try {
                    result.complete(MAPPER.readValue(response.getBodyText(), StockPriceResponse.class));
                } catch (Exception ex) {
                    result.completeExceptionally(ex);
                }
            }

            @Override
            public void failed(Exception ex) {
                result.completeExceptionally(ex);
            }

            @Override
            public void cancelled() {
                result.completeExceptionally(new Exception("request canceled!!!"));
            }
        });
        return result;
    }


    public boolean isShouldSuccess() {
        int number = rand.nextInt(10);
        return number >= 2;
    }

}
