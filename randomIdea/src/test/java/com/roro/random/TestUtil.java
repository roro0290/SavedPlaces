package com.roro.random;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtil {

    public static String readJsonFromFile(String path) throws IOException {
//        File file = new File(path);
        return Files.readString(Path.of(path));
    }
}
