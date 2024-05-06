package com.roro.random.service;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.roro.random.ContainerBase;
import com.roro.random.TestUtil;
import com.roro.random.service.OutboundServiceImpl;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.roro.random.constants.GoogleUrlConstants.FIND_PLACE_FROM_TEXT_URL;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class ControllerWithWireMockTest {

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
    MockMvc mockMvc;

    // start the wire mock server using @RegisterExtension on a dynamic port (not fixed port)
    // when you provide these test cases to a pipeline, and there are multiple pipelines, its possible for there to be a port confict, test fail
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    // the production value for this property is replaced with the mock server url
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("google.base.url", wireMockServer::baseUrl);
    }


    @Test
    public void addLocation_success_test() throws Exception {

        String response = TestUtil.readJsonFromFile("src/test/resources/sampleSuccessRes.txt");

        // WE ARE SENDING A GET REQUEST TO GOOGLE
        // use .* for regex
        wireMockServer.stubFor(WireMock
                .get(WireMock.urlMatching("/maps/.*"))
                .willReturn(
                        aResponse().withBody(response)
                )
        );

        mockMvc.perform(post("/add/location").queryParam("name", "McDonald's"))
                .andExpect(content().string("McDonald's"))
                .andExpect(status().is(200));
    }

}
