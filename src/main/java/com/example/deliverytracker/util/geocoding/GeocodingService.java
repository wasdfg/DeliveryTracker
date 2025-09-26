package com.example.deliverytracker.util.geocoding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class GeocodingService {

    private final RestTemplate restTemplate;
    private final String kakaoApiKey;
    private final String kakaoApiUrl = "https://dapi.kakao.com/v2/local/search/address.json";

    public GeocodingService(@Value("${kakao.api.key}") String kakaoApiKey) {
        this.restTemplate = new RestTemplate();
        this.kakaoApiKey = kakaoApiKey;
    }

    public Coordinates getCoordinates(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("주소는 비어 있을 수 없습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + kakaoApiKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", address);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            KakaoAddressResponse response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    KakaoAddressResponse.class
            ).getBody();

            if (response != null && !response.getDocuments().isEmpty()) {
                KakaoAddressResponse.Document firstDocument = response.getDocuments().get(0);
                log.info("지오코딩 성공: {} -> [{}, {}]", address, firstDocument.getLatitude(), firstDocument.getLongitude());
                return new Coordinates(firstDocument.getLatitude(), firstDocument.getLongitude());
            }
        } catch (Exception e) {
            log.error("지오코딩 API 호출 중 에러 발생: {}", e.getMessage());
        }

        // 검색 결과가 없거나 에러 발생 시 예외 처리
        throw new RuntimeException("주소에 해당하는 좌표를 찾을 수 없습니다: " + address);
    }
}
