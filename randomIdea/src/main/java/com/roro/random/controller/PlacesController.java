package com.roro.random.controller;

import ch.qos.logback.core.status.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.roro.random.exceptions.FailedStatusException;
import com.roro.random.exceptions.NoCandidatesException;
import com.roro.random.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;

@RestController
public class PlacesController {

    @Autowired
    OutboundService outboundService;

    /*
    1. To take in the name of the place
    2. Look up using Google
    3. If success -> add to list
    4. else -> return "place not found"
     */
    @PostMapping("/add/location")
    public ResponseEntity<?> addLocation(@RequestParam String name) {

        String result;
        try {
            result = outboundService.sendRequest(name);
        } catch (NoCandidatesException e) {
            return ResponseEntity.status(HTTP_NO_CONTENT).body(e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (FailedStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/give/me/random")
    public ResponseEntity<?> randomPlace() {

        return ResponseEntity.ok("");
    }

}
