package com.demo.common.message.cancelorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderRequest {

    private UUID requestId;
    private long orderNo;

}
