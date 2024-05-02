package com.roro.random.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.roro.random.exceptions.FailedStatusException;
import com.roro.random.exceptions.NoCandidatesException;

public interface OutboundService {

    String sendRequest(String location) throws NoCandidatesException, JsonProcessingException, FailedStatusException;


}
