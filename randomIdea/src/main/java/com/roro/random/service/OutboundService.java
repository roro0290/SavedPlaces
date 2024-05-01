package com.roro.random.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OutboundService {

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate;

    /*
    CHECK getForObject method vs exchange method
    check response class structure and create something for it?
    %2C in the URL is for encoding
     */
    public String sendRequest(String location) {
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?fields=formatted_address,name,rating,opening_hours&input={location}&inputtype=textquery&key={apiKey}";
        return restTemplate.getForObject(url, String.class, location, apiKey);
    }

}
