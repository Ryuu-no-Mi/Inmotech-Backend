package com.ryuunomi.inmotech.services.geocoding;

import com.ryuunomi.inmotech.config.MapaGeograficoEstatico;
import com.ryuunomi.inmotech.config.MapaGeograficoEstatico.ComunidadAutonoma;
import com.ryuunomi.inmotech.config.MapaGeograficoEstatico.Municipio;
import com.ryuunomi.inmotech.config.MapaGeograficoEstatico.Provincia;
import com.ryuunomi.inmotech.dto.CityGeoData;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GeocodingService {

    private final Map<String, CityGeoData> cache = new LinkedHashMap<>();
    private final Random random = new Random(42);

    public CityGeoData getGeoData(String ciudad) {
        String ciudadNormalizada = normalizarCiudad(ciudad);
        if (cache.containsKey(ciudadNormalizada)) {
            return cache.get(ciudadNormalizada);
        }
        CityGeoData data = buscarEnMapa(ciudadNormalizada);
        cache.put(ciudadNormalizada, data);
        return data;
    }

    public void precargarCache(List<String> ciudades) {
        System.out.println("=== GEOCODING: Precargando " + ciudades.size() + " ciudades desde mapa estático ===");
        long start = System.currentTimeMillis();
        for (String ciudad : ciudades) {
            getGeoData(ciudad);
        }
        System.out.printf("=== GEOCODING: Cache precargado en %dms ===%n", System.currentTimeMillis() - start);
    }

    public String getDistritoAleatorio(String ciudad) {
        CityGeoData data = getGeoData(ciudad);
        List<String> distritos = data.distritos();
        if (distritos.isEmpty()) return null;
        return distritos.get(random.nextInt(distritos.size()));
    }

    public String getBarrioAleatorio(String ciudad, String distrito) {
        CityGeoData data = getGeoData(ciudad);
        if (distrito == null) {
            List<List<String>> allBarrios = new ArrayList<>(data.barriosPorDistrito().values());
            if (allBarrios.isEmpty()) return null;
            List<String> barrioList = allBarrios.get(random.nextInt(allBarrios.size()));
            if (barrioList.isEmpty()) return null;
            return barrioList.get(random.nextInt(barrioList.size()));
        }
        List<String> barrios = data.barriosPorDistrito().get(distrito);
        if (barrios == null || barrios.isEmpty()) {
            List<List<String>> allBarrios = new ArrayList<>(data.barriosPorDistrito().values());
            if (allBarrios.isEmpty()) return null;
            List<String> barrioList = allBarrios.get(random.nextInt(allBarrios.size()));
            if (barrioList.isEmpty()) return null;
            return barrioList.get(random.nextInt(barrioList.size()));
        }
        return barrios.get(random.nextInt(barrios.size()));
    }

    public double getLatitud(String ciudad) {
        return getGeoData(ciudad).latitud();
    }

    public double getLongitud(String ciudad) {
        return getGeoData(ciudad).longitud();
    }

    public String getComunidad(String ciudad) {
        return MapaGeograficoEstatico.getComunidad(normalizarCiudad(ciudad));
    }

    public String getProvincia(String ciudad) {
        return MapaGeograficoEstatico.getProvincia(normalizarCiudad(ciudad));
    }

    private String normalizarCiudad(String ciudad) {
        if (ciudad == null) return "";
        String normalizado = ciudad.trim();
        normalizado = normalizado.replace("á", "a").replace("é", "e").replace("í", "i")
                                 .replace("ó", "o").replace("ú", "u").replace("ü", "u")
                                 .replace("ñ", "n");
        normalizado = normalizado.replace("À", "A").replace("É", "E").replace("Í", "I")
                                 .replace("Ó", "O").replace("Ú", "U").replace("Ü", "U")
                                 .replace("Ñ", "N");
        return normalizado;
    }

    private CityGeoData buscarEnMapa(String ciudad) {
        String ciudadNorm = normalizarCiudad(ciudad);
        for (ComunidadAutonoma comunidad : MapaGeograficoEstatico.MAPA_ESPANA.values()) {
            for (Provincia provincia : comunidad.provincias().values()) {
                for (Map.Entry<String, Municipio> entry : provincia.municipios().entrySet()) {
                    String nombreNorm = normalizarCiudad(entry.getKey());
                    if (nombreNorm.equalsIgnoreCase(ciudadNorm) ||
                        nombreNorm.contains(ciudadNorm) ||
                        ciudadNorm.contains(nombreNorm)) {
                        Municipio muni = entry.getValue();
                        List<String> distritos = new ArrayList<>(muni.distritosBarrios().keySet());
                        return new CityGeoData(
                            muni.nombre(),
                            muni.latitud(),
                            muni.longitud(),
                            distritos,
                            muni.distritosBarrios()
                        );
                    }
                }
            }
        }
        return crearDataDefault(ciudad);
    }

    private CityGeoData crearDataDefault(String ciudad) {
        double[] coords = generarCoordenadasDefault(ciudad);
        return new CityGeoData(
            ciudad,
            coords[0],
            coords[1],
            List.of("Centro"),
            Map.of("Centro", List.of("Centro"))
        );
    }

    private double[] generarCoordenadasDefault(String ciudad) {
        String ciudadNorm = normalizarCiudad(ciudad).toLowerCase();
        if (ciudadNorm.contains("madrid")) return new double[]{40.4168, -3.7038};
        if (ciudadNorm.contains("barcelona")) return new double[]{41.3851, 2.1734};
        if (ciudadNorm.contains("valencia")) return new double[]{39.4699, -0.3763};
        if (ciudadNorm.contains("sevilla")) return new double[]{37.3891, -5.9845};
        if (ciudadNorm.contains("malaga") || ciudadNorm.contains("málaga")) return new double[]{36.7213, -4.4214};
        if (ciudadNorm.contains("bilbao")) return new double[]{43.2630, -2.9350};
        if (ciudadNorm.contains("granada")) return new double[]{37.1773, -3.5986};
        if (ciudadNorm.contains("cordoba") || ciudadNorm.contains("córdoba")) return new double[]{37.8882, -4.7794};
        if (ciudadNorm.contains("alicante")) return new double[]{38.3452, -0.4815};
        if (ciudadNorm.contains("murcia")) return new double[]{37.9870, -1.1304};
        if (ciudadNorm.contains("cadiz") || ciudadNorm.contains("cádiz")) return new double[]{36.5271, -6.1897};
        if (ciudadNorm.contains("las palma")) return new double[]{28.1235, -15.4363};
        if (ciudadNorm.contains("tenerife") || ciudadNorm.contains("santa cruz")) return new double[]{28.4636, -16.2518};
        if (ciudadNorm.contains("coruña") || ciudadNorm.contains("a coruña")) return new double[]{43.3623, -8.4115};
        if (ciudadNorm.contains("vigo")) return new double[]{42.2401, -8.7205};
        if (ciudadNorm.contains("santiago")) return new double[]{42.8782, -8.5448};
        if (ciudadNorm.contains("vitoria") || ciudadNorm.contains("gasтеiz")) return new double[]{42.8599, -2.6828};
        if (ciudadNorm.contains("san sebastian") || ciudadNorm.contains("donostia")) return new double[]{43.3183, -1.9812};
        if (ciudadNorm.contains("zaragoza")) return new double[]{41.6488, -0.8891};
        if (ciudadNorm.contains("huesca")) return new double[]{42.1316, -0.4077};
        if (ciudadNorm.contains("teruel")) return new double[]{40.3456, -1.1065};
        if (ciudadNorm.contains("lleida")) return new double[]{41.6176, 0.6200};
        if (ciudadNorm.contains("girona")) return new double[]{41.9794, 2.8212};
        if (ciudadNorm.contains("tarragona")) return new double[]{41.1189, 1.2451};
        if (ciudadNorm.contains("palma")) return new double[]{39.5696, 2.6502};
        if (ciudadNorm.contains("valladolid")) return new double[]{41.6523, -4.7245};
        if (ciudadNorm.contains("burgos")) return new double[]{42.3500, -3.6800};
        if (ciudadNorm.contains("leon")) return new double[]{42.5987, -5.5671};
        if (ciudadNorm.contains("ponferrada")) return new double[]{42.5466, -6.5985};
        if (ciudadNorm.contains("palencia")) return new double[]{42.0097, -4.7388};
        if (ciudadNorm.contains("segovia")) return new double[]{40.9429, -4.1088};
        if (ciudadNorm.contains("soria")) return new double[]{41.7640, -2.4649};
        if (ciudadNorm.contains("avila")) return new double[]{40.6566, -4.7000};
        if (ciudadNorm.contains("salamanca")) return new double[]{40.9650, -5.6639};
        if (ciudadNorm.contains("zamora")) return new double[]{41.6561, -5.7447};
        if (ciudadNorm.contains("toledo")) return new double[]{39.8628, -4.0273};
        if (ciudadNorm.contains("cuenca")) return new double[]{40.0706, -2.1377};
        if (ciudadNorm.contains("guadalajara")) return new double[]{40.6328, -3.1629};
        if (ciudadNorm.contains(" Albacete")) return new double[]{38.9973, -1.9267};
        if (ciudadNorm.contains("ciudad real")) return new double[]{38.9860, -3.9291};
        if (ciudadNorm.contains("badajoz")) return new double[]{38.8786, -6.9703};
        if (ciudadNorm.contains("caceres") || ciudadNorm.contains("cáceres")) return new double[]{39.4753, -6.3724};
        if (ciudadNorm.contains("huelva")) return new double[]{37.2204, -6.9498};
        if (ciudadNorm.contains("jaen") || ciudadNorm.contains("jaén")) return new double[]{37.7796, -3.7885};
        if (ciudadNorm.contains("almeria") || ciudadNorm.contains("almería")) return new double[]{36.8340, -2.4637};
        if (ciudadNorm.contains("logroño")) return new double[]{42.4651, -2.4456};
        return new double[]{40.4168, -3.7038};
    }
}
