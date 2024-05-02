package com.roro.random.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.roro.random.exceptions.FailedStatusException;
import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.service.CustomPlaceRepository;
import com.roro.random.service.OutboundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PlacesController.class)
public class PlacesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OutboundService outboundService;

    @Test
    public void addLocation_returnSuccess() throws Exception {
        String result = "hoppers";
        when(outboundService.sendRequest("hopper")).thenReturn(result);
        mockMvc.perform(post("/add/location").queryParam("name", "hopper"))
                .andExpect(content().string(result));
    }
}
