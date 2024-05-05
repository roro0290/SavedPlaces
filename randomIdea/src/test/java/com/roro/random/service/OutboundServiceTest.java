package com.roro.random.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roro.random.TestUtil;
import com.roro.random.db.PlacesRepository;
import com.roro.random.exceptions.FailedStatusException;
import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.PlacesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Arrays;

import static com.roro.random.constants.GoogleUrlConstants.FIND_PLACE_FROM_TEXT_URL;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class OutboundServiceTest {

    @Autowired
    OutboundService outboundService;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    PlacesRepository placesRepository;

    @Value("${google.api.key}")
    private String apiKey;

    public static GenericContainer<?> mongoDbContainer
            = new GenericContainer("mongo:4.4.29")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDbContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDbContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.database", () -> "placesDb");
    }

    @BeforeAll
    public static void startContainer() {
        mongoDbContainer.start();
    }

    @Test
    public void sendRequest_success_test() throws IOException, NoCandidatesException, FailedStatusException {
        String location = "McDonald";
        String resp = TestUtil.readJsonFromFile("src/test/resources/sampleSuccessRes.txt");
        when(restTemplate.getForObject(FIND_PLACE_FROM_TEXT_URL, String.class, location, apiKey))
                .thenReturn(resp);

        OutboundServiceImpl impl = mock(OutboundServiceImpl.class);
        doNothing().when(impl).savePlaceToDb(candidate_McDonald(resp));
        Assertions.assertEquals("McDonald's", outboundService.sendRequest(location));
    }

    /*
    Failure because ResponseStatus is unsuccessful
     */
    @Test
    public void sendRequest_fail_requestFailStatus_test() throws IOException {
        String location = "McDonald";
        String resp = TestUtil.readJsonFromFile("src/test/resources/failedStatusRes.txt");
        when(restTemplate.getForObject(FIND_PLACE_FROM_TEXT_URL, String.class, location, apiKey))
                .thenReturn(resp);
        Assertions.assertThrows(FailedStatusException.class, () -> outboundService.sendRequest(location));
    }

    /*
    no candidates found in response
     */
    @Test
    public void sendRequest_fail_noCandidatesInRes_test() throws IOException {
        String location = "McDonald";
        String resp = TestUtil.readJsonFromFile("src/test/resources/noCandidatesRes.txt");
        when(restTemplate.getForObject(FIND_PLACE_FROM_TEXT_URL, String.class, location, apiKey))
                .thenReturn(resp);
        Assertions.assertThrows(NoCandidatesException.class, () -> outboundService.sendRequest(location));
    }

    PlacesResponse.Candidate candidate_McDonald(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return Arrays.stream(objectMapper.readValue(response, PlacesResponse.class).getCandidates()).findFirst().get();
    }

}
