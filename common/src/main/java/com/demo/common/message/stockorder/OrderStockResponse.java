package com.demo.common.message.stockorder;

import com.demo.common.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStockResponse {

    private UUID requestId;
    private String responseCode;
    private long orderNo;

    private OrderStockResponse(UUID requestId, String responseCode){
        this.requestId = requestId;
        this.responseCode = responseCode;
    }

    public static OrderStockResponse fail(UUID requestId){
        return new OrderStockResponse(requestId, ResponseCode.FAIL.getCode());
    }


}
