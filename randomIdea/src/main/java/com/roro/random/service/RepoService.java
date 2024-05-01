package com.roro.random.service;

import com.roro.random.model.PlacesResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface RepoService extends MongoRepository<PlacesResponse.Candidate, String> {
}
