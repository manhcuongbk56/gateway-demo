package com.demo.common.message;


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
    private long stockPrice;

    public static StockPriceResponse fail(GetStockPriceRequest request){
        return  StockPriceResponse.builder()
                .requestId(request.getRequestId())
                .responseCode(ResponseCode.FAIL.getCode())
                .build();
    }

    public static StockPriceResponse success(GetStockPriceRequest request, long stockPrice){
        return  StockPriceResponse.builder()
                .requestId(request.getRequestId())
                .stockPrice(stockPrice)
                .stockItemName(request.getStockItemName())
                .responseCode(ResponseCode.SUCCESS.getCode())
                .build();
    }

}
