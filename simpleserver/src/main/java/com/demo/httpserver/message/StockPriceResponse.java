package com.demo.httpserver.message;


import com.demo.httpserver.constant.ResponseCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class StockPriceResponse {
    private UUID requestId;
    private String responseCode;
    private String stockItemName;
    private BigDecimal stockPrice;

    public static StockPriceResponse fail(GetStockPriceRequest request){
        return  StockPriceResponse.builder()
                .requestId(request.getRequestId())
                .responseCode(ResponseCode.FAIL.getCode())
                .build();
    }

    public static StockPriceResponse success(GetStockPriceRequest request, BigDecimal stockPrice){
        return  StockPriceResponse.builder()
                .requestId(request.getRequestId())
                .stockPrice(stockPrice)
                .stockItemName(request.getStockItemName())
                .responseCode(ResponseCode.SUCCESS.getCode())
                .build();
    }

}
