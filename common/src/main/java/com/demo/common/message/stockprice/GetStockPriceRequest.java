package com.demo.common.message.stockprice;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStockPriceRequest {
    private UUID requestId;
    private String stockItemName;
}
