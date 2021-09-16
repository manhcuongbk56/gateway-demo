package com.demo.gateway.schema;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SimpleYamlSchemaLoaderTest {

    private static SchemaProvider schemaProvider;

    @BeforeAll
    static void beforeAll() throws IOException {
        schemaProvider = new SimpleYamlSchemaLoader("schema.yaml");
    }


    @Test
    void name() {
        System.out.println("AAAAAAAAAAA");
        System.out.println(schemaProvider.getSchema("A3"));
    }
}