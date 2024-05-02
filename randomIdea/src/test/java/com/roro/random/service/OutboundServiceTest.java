package com.roro.random.service;

import com.roro.random.TestUtil;
import com.roro.random.dao.PlacesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.roro.random.constants.GoogleUrlConstants.FIND_PLACE_FROM_TEXT_URL;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OutboundServiceTest {

    @Autowired
    OutboundService outboundService;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    PlacesRepository placesRepository;

    @Value("${google.api.key}")
    private String apiKey;

    @Test
    public void sendRequest_success_test() throws IOException {
        String location = "hoppers";
        when(restTemplate.getForObject(FIND_PLACE_FROM_TEXT_URL, String.class, location, apiKey))
                .thenReturn(TestUtil.readJsonFromFile("src/test/resources/res1.txt"));
    }

}
