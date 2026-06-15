package com.ryuunomi.inmotech.services.geocoding;

import com.ryuunomi.inmotech.dto.CityGeoData;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GeocodingService {

    private final Map<String, CityGeoData> cache = new LinkedHashMap<>();

    private static final Map<String, Map<String, Map<String, List<String>>>> MAPA_GEOGRAFICO;

    static {
        MAPA_GEOGRAFICO = new LinkedHashMap<>();

        // ================================================
        // ANDALUCÍA
        // ================================================
        Map<String, Map<String, List<String>>> andalucia = new LinkedHashMap<>();

        // Almería
        Map<String, List<String>> almeria = new LinkedHashMap<>();
        almeria.put("Centro", List.of("Almedinilla", "San Sebastián", "Cathedral", "Barrio Nuevo", "El Puche"));
        almeria.put("Almedinilla", List.of("El Llano", "La Fuente", "San Roque", "Las Minas", "Los EJido"));
        almeria.put("Roquetas de Mar", List.of("Roquetas Pueblo", "La Granada", "Las Salinas", "El Puerto", "Los Aguaducos"));
        almeria.put("El Ejido", List.of("Centro", "Pampanico", "San Agustín", "Almerimar", "Las Norias"));
        almeria.put("Níjar", List.of("Níjar Pueblo", "San Miguel", "Los Molinos", "Cabo de Gata", "La Jana"));
        almeria.put("Vera", List.of("Vera Pueblo", "Vera Playa", "El Puerto", "La Gangosa", "Los Maños"));
        almeria.put("Adra", List.of("Adra Centro", "El Lance", "La Curva", "San Miguel", "La Alcazaba"));
        almeria.put("Cuevas del Almanzora", List.of("Cuevas Pueblo", "San Juan", "El Arteal", "Los Lobos", "El Gabar"));
        almeria.put("Huércal-Overa", List.of("Huércal Centro", "Santa María", "El Realengo", "Los Medinas", "La Hornera"));
        almeria.put("Albox", List.of("Albox Centro", "El Badul", "Los Baños", "Llano de Don Juan", "La Jauca"));
        andalucia.put("Almería", almeria);

        // Cádiz
        Map<String, List<String>> cadiz = new LinkedHashMap<>();
        cadiz.put("Jerez de la Frontera", List.of("Centro", "San Miguel", "Santiago", "San Dionisio", "La Ina", "El Portal", "San Juan de Dios", "El Altillo", "El Majuelo", "La Granja"));
        cadiz.put("Algeciras", List.of("Centro", "San García", "El Saladillo", "Getares", "El Rinconcillo", "La Juliana", "Los Portales", "Cotorrillo", "Campo de Gibraltar", "Las Huertas"));
        cadiz.put("Cádiz", List.of("Centro", "Santa María", "La Viña", "El Pópulo", "Catedral", "San Antonio", "Plaza de España", "Barriada ElBufador", "Zabal", "Cortadura"));
        cadiz.put("El Puerto de Santa María", List.of("Centro", "La Puntilla", "San Marcos", "Valdelagrana", "El Portal", "Los Frailes", "La Margarita", "El Carmen", "Pinillo", "Vistahermosa"));
        cadiz.put("Sanlúcar de Barrameda", List.of("Centro", "Bajo de Guía", "San Lázaro", "El Carmen", "La Jara", "Doña Blanco", "Las Piletas", "El Mancud", "Barriada de la Blanca", "Colinas"));
        cadiz.put("Chiclana de la Frontera", List.of("Centro", "San Antonio", "El Arenal", "La Janda", "El Sotillo", "Los Boliches", "San Juan de los Terreros", "El Carbonal", "La Magdalena", "La Manchuela"));
        cadiz.put("La Línea de la Concepción", List.of("Centro", "San Bernardo", "El Carmen", "La Atunara", "San Felipe", "Santa Margarita", "El Puerto", "Los Junquiles", "El Zabal", "La Granja"));
        cadiz.put("Rota", List.of("Centro", "El Puerto", "Mina", "La Jodar", "Los Remedios", "El Carmen", "San Bartolomé", "Los Hoyos", "La Villa", "La Pita"));
        cadiz.put("Arcos de la Frontera", List.of("Centro Histórico", "La Villa", "El Marquese", "San Miguel", "Los Molinos", "El Pilar", "El Carmen", "San Juan", "La Paz", "Los又不是"));
        cadiz.put("Puerto Real", List.of("Centro", "El Carmen", "La Music", "Los Remedios", "San José", "La Jarcia", "El Zaporito", "El Soldado", "Los Pinos", "La离了"));
        cadiz.put("Los Barrios", List.of("Centro", "Los Carros", "San Roque", "El Monatal", "La Jara", "Los Als", "El Campo", "San Andrés", "Los Patrón", "Las Huertas"));
        cadiz.put("Cádiz capital districts", List.of("Centro", "Santa María del Mar", "La Viña", "El Pópulo", "Cortadura", "Valcárcamo", "Zabal", "Barriada Lesseps", "El Mentidero", "San Antonio"));
        andalucia.put("Cádiz", cadiz);

        // Córdoba
        Map<String, List<String>> cordoba = new LinkedHashMap<>();
        cordoba.put("Córdoba", List.of("Centro", "San Miguel", "Capuchinos", "La Magdalena", "San Pedro", "El Brillante", "El Campo Madre de Dios", "La Fuensanta", "Aguas Claras", "El Patriarca"));
        cordoba.put("Lucena", List.of("Centro", "San Jorge", "El Cuesto", "La Mina", "San Francisco", "Los Remedios", "El Carmen", "La estrella", "Los Llanos", "Puente del Zújar"));
        cordoba.put("Puente Genil", List.of("Centro", "El Palero", "La Salceda", "San Miguel", "El Carmen", "Los Molinos", "La Jara", "El Pilar", "El Salvador", "San Roque"));
        cordoba.put("Montilla", List.of("Centro", "La Villa", "San Bartolomé", "El Carmen", "San Juan", "Los Remedios", "El Pilar", "Santa María", "San Francisco", "Los López"));
        cordoba.put("Cabra", List.of("Centro", "La Villa", "San Juan de la Cruz", "El Carmen", "Los Remedios", "El Pilar", "San José", "La Paz", "El Castillo", "Los又不是"));
        cordoba.put("Priego de Córdoba", List.of("Centro", "La Villa", "San Juan de la Puerta", "El Carmen", "Los Remedios", "La Fuente", "El Castillo", "San Roque", "La Solanilla", "Los Tís"));
        cordoba.put("Rute", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Jara", "El Torno", "Los Puentec", "La Granja"));
        cordoba.put("Baena", List.of("Centro", "La Villa", "San Francisco", "El Carmen", "Los Remedios", "La Fuente", "El Pilar", "San Roque", "Los Vizca", "La Alberquilla"));
        cordoba.put("Castro del Río", List.of("Centro", "La Villa", "San Miguel", "El Carmen", "Los Remedios", "La Fuente", "El Pilar", "Los又不是", "El Badul", "La Jara"));
        cordoba.put("Espejo", List.of("Centro", "La Villa", "San Miguel", "El Carmen", "Los Remedios", "El Llano", "La Fuente", "El Tío", "Los Pinos", "El LMati"));
        andalucia.put("Córdoba", cordoba);

        // Granada
        Map<String, List<String>> granada = new LinkedHashMap<>();
        granada.put("Granada", List.of("Centro", "Albaicín", "Realejo", "Sacromonte", "Alhambra", "Carmen de los Molinos", "San Pedro", "San Miguel", "El Serrallo", "La Cartuja"));
        granada.put("Motril", List.of("Centro", "El Varadero", "La Granada", "Torrecuéllar", "La Jara", "San Roque", "El Carmen", "Los Albores", "El Puerto", "La Guillerna"));
        granada.put("Almuñécar", List.of("Centro", "San Miguel", "El Cervillejo", "La Herradura", "Punta de la Mona", "El Secano", "Los Tarambanes", "El Lano", "La Rivera", "San Nicolás"));
        granada.put("Línea de la Concepción (Granada area)", List.of("Centro", "San Miguel", "La Gloria", "El Zorro", "Los Huertos", "El Carmen", "Los Pinos", "El Salado", "La Juncosa", "El Bukan"));
        granada.put("Loja", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Fuente", "El Torno", "Los Puentec", "La Granja"));
        granada.put("Alhama de Granada", List.of("Centro", "La Villa", "San Juan", "El Carmen", "Los Remedios", "El Llano", "La Jara", "El Tío", "Los Pinos", "La Granja"));
        granada.put("Santa Fe", List.of("Centro", "La Jara", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "San Roque", "Los Vizca"));
        granada.put("Illora", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "La Fuente", "El Pilar", "El Llano", "Los Pinos", "La Jara"));
        granada.put("Padul", List.of("Centro", "El Carmen", "La Fuente", "San Juan", "Los Remedios", "El Llano", "La Jara", "El Tío", "Los Pinos", "La Granja"));
        granada.put("Huétor Tájar", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los不是", "El Badul"));
        andalucia.put("Granada", granada);

        // Huelva
        Map<String, List<String>> huelva = new LinkedHashMap<>();
        huelva.put("Huelva", List.of("Centro", "El Carmen", "San Sebastián", "Laorde", "Los Dolores", "El Matadero", "La Placeta", "El Molino", "Higueral", "Los Pinos"));
        huelva.put("Lepe", List.of("Centro", "La Antilla", "El Terrón", "Los Limoneros", "El Choque", "La Redondela", "Isla Cristina", "El Puerto", "Los“不是", "El Salado"));
        huelva.put("Almonte", List.of("Centro", "Matalascañas", "ElRocio", "Los Cabezos", "La NASA", "El LIR", "Los“不是", "El Pastizal", "El Salado", "La Perea"));
        huelva.put("Ayamonte", List.of("Centro", "La Villa", "El Puerto", "La Antilla", "Isla Cristina", "El Terrón", "Los“不是", "El Salado", "La Redondela", "El LMati"));
        huelva.put("Moguer", List.of("Centro", "Palos de la Frontera", "Mazagón", "ElLIR", "Los“不是", "El Puerto", "La Fuente", "El Carmen", "Los Pinos", "El Salado"));
        huelva.put("Aljaraque", List.of("Centro", "El Torreón", "Los“不是", "El Salado", "La Fuente", "El Carmen", "Los Pinos", "El Puerto", "El Almendral", "La Jara"));
        huelva.put("Cartaya", List.of("Centro", "El Puerto", "La Antilla", "El Terrón", "Los“不是", "El Salado", "La Redondela", "El Carmen", "Los Pinos", "El Zorro"));
        huelva.put("San Juan del Puerto", List.of("Centro", "El Carmen", "La Fuente", "San Juan", "Los Remedios", "El Llano", "La Jara", "El Tío", "Los Pinos", "La Granja"));
        huelva.put("Gibraleón", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "San Roque", "Los Vizca"));
        huelva.put("Bollullos par del Condado", List.of("Centro", "El Carmen", "La Fuente", "San Juan", "Los Remedios", "El Llano", "La Jara", "El Tío", "Los Pinos", "La Granja"));
        andalucia.put("Huelva", huelva);

        // Jaén
        Map<String, List<String>> jaen = new LinkedHashMap<>();
        jaen.put("Jaén", List.of("Centro", "San Juan", "ElCarmen", "La Magdalena", "Los olivares", "El Almorran", "La Jara", "El Seda", "Los“不是", "El Zorro"));
        jaen.put("Linares", List.of("Centro", "El Carmen", "La Magdalena", "San Juan", "Los olivares", "El Almorran", "La Jara", "El Seda", "Los“不是", "El Tío"));
        jaen.put("Andújar", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Badul"));
        jaen.put("Úbeda", List.of("Centro", "La Villa", "El Salvador", "San Juan de la Cruz", "ElCarmen", "Los Remedios", "San Lorenzo", "El Llano", "La Jara", "El Tío"));
        jaen.put("Alcazar de San Juan", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Badul"));
        jaen.put("Alcalá la Real", List.of("Centro", "La Villa", "El Alcázar", "San Juan", "El Carmen", "Los Remedios", "La Fuente", "El Pilar", "El Llano", "Los Pinos"));
        jaen.put("Baeza", List.of("Centro", "La Villa", "El Pópulo", "San Juan de la Cruz", "El Carmen", "Los Remedios", "La Catedral", "El Llano", "La Jara", "Los不是"));
        jaen.put("Torredelcampo", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Badul"));
        jaen.put("Martos", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Tío"));
        jaen.put("Cabra del Santo Cristo", List.of("Centro", "La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano", "La Jara", "El Tío", "Los Pinos", "La Granja"));
        andalucia.put("Jaén", jaen);

        // Málaga
        Map<String, List<String>> malaga = new LinkedHashMap<>();
        malaga.put("Málaga", List.of("Centro", "LaMalagueta", "ElPerchel", "Bailén-Miraflores", "Carretera de Cádiz", "Teatinos-Universidad", "Limonar", "El Palo", "Ciudad Jardín", "Málaga Este", "ElPuerto", "LaCruz", "San Rafael", "ElEjido", "Los Nortes"));
        malaga.put("Marbella", List.of("Centro", "Marbella Pueblo", "San Pedro de Alcántara", "Nueva Andalucía", "Elviria", "Las Chapas", "Cabo Pino", "Artola", "Casablanca", "Río Real", "LosMontermoso", "LaMilla"));
        malaga.put("Estepona", List.of("Centro", "Paseo Marítimo", "ElPadron", "LaJohana", "Isabel", "ReinoUnido", "ElCarmen", "SanJosé", "LosGramos", "ElOlivar", "LaRambla", "Bel-Air"));
        malaga.put("Torremolinos", List.of("Centro", "La Carihuela", "El Bajondillo", "LosMonterros", "Cruz de Humilladero", "San Miguel", "ElRetiro", "Los“不是", "LaJara", "El Carmen"));
        malaga.put("Benalmádena", List.of("Centro", "Arroyo de la Miel", "Benalmádena Pueblo", "Torremuelle", "ElMuelle", "LaCuesta", "LosMonterros", "ElCarmen", "Los Pinos", "LaVera"));
        malaga.put("Fuengirola", List.of("Centro", "LosBoliches", "ElBoquetillo", "ElCarmen", "LosMonterros", "LaUnión",  "ElZorro", "ElPuerto", "Los Pinos"));
        malaga.put("Mijas", List.of("Centro", "Mijas Pueblo", "LasLagunas", "LaCala", "ElChopillo", "Los“不是", "ElCalario", "LaJara", "El Tío", "Los Pinos"));
        malaga.put("Ronda", List.of("Centro", "ElMercado", "San Francisco", "ElCarmen", "San Miguel", "ElSalvador", "LaCiudad", "ElTajo", "Los Remedios", "LaRanita"));
        malaga.put("Antequera", List.of("Centro", "LaVilla", "San Sebastián", "ElCarmen", "Los Remedios", "La Fuente", "El Pilar", "San Roque", "Los“不是", "El Tío"));
        malaga.put("Nerja", List.of("Centro", "ElCañuelo", "ElSaladillo", "Maro", "ElTINT", "Los“不是", "LaJara", "ElTío", "Los Pinos", "El Carmen"));
        malaga.put("Torre del Mar", List.of("Centro", "ElPuerto", "LaTorre", "Los“不是", "El Salado", "La Redondela", "El Carmen", "Los Pinos", "El Zorro", "La Perea"));
        malaga.put("Alhaurín de la Torre", List.of("Centro", "ElSantiago", "LaAlquería", "Los“不是", "ElSalado", "LaJara", "ElTío", "Los Pinos", "El Carmen", "La Granja"));
        malaga.put("Alhaurín el Grande", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Badul"));
        malaga.put("Coín", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Tío"));
        malaga.put("Río Real", List.of("Centro", "ElTajoe", "LosBoliches", "ElCarmen", "Los“不是", "LaJara", "ElTío", "Los Pinos", "ElPuerto", "Los Monteros"));
        andalucia.put("Málaga", malaga);

        // Sevilla
        Map<String, List<String>> sevilla = new LinkedHashMap<>();
        sevilla.put("Sevilla", List.of("Nervión", "Alameda de Hércules", "Macarena", "Triana", "Cerro-Amate", "Nervión-Santa Justa", "San Pablo-Santa Justa", "Los Remedios", "Casco Antiguo", "Sur", "Este", "Norte"));
        sevilla.put("Dos Hermanas", List.of("Centro", "San José de Orellana", "ElRocio", "LaNazarena", "Los“不是", "El Clem", "LaJara", "ElTío", "Los Pinos", "El Carmen"));
        sevilla.put("Alcalá de Guadaíra", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Badul"));
        sevilla.put("Utrera", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Tío"));
        sevilla.put("Mairena del Aljarafe", List.of("Centro", "El“不是", "LaJara", "ElTío", "Los Pinos", "El Carmen", "Los“不是", "El Salado", "La Redondela", "El Zorro"));
        sevilla.put("Los Palacios y Villafranca", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Badul"));
        sevilla.put("Carmona", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Tío"));
        sevilla.put("Écija", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Badul"));
        sevilla.put("Osuna", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Tío"));
        sevilla.put("Lora del Río", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Fuente", "El Pilar", "Los“不是", "El Badul"));
        sevilla.put("Camas", List.of("Centro", "LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano", "La Jara", "El Tío", "Los Pinos", "La Granja"));
        andalucia.put("Sevilla", sevilla);

        MAPA_GEOGRAFICO.put("Andalucía", andalucia);
    }

    public CityGeoData getGeoData(String ciudad) {
        if (cache.containsKey(ciudad)) return cache.get(ciudad);
        CityGeoData data = buscarEnMapa(ciudad);
        cache.put(ciudad, data);
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

    private CityGeoData buscarEnMapa(String ciudad) {
        for (Map<String, Map<String, List<String>>> comunidad : MAPA_GEOGRAFICO.values()) {
            for (Map.Entry<String, Map<String, List<String>>> entry : comunidad.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(ciudad)) {
                    String nombreCiudad = entry.getKey();
                    Map<String, List<String>> distritosMap = comunidad.get(nombreCiudad);
                    List<String> distritos = new ArrayList<>(distritosMap.keySet());
                    Map<String, List<String>> barriosPorDistrito = new LinkedHashMap<>();
                    for (String d : distritos) {
                        barriosPorDistrito.put(d, distritosMap.get(d));
                    }
                    return new CityGeoData(nombreCiudad, distritos, barriosPorDistrito);
                }
            }
        }
        return new CityGeoData(ciudad, List.of("Centro"), Map.of("Centro", List.of("Centro")));
    }
}