package com.hospital.boot.domain.hospital.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class DistanceService {

    @Value("${osrm.server.url}")
    private String osrmUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public int[] calculateDistance(double originLat, double originLng, double destLat, double destLng) {
        try {
            String url = String.format("%s/%f,%f;%f,%f?overview=false",
                    osrmUrl, originLng, originLat, destLng, destLat);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String code = root.path("code").asText();

                if ("Ok".equals(code)) {
                    JsonNode routes = root.path("routes");

                    if (routes.isArray() && routes.size() > 0) {
                        JsonNode route = routes.get(0);
                        double distanceMeters = route.path("distance").asDouble();
                        double durationSeconds = route.path("duration").asDouble();

                        int distance = (int) Math.round(distanceMeters);
                        int duration = (int) Math.round(durationSeconds);

                        return new int[]{distance, duration};
                    }
                }
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

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
