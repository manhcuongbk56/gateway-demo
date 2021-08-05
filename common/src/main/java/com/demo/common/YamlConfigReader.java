package com.demo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlConfigReader<ConfigType> {

    private final Class<ConfigType> type;

    public static <T> YamlConfigReader<T> forType(Class<T> type) {
        return new YamlConfigReader<T>(type);
    }

    public ConfigType readYaml(String yamlFilePath) throws Exception {
        Path path = Paths.get(yamlFilePath);
        BufferedReader reader = Files.newBufferedReader(path);
        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(data, type);
    }

}
