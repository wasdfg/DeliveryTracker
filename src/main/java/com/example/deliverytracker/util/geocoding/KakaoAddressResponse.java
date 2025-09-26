package com.example.deliverytracker.util.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAddressResponse {
    @JsonProperty("documents")
    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {
        @JsonProperty("y") // 카카오 API에서는 위도가 'y'
        private Double latitude;

        @JsonProperty("x") // 카카오 API에서는 경도가 'x'
        private Double longitude;
    }
}