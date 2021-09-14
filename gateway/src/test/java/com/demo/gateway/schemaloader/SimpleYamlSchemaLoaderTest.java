package com.demo.gateway.schemaloader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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