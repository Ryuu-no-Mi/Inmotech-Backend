package com.ryuunomi.inmotech.services.geocoding;

import com.ryuunomi.inmotech.dto.CityGeoData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class NominatimService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private final RestTemplate restTemplate;

    public NominatimService() {
        this.restTemplate = new RestTemplate();
    }

    public Optional<CityGeoData> getCityGeoData(String ciudad) {
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String url = NOMINATIM_URL + "?q=" + ciudad + ",+Spain&format=json&limit=5&addressdetails=1";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "InmotechApp/1.0 (Java Spring Boot; contact@inmotech.com)");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> results = restTemplate.exchange(url, HttpMethod.GET, entity, List.class).getBody();

            if (results == null || results.isEmpty()) {
                return Optional.empty();
            }

            Set<String> distritosSet = new LinkedHashSet<>();
            Map<String, Set<String>> barriosPorDistrito = new LinkedHashMap<>();

            for (Map<String, Object> result : results) {
                Map<String, Object> address = (Map<String, Object>) result.get("address");

                String suburb = (String) address.get("suburb");
                String neighbourhood = (String) address.get("neighbourhood");
                String cityDistrict = (String) address.get("city_district");
                String district = (String) address.get("district");
                String borough = (String) address.get("borough");

                String distrito = cityDistrict != null ? cityDistrict : district;
                if (distrito != null && !distrito.isBlank()) {
                    distritosSet.add(distrito);
                    if (!barriosPorDistrito.containsKey(distrito)) {
                        barriosPorDistrito.put(distrito, new LinkedHashSet<>());
                    }
                    if (suburb != null && !suburb.isBlank()) {
                        barriosPorDistrito.get(distrito).add(suburb);
                    }
                    if (neighbourhood != null && !neighbourhood.isBlank()) {
                        barriosPorDistrito.get(distrito).add(neighbourhood);
                    }
                }

                if (borough != null && !borough.isBlank()) {
                    distritosSet.add(borough);
                    if (!barriosPorDistrito.containsKey(borough)) {
                        barriosPorDistrito.put(borough, new LinkedHashSet<>());
                    }
                }
            }

            if (distritosSet.isEmpty()) {
                return Optional.empty();
            }

            Map<String, List<String>> barriosMap = new LinkedHashMap<>();
            for (Map.Entry<String, Set<String>> entry : barriosPorDistrito.entrySet()) {
                barriosMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }

            return Optional.of(new CityGeoData(ciudad, new ArrayList<>(distritosSet), barriosMap));

        } catch (Exception e) {
            System.err.println("Nominatim error for " + ciudad + ": " + e.getMessage());
            return Optional.empty();
        }
    }
}