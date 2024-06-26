package com.roro.random.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roro.random.db.PlacesRepository;
import com.roro.random.exceptions.FailedStatusException;
import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.GoogleApiInfoMap;
import com.roro.random.model.googleResponse.PlacesResponse;
import com.roro.random.model.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static com.roro.random.constants.ExceptionMessages.NO_CANDIDATES_FOUND;
import static com.roro.random.constants.GoogleUrlConstants.FIND_PLACE_FROM_TEXT_ENDPOINT;

@Service
public class OutboundServiceImpl implements OutboundService {

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    GoogleApiInfoMap googleApiInfoMap;

    /*
    CHECK getForObject method vs exchange method
    check response class structure and create something for it?
    %2C in the URL is for encoding
     */
    public String sendRequest(String location) throws NoCandidatesException, JsonProcessingException, FailedStatusException {
        return processResponse(restTemplate.getForObject(googleApiInfoMap.getGoogleUrl(FIND_PLACE_FROM_TEXT_ENDPOINT), String.class, location, apiKey));
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
                    .orElseThrow(() -> new NoCandidatesException(NO_CANDIDATES_FOUND));
        } else {
            throw new FailedStatusException("request failed: " + status);
        }

        savePlaceToDb(candidate);
        return candidate.getPlaceName();
    }

    /*
    save the candidate object to MongoDB
     */
    void savePlaceToDb(PlacesResponse.Candidate candidate) {
        placesRepository.save(candidate);
    }

}
