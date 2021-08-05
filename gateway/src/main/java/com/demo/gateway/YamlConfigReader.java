package com.demo.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlConfigReader<ConfigType> {

    private final Class<ConfigType> type;

    public static <T> YamlConfigReader<T> forType(Class<T> type) {
        return new YamlConfigReader<>(type);
    }

    public ConfigType readYaml(String yamlFilePath) throws IOException {
        Path path = Paths.get(yamlFilePath);
        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(data, type);
    }

    public ConfigType readYamlInResources(String yamlFilePath) throws IOException {
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
        return mapper.readValue(all.toString(), type);
    }


}
