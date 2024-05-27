package com.VIVO.echotale.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FILE_PATH = "src\\main\\java\\com\\VIVO\\echotale\\ApiTest\\messages.json";

    public static List<Map<String, String>> loadMessages() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return null;
        }
        return objectMapper.readValue(file, new TypeReference<List<Map<String, String>>>() {});
    }

    public static void saveMessages(List<Map<String, String>> messages) throws IOException {
        objectMapper.writeValue(new File(FILE_PATH), messages);
    }
}