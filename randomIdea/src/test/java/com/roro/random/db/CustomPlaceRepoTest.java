package com.roro.random.db;

import com.roro.random.ContainerBase;
import com.roro.random.TestUtil;
import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.PlacesResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class CustomPlaceRepoTest {

    @BeforeAll
    public static void startContainer() {
        ContainerBase.startContainer();
    }

    @AfterAll
    public static void stopContainer() {
        ContainerBase.stopContainer();
    }

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        ContainerBase.mongoDbProperties(registry);
    }

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    CustomPlaceRepository customPlaceRepository;

    /*
    Execute tests in order where
    1. put place in DB
    2. retrieve place from DB
    */
    @Test
    @Order(2)
    public void getRandomPlace_success_test() throws IOException, NoCandidatesException {
        String resp = TestUtil.readJsonFromFile("src/test/resources/sampleSuccessRes.txt");
        placesRepository.save(TestUtil.candidate_McDonald(resp));
        PlacesResponse.Candidate c = customPlaceRepository.getRandomPlace();
        assertNotNull(c);
        assertEquals(c.getPlaceName(), "McDonald's");
    }

    @Test
    @Order(1)
    public void getRandomPlace_noEntryInDb_failure_test() {
        assertThrows(NoCandidatesException.class, () -> customPlaceRepository.getRandomPlace());
    }

}
