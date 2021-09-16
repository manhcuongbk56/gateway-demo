package com.demo.gateway.schema;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Field {

    private String name;
    private DataType dataType;
    private int length;

}
