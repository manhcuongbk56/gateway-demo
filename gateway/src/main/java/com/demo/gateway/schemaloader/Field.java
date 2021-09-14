package com.demo.gateway.schemaloader;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Field {

    private String name;
    private DataType dataType;
    private int length;

}
