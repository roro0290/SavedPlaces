package com.roro.random.model.googleResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roro.random.model.ResponseStatus;
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

        @JsonProperty("formatted_address")
        private String formattedAddress;

        @Id
        @JsonProperty("name")
        private String placeName;

        @JsonProperty("opening_hours")
        private OpeningHours openingHours;

        private String rating;

        @JsonProperty("place_id")
        private String placeId;

    }

    @Data
    public static class OpeningHours {

        @JsonProperty("open_now")
        private boolean openNow;
    }

}
