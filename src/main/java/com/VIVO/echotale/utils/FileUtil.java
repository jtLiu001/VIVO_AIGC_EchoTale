package com.VIVO.echotale.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "src\\main\\java\\com\\VIVO\\echotale\\ApiTest\\messages.json";

    public static List<Map<String, String>> loadMessages() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return objectMapper.readValue(sb.toString(), new TypeReference<List<Map<String, String>>>() {});
    }

    public static void saveMessages(List<Map<String, String>> messages) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
        String jsonString = objectMapper.writeValueAsString(messages);
        writer.write(jsonString.replace("},{", "},\n{")); // 插入换行符
        writer.close();
    }
}