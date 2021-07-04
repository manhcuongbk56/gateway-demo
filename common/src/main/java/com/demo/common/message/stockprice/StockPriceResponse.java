package com.demo.common.message.stockprice;


import com.demo.common.constant.ResponseCode;
import lombok.*;

import java.util.UUID;

@Data
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class StockPriceResponse {
    private UUID requestId;
    private String responseCode;
    private String stockItemName;
    private double stockPrice;

    public static StockPriceResponse fail(GetStockPriceRequest request){
        return  StockPriceResponse.builder()
                .requestId(request.getRequestId())
                .responseCode(ResponseCode.FAIL.getCode())
                .build();
    }

    public static StockPriceResponse fail(UUID requestId){
        return  StockPriceResponse.builder()
                .requestId(requestId)
                .responseCode(ResponseCode.FAIL.getCode())
                .build();
    }

    public static StockPriceResponse success(GetStockPriceRequest request, double stockPrice){
        return  StockPriceResponse.builder()
                .requestId(request.getRequestId())
                .stockPrice(stockPrice)
                .stockItemName(request.getStockItemName())
                .responseCode(ResponseCode.SUCCESS.getCode())
                .build();
    }

    public static StockPriceResponse success(UUID requestId, String stockItemName, double stockPrice){
        return  StockPriceResponse.builder()
                .requestId(requestId)
                .stockPrice(stockPrice)
                .stockItemName(stockItemName)
                .responseCode(ResponseCode.SUCCESS.getCode())
                .build();
    }


}
