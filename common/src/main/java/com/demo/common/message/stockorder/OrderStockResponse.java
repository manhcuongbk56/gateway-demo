package com.demo.common.message.stockorder;

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

}
