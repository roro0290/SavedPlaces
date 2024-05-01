package com.roro.random.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlacesResponse {

    private Candidate[] candidates;

    private String status;

    @Data
    public static class Candidate {

        @JsonProperty("formatted_address")
        private String formattedAddress;

        @JsonProperty("name")
        private String placeName;

        @JsonProperty("opening_hours")
        private OpeningHours openingHours;

        private String rating;

    }

    @Data
    public static class OpeningHours {

        @JsonProperty("open_now")
        private boolean openNow;
    }

}
