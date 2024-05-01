package com.roro.random.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roro.random.exceptions.FailedStatusException;
import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.PlacesResponse;
import com.roro.random.model.ResponseStatus;
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

    @Autowired
    RepoService repoService;

    /*
    CHECK getForObject method vs exchange method
    check response class structure and create something for it?
    %2C in the URL is for encoding
     */
    public String sendRequest(String location) throws NoCandidatesException, JsonProcessingException, FailedStatusException {
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?fields=formatted_address,name,rating,opening_hours&input={location}&inputtype=textquery&key={apiKey}";
        return processResponse(restTemplate.getForObject(url, String.class, location, apiKey));
    }


    /*
    Takes in the jsonResponse and extracts the fields
     */
    String processResponse(String jsonResponse) throws JsonProcessingException, NoCandidatesException, FailedStatusException {
        PlacesResponse response = null;
        PlacesResponse.Candidate candidate = null;

        response = objectMapper.readValue(jsonResponse, PlacesResponse.class);
        ResponseStatus status = response.getStatus();

        if (status.equals(ResponseStatus.OK)) {
            candidate = Arrays.stream(response.getCandidates())
                    .findFirst()
                    .orElseThrow(() -> new NoCandidatesException("no candidates found"));
        } else {
            throw new FailedStatusException("request failed: " + status);
        }

        saveToDb(candidate);
        return candidate.getPlaceName();
    }

    void saveToDb(PlacesResponse.Candidate candidate) {
        repoService.save(candidate);
    }

}
