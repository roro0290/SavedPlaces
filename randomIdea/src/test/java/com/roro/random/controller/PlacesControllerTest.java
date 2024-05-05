package com.roro.random.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roro.random.db.CustomPlaceRepository;
import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.model.PlacesResponse;
import com.roro.random.service.OutboundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.roro.random.constants.ExceptionMessages.NO_CANDIDATES_FOUND;
import static com.roro.random.constants.ExceptionMessages.NO_CANDIDATES_IN_DB;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlacesController.class)
public class PlacesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OutboundService outboundService;

    @MockBean
    CustomPlaceRepository customPlaceRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void addLocation_success_test() throws Exception {
        String result = "hoppers";
        when(outboundService.sendRequest("hopper")).thenReturn(result);
        mockMvc.perform(post("/add/location").queryParam("name", "hopper"))
                .andExpect(content().string(result))
                .andExpect(status().is(200));
    }

    @Test
    public void addLocation_fail_missingQueryParam_test() throws Exception {
        mockMvc.perform(post("/add/location"))
                .andExpect(status().is(400));
    }

    @Test
    public void addLocation_fail_NoCandidatesException_test() throws Exception {
        when(outboundService.sendRequest("hopper")).thenThrow(new NoCandidatesException(NO_CANDIDATES_FOUND));
        mockMvc.perform(post("/add/location").queryParam("name", "hopper"))
                .andExpect(status().is(HTTP_NO_CONTENT))
                .andExpect(content().string(NO_CANDIDATES_FOUND));
    }

    @Test
    public void getRandomPlace_success_test() throws Exception {
        PlacesResponse.Candidate c = new PlacesResponse.Candidate();
        c.setPlaceName("hoppers");
        when(customPlaceRepository.getRandomPlace()).thenReturn(c);
        mockMvc.perform(get("/get/random"))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(c)))
                .andExpect(status().is(200));
    }

    @Test
    public void getRandomPlace_fail_noCandidates_test() throws Exception {
        when(customPlaceRepository.getRandomPlace()).thenThrow(new NoCandidatesException(NO_CANDIDATES_IN_DB));
        mockMvc.perform(get("/get/random"))
                .andExpect(status().is(HTTP_NO_CONTENT))
                .andExpect(content().string(NO_CANDIDATES_IN_DB));
    }

}
