package com.demo.httpserver.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResponseCode {

    SUCCESS("0000"),
    FAIL("9999");


    @Getter
    private String code;

}
