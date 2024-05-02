package com.roro.random.dao;

import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.PlacesResponse;

public interface CustomPlaceRepository {
    PlacesResponse.Candidate getRandomPlace() throws NoCandidatesException;

}
