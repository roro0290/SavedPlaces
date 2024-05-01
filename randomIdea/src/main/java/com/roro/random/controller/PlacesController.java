package com.roro.random.controller;

import com.roro.random.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        String result = outboundService.sendRequest(name);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/give/me/random")
    public ResponseEntity<?> randomPlace() {

        return ResponseEntity.ok("");
    }

}
