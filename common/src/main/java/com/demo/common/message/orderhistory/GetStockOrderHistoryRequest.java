package com.demo.common.message.orderhistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetStockOrderHistoryRequest {
    private UUID requestId;
    private long clientAccountNo;
    private LocalDate from;
    private LocalDate to;

}
