package com.roro.random.service;

import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.PlacesResponse;

public interface RandomGenerator {

    PlacesResponse.Candidate getRandomPlaceFromDB() throws NoCandidatesException;

}
