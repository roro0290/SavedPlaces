package com.roro.random.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roro.random.model.PlacesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class OutboundService {

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    /*
    CHECK getForObject method vs exchange method
    check response class structure and create something for it?
    %2C in the URL is for encoding
     */
    public String sendRequest(String location) {
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?fields=formatted_address,name,rating,opening_hours&input={location}&inputtype=textquery&key={apiKey}";
        return processResponse(restTemplate.getForObject(url, String.class, location, apiKey));
    }


    String processResponse(String jsonResponse) {
        PlacesResponse response = null;
        try {
            response = objectMapper.readValue(jsonResponse, PlacesResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Arrays.stream(response.getCandidates())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("no candidates found"))
                .getPlaceName();
    }

}
