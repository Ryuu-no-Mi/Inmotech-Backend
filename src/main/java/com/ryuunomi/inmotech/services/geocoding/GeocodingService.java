package com.ryuunomi.inmotech.services.geocoding;

import com.ryuunomi.inmotech.dto.CityGeoData;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeocodingService {

    private final NominatimService nominatimService;
    private final Map<String, CityGeoData> cache = new ConcurrentHashMap<>();

    private static final Map<String, List<String>> CIUDADES_FALLBACK = Map.of(
        "Madrid", List.of("Centro", "Salamanca", "Chamberí", "Retiro", "Tetuán", "Moncloa-Aravaca", "Arganzuela", "Latina", "Carabanchel", "Fuencarral-El Pardo"),
        "Barcelona", List.of("Eixample", "Gràcia", "Sants-Montjuïc", "Horta-Guinardó", "Nou Barris", "Sant Andreu", "Sant Martí", "Les Corts", "Sarrià-Sant Gervasi", "Ciutat Vella"),
        "Valencia", List.of("Ciutat Vella", "Eixample", "Extramurs", "El Grau", "Benimaclet", "Rascanya", "Patraix", "Jesús", "La Saïdia", "Poblats Marítims"),
        "Málaga", List.of("Centro", "La Malagueta", "El Perchel", "Bailén-Miraflores", " Carretera de Cádiz", "Teatinos-Universidad", "Limonar", "El Palo", "Ciudad Jardín", "Málaga Este"),
        "Sevilla", List.of("Nervión", "Alameda de Hércules", "Macarena", "Triana", "Cerro-Amate", "Nervión-Santa Justa", "San Pablo-Santa Justa", "Los Remedios", "Casco Antiguo", "Sur"),
        "Bilbao", List.of("Abando", "Indautxu", "Deusto", "San Ignacio", "Begoña", "Otxarkoaga", "Txurdinaga", "Basurto", "Errekalde", "Uribe"),
        "Zaragoza", List.of("Casco Histórico", "El Gancho", "Delicias", "Las Fuentes", "San José", "Almozara", "La Madalena", "Torroso", "Miralbueno", "Montecarlo"),
        "Murcia", List.of("Centro", "El Carmen", "Vista Alegre", "La Flota", "San Andrés", "Santa Cruz", "San Juan", "La Paz", "Norte", "Este"),
        "Palma de Mallorca", List.of("Centre", "El Terreno", "Santa Catalina", "El Molinar", "Coll d'en Rabassa", "Son Gotleu", "Son Espanyol", "Son Culitat", "La Soledat", "Playa de Palma"),
        "Las Palmas de Gran Canaria", List.of("Vegueta", "Triana", "Alameda de Colón", "Ciudad Alta", "Schamann", "Tamaraceite", "La Isleta", "Guanarteme", "Puerto", "San Cristóbal")
    );

    private static final Map<String, Map<String, List<String>>> BARRIOS_FALLBACK = Map.of(
        "Madrid", Map.of(
            "Centro", List.of("Sol", "Embajadores", "Cortes", "Justicia", "Universidad", "Palacio"),
            "Salamanca", List.of("Salamanca", "Recoletos", "Goya", "Lista", "Fuente del Berro", "Guindalera"),
            "Chamberí", List.of("Chamberí", "Tufé", "Gaztambide", "Arapiles", "Almagro", "Río de Janeiro")
        ),
        "Barcelona", Map.of(
            "Eixample", List.of("Eixample Esquerra", "Eixample Dret", "Fort Pienc", "Sagrada Familia"),
            "Gràcia", List.of("Gràcia", "Vila de Gràcia", "Camp d'en Grassot", "Salut"),
            "Sants-Montjuïc", List.of("Sants", "Hostafrancs", "Poble Sec", "Montjuïc")
        ),
        "Málaga", Map.of(
            "Centro", List.of("Casco Antiguo", "La Merchant", "Catedral"),
            "La Malagueta", List.of("La Malagueta", "Pedragalejo", "Limonar"),
            "El Perchel", List.of("El Perchel", "Muelle Heredia", "Hermandad")
        ),
        "Sevilla", Map.of(
            "Nervión", List.of("Nervión", "San Pablo", "Santa Justa"),
            "Alameda de Hércules", List.of("Alameda", "Corea", "San Luis"),
            "Triana", List.of("Triana", "Los Remedios", "Barriada del Carmen")
        )
    );

    public GeocodingService(NominatimService nominatimService) {
        this.nominatimService = nominatimService;
    }

    public CityGeoData getGeoData(String ciudad) {
        return cache.computeIfAbsent(ciudad, c -> {
            Optional<com.ryuunomi.inmotech.dto.CityGeoData> data = nominatimService.getCityGeoData(c);
            return data.orElseGet(() -> createFallback(c));
        });
    }

    public void precargarCache(List<String> ciudades) {
        System.out.println("=== GEOCODING: Precargando " + ciudades.size() + " ciudades ===");
        long start = System.currentTimeMillis();
        int count = 0;
        for (String ciudad : ciudades) {
            getGeoData(ciudad);
            count++;
            if (count % 10 == 0) {
                System.out.printf("  %d/%d ciudades geocodificadas (%dms)%n", count, ciudades.size(), System.currentTimeMillis() - start);
            }
        }
        System.out.printf("=== GEOCODING: Cache precargado en %dms ===%n", System.currentTimeMillis() - start);
    }

    public String getDistritoAleatorio(String ciudad) {
        CityGeoData data = getGeoData(ciudad);
        List<String> distritos = data.distritos();
        if (distritos.isEmpty()) return null;
        return distritos.get(new Random().nextInt(distritos.size()));
    }

    public String getBarrioAleatorio(String ciudad, String distrito) {
        CityGeoData data = getGeoData(ciudad);
        if (distrito == null) {
            List<List<String>> allBarrios = new ArrayList<>(data.barriosPorDistrito().values());
            if (allBarrios.isEmpty()) return null;
            List<String> barrioList = allBarrios.get(new Random().nextInt(allBarrios.size()));
            if (barrioList.isEmpty()) return null;
            return barrioList.get(new Random().nextInt(barrioList.size()));
        }
        List<String> barrios = data.barriosPorDistrito().get(distrito);
        if (barrios == null || barrios.isEmpty()) {
            List<List<String>> allBarrios = new ArrayList<>(data.barriosPorDistrito().values());
            if (allBarrios.isEmpty()) return null;
            List<String> barrioList = allBarrios.get(new Random().nextInt(allBarrios.size()));
            if (barrioList.isEmpty()) return null;
            return barrioList.get(new Random().nextInt(barrioList.size()));
        }
        return barrios.get(new Random().nextInt(barrios.size()));
    }

    private CityGeoData createFallback(String ciudad) {
        List<String> distritos = CIUDADES_FALLBACK.getOrDefault(ciudad, List.of("Centro"));
        Map<String, List<String>> barriosPorDistrito = BARRIOS_FALLBACK.getOrDefault(ciudad, Map.of("Centro", List.of("Centro")));
        return new CityGeoData(ciudad, distritos, barriosPorDistrito);
    }
}