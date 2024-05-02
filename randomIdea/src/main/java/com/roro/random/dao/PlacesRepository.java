package com.roro.random.dao;

import com.roro.random.model.PlacesResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface PlacesRepository extends MongoRepository<PlacesResponse.Candidate, String> {

}
