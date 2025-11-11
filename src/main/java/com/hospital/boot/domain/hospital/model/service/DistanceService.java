package com.hospital.boot.domain.hospital.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Slf4j
@Service
public class DistanceService {

    @Value("${osrm.server.url}")
    private String osrmUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * OSRM API를 사용하여 두 지점 간의 실제 거리와 소요시간을 계산
     *
     * @param originLat 출발지 위도
     * @param originLng 출발지 경도
     * @param destLat 목적지 위도
     * @param destLng 목적지 경도
     * @return int[] {거리(미터), 소요시간(초)} 또는 실패시 null
     */
    public int[] calculateDistance(double originLat, double originLng, double destLat, double destLng) {
        long startTime = System.currentTimeMillis();

        try {
            // OSRM URL 형식: {lng},{lat};{lng},{lat}
            String url = String.format("%s/%f,%f;%f,%f?overview=false",
                    osrmUrl, originLng, originLat, destLng, destLat);

            log.info("[{}] OSRM API 호출 시작: ({},{}) -> ({},{})",
                    Thread.currentThread().getName(), originLat, originLng, destLat, destLng);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            long apiTime = System.currentTimeMillis() - startTime;

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String code = root.path("code").asText();

                if ("Ok".equals(code)) {
                    JsonNode routes = root.path("routes");

                    if (routes.isArray() && routes.size() > 0) {
                        JsonNode route = routes.get(0);
                        double distanceMeters = route.path("distance").asDouble();  // 미터
                        double durationSeconds = route.path("duration").asDouble();  // 초

                        int distance = (int) Math.round(distanceMeters);
                        int duration = (int) Math.round(durationSeconds);

                        log.debug("OSRM API 성공: 거리={}m, 소요시간={}초, API 응답시간={}ms",
                                distance, duration, apiTime);

                        return new int[]{distance, duration};
                    }
                }

                log.warn("OSRM API 응답 코드: {} ({}ms)", code, apiTime);
                return null;
            }

            log.warn("OSRM API 응답이 올바르지 않습니다 ({}ms): {}", apiTime, response.getBody());
            return null;

        } catch (Exception e) {
            long apiTime = System.currentTimeMillis() - startTime;
            log.error("거리 계산 중 오류 발생 ({}ms): ({},{}) -> ({},{}), 오류: {}",
                    apiTime, originLat, originLng, destLat, destLng, e.getMessage());
            return null;
        }
    }

    /**
     * Haversine 공식을 사용한 직선거리 계산 (대략적인 거리, API 실패시 fallback)
     *
     * @param lat1 위도1
     * @param lng1 경도1
     * @param lat2 위도2
     * @param lng2 경도2
     * @return 거리(미터)
     */
    public int calculateStraightDistance(double lat1, double lng1, double lat2, double lng2) {
        final int EARTH_RADIUS = 6371; // km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c * 1000; // km -> m

        return (int) Math.round(distance);
    }
}
