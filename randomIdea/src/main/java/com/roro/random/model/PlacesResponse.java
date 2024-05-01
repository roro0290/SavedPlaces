package com.roro.random.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
TO THINK ABOUT HOW TO MAKE THIS MORE DYNAMIC
- if new fields are added in query, this should be automatically updated
- currently, we have to add it here manually
 */
@Data
public class PlacesResponse {

    private Candidate[] candidates;

    @JsonProperty("error_message")
    private String errorMessage;

    private ResponseStatus status;

    @Data
    @Document
    public static class Candidate {

        @Id
        public String id;

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
