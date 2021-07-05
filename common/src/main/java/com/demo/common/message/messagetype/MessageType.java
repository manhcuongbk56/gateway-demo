package com.demo.common.message.messagetype;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageType {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {
        public static final int GET_PRICE = 1;
        public static final int STOCK_ORDER = 2;
        public static final int CANCEL_ORDER = 3;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        public static final int GET_PRICE_RESPONSE = 1;
        public static final int ORDER_STOCK_COMPLETED = 2;
        public static final int ORDER_STOCK_RESPONSE = 3;
        public static final int CANCEL_STOCK_ORDER_RESPONSE = 4;
        public static final int GET_STOCK_ORDER_HISTORY_RESPONSE = 5;
    }


}
