package com.roro.random.dao;

import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.PlacesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.roro.random.constants.ExceptionMessages.NO_CANDIDATES_IN_DB;

@Service
public class CustomPlaceRepositoryImpl implements CustomPlaceRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Value("${collection.name}")
    String collectionName;

    @Override
    public PlacesResponse.Candidate getRandomPlace() throws NoCandidatesException {
        SampleOperation so = Aggregation.sample(1);
        Aggregation agg = Aggregation.newAggregation(so);

        List<PlacesResponse.Candidate> result = mongoTemplate.aggregate(agg, collectionName, PlacesResponse.Candidate.class)
                .getMappedResults();


        if (result.isEmpty()) {
            throw new NoCandidatesException(NO_CANDIDATES_IN_DB);
        }

        // Ensure there's at least one result
        return result.get(0);
    }
}
