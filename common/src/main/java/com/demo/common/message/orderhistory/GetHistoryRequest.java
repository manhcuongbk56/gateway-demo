package com.demo.common.message.orderhistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetHistoryRequest {
    private UUID requestId;
    private long clientAccountNo;
    private OffsetDateTime from;
    private OffsetDateTime to;

}
