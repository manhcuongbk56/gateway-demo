package com.demo.common.message.orderhistory;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStockOrderHistoryResponse {

    private UUID requestId;
    @Singular
    private List<DayHistory> days;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DayHistory {

        private LocalDate day;
        @Singular
        private List<StockOrderInfo> orders;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StockOrderInfo {

        private String stock;
        private String sellOrBuy;
        private long quantity;
        private double price;
        private boolean isSuccess;

    }

}


