package com.roro.random.db;

import com.roro.random.model.googleResponse.PlacesResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface PlacesRepository extends MongoRepository<PlacesResponse.Candidate, String> {

}
