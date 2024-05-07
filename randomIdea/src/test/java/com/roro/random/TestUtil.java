package com.roro.random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roro.random.model.googleResponse.PlacesResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class TestUtil {

    public static String readJsonFromFile(String path) throws IOException {
//        File file = new File(path);
        return Files.readString(Path.of(path));
    }

    public static PlacesResponse.Candidate candidate_McDonald(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return Arrays.stream(objectMapper.readValue(response, PlacesResponse.class).getCandidates()).findFirst().get();
    }
}
