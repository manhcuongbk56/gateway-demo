package com.demo.common.message.messagetype;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerMessageType {

    public static class Request {
        public static final int GET_PRICE = 1;
        public static final int STOCK_ORDER = 2;
        public static final int CANCEL_ORDER = 3;
    }

    public static class Response {
        public static final int GET_PRICE_RESPONSE = 1;
        public static final int STOCK_ORDER_NO = 2;
        public static final int STOCK_ORDER_COMPLETED = 3;
        public static final int CANCEL_STOCK_ORDER_COMPLETED = 4;
        public static final int STOCK_ORDER_HISTORY = 5;
    }


}
