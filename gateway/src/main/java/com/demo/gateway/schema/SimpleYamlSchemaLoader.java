package com.demo.gateway.schema;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SimpleYamlSchemaLoader implements SchemaProvider{


    private Map<String, Schema> schemas;

    public SimpleYamlSchemaLoader(String file) throws IOException {
        this.schemas = readYamlInResources(file);
    }

    @Override
    public Schema getSchema(String dataType) {
        return schemas.get(dataType);
    }


    public Map<String, Schema> readYamlInResources(String yamlFilePath) throws IOException {
        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(yamlFilePath);
        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + yamlFilePath);
        }
        StringBuilder all = new StringBuilder();
        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                all.append(line);
                all.append("\n");
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("error when reading file " + yamlFilePath);
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(all.toString(), new TypeReference<Map<String, Schema>>() {
        });
    }
}
