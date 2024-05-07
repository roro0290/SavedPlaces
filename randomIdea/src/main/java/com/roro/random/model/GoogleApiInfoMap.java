package com.roro.random.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static com.roro.random.constants.GoogleUrlConstants.*;

@Data
@Configuration
@ConfigurationProperties
public class GoogleApiInfoMap {

    Map<String, SearchObject> googleApiInfoMap;

    public String getGoogleUrl(String endpoint){
        return googleApiInfoMap.get(endpoint).constructUrl();
    }

    @Data
    static class SearchObject {
        private String baseUrl;
        private String endpoint;
        private String output;
        private List<String> requiredParameters;
        private List<String> fields;

        String constructUrl() {
            StringBuilder sb = new StringBuilder();
            sb.append(baseUrl)
                    .append(endpoint)
                    .append(output)
                    .append(QUESTION_MARK);
            requiredParameters.forEach(s -> sb.append(s).append(AMPERSAND));
            if (fields.size() == 0) {
                sb.deleteCharAt(sb.length() - 1);
            } else {
                sb.append(OPTIONAL_FIELDS);
                fields.forEach(f -> sb.append(f).append(COMMA));
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }

    }


}
