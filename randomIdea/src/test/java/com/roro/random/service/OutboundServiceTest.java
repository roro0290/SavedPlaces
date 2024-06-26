package com.roro.random.service;

import com.roro.random.ContainerBase;
import com.roro.random.TestUtil;
import com.roro.random.db.PlacesRepository;
import com.roro.random.exceptions.FailedStatusException;
import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.GoogleApiInfoMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static com.roro.random.constants.GoogleUrlConstants.FIND_PLACE_FROM_TEXT_ENDPOINT;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
public class OutboundServiceTest {

    @Autowired
    OutboundService outboundService;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    PlacesRepository placesRepository;

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    GoogleApiInfoMap googleApiInfoMap;

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        ContainerBase.mongoDbProperties(registry);
    }

    @BeforeAll
    public static void startContainer() {
        ContainerBase.startContainer();
    }

    @AfterAll
    public static void stopContainer() throws InterruptedException {
        ContainerBase.stopContainer();
    }

    @Test
    public void sendRequest_success_test() throws IOException, NoCandidatesException, FailedStatusException {
        String location = "McDonald";
        String resp = TestUtil.readJsonFromFile("src/test/resources/sampleSuccessRes.txt");
        when(restTemplate.getForObject(googleApiInfoMap.getGoogleUrl(FIND_PLACE_FROM_TEXT_ENDPOINT), String.class, location, apiKey))
                .thenReturn(resp);

        OutboundServiceImpl impl = mock(OutboundServiceImpl.class);
        doNothing().when(impl).savePlaceToDb(TestUtil.candidate_McDonald(resp));
        Assertions.assertEquals("McDonald's", outboundService.sendRequest(location));
    }

    /*
    Failure because ResponseStatus is unsuccessful
     */
    @Test
    public void sendRequest_fail_requestFailStatus_test() throws IOException {
        String location = "McDonald";
        String resp = TestUtil.readJsonFromFile("src/test/resources/failedStatusRes.txt");
        when(restTemplate.getForObject(googleApiInfoMap.getGoogleUrl(FIND_PLACE_FROM_TEXT_ENDPOINT), String.class, location, apiKey))
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
        when(restTemplate.getForObject(googleApiInfoMap.getGoogleUrl(FIND_PLACE_FROM_TEXT_ENDPOINT), String.class, location, apiKey))
                .thenReturn(resp);
        Assertions.assertThrows(NoCandidatesException.class, () -> outboundService.sendRequest(location));
    }

}
