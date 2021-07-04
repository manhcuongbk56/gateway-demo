package com.demo.common.message.stockorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private UUID requestId;
    private String stock;
    private boolean sell;
    private long quantity;
    private double price;
}
