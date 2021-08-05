package com.demo.gateway.business;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import com.demo.gateway.GatewayConfiguration;
import com.demo.gateway.util.CompletableFutureUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.MediaType;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.demo.gateway.util.JsonUtils.MAPPER;

@Log4j2
public class ApacheSyncStockBusinessHandler implements StockBusinessHandler {

    public static final String PRICE_URL = "http://localhost:8080/price";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Random rand = new Random();
    private final CloseableHttpClient client;
    private final PoolingHttpClientConnectionManager connectionManager;
    private final RequestConfig requestConfig;
    private final SocketConfig socketConfig;

    public ApacheSyncStockBusinessHandler(GatewayConfiguration.HttpClientConfiguration config) {
        connectionManager = new PoolingHttpClientConnectionManager();
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
        connectionManager.setDefaultSocketConfig(socketConfig);
        client = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

    }

    @Override
    public CompletableFuture<StockPriceResponse> getPrice(GetStockPriceRequest getStockPriceRequest) {
        log.info("Call from Apache sync");
        final HttpPost httpPost = new HttpPost(PRICE_URL);
        String body;
        try {
            body = MAPPER.writeValueAsString(getStockPriceRequest);
        } catch (JsonProcessingException ex) {
            return CompletableFutureUtils.failedFuture(ex);
        }
        StringEntity entity = new StringEntity(body);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        try (final CloseableHttpResponse response = client.execute(httpPost)) {
            HttpEntity httpEntity = response.getEntity();
            if (Objects.isNull(httpEntity)) {
                return CompletableFutureUtils.failedFuture(new Exception("failed request"));
            }
            StockPriceResponse priceResponse = MAPPER.readValue(EntityUtils.toString(httpEntity), StockPriceResponse.class);
            return CompletableFuture.completedFuture(priceResponse);
        } catch (IOException | ParseException ex) {
            return CompletableFutureUtils.failedFuture(ex);
        }
    }


    public boolean isShouldSuccess() {
        int number = rand.nextInt(10);
        return number >= 2;
    }

}
