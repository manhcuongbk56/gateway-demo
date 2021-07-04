package com.demo.common.message.stockorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStockRequest {

    private UUID requestId;
    private String stock;
    private String sellOrBuy;
    private long quantity;
    private double price;
}
