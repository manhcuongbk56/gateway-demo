package com.demo.httpserver.message;


import lombok.Data;

import java.util.UUID;

@Data
public class GetStockPriceRequest {
    private UUID requestId;
    private String stockItemName;
}
