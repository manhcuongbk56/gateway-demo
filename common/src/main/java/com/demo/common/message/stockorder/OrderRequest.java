package com.demo.common.message.stockorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String stock;
    private String orderType;
    private Map<String, Object> orderInfo;

}
