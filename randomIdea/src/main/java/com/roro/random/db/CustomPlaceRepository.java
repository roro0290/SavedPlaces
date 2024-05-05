package com.roro.random.db;

import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.PlacesResponse;

public interface CustomPlaceRepository {
    PlacesResponse.Candidate getRandomPlace() throws NoCandidatesException;

}
