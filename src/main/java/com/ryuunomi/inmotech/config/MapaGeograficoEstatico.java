package com.ryuunomi.inmotech.config;

import java.util.*;

public class MapaGeograficoEstatico {

    public record Municipio(
        String nombre,
        double latitud,
        double longitud,
        Map<String, List<String>> distritosBarrios
    ) {}

    public record Provincia(
        String nombre,
        Map<String, Municipio> municipios
    ) {}

    public record ComunidadAutonoma(
        String nombre,
        Map<String, Provincia> provincias
    ) {}

    public static final Map<String, ComunidadAutonoma> MAPA_ESPANA;

    static {
        MAPA_ESPANA = new LinkedHashMap<>();

        // ================================================
        // ANDALUCÍA
        // ================================================
        Map<String, Provincia> andalucia = new LinkedHashMap<>();

        // Almería
        Map<String, Municipio> almeriaProv = new LinkedHashMap<>();
        almeriaProv.put("Almería", new Municipio("Almería", 36.8340, -2.4637, Map.of(
            "Centro", List.of("Almedinilla", "San Sebastián", "Cathedral", "Barrio Nuevo", "El Puche"),
            "Almedinilla", List.of("El Llano", "La Fuente", "San Roque", "Las Minas", "Los Ejidos"),
            "Cabo de Gata", List.of("Níjar Pueblo", "San Miguel", "Los Molinos", "Cabo de Gata", "La Jana"),
            "Retiro", List.of("El Chuche", "La Luz", "Los Amadores", "El Perul", "La Palmera")
        )));
        almeriaProv.put("Roquetas de Mar", new Municipio("Roquetas de Mar", 36.7642, -2.6106, Map.of(
            "Centro", List.of("Roquetas Pueblo", "La Granada", "Las Salinas", "El Puerto", "Los Aguaducos"),
            "Urbanizaciones", List.of("El Puerto", "Los Mollendos", "Las Lomas", "El Romeral", "La Encina")
        )));
        almeriaProv.put("El Ejido", new Municipio("El Ejido", 36.7769, -2.8116, Map.of(
            "Centro", List.of("Centro", "Pampanico", "San Agustín", "Almerimar", "Las Norias"),
            "Ejido Norte", List.of("El Parador", "Las Minas", "La Gangosa", "Los Zocados"),
            "Ejido Sur", List.of("Altabix", "San Nicolás", "El Argar", "Los Martinez")
        )));
        almeriaProv.put("Níjar", new Municipio("Níjar", 36.9670, -2.1315, Map.of(
            "Níjar Pueblo", List.of("Níjar Centro", "San Miguel", "Los Molinos", "La Jana", "El Nazar"),
            "Cabo de Gata", List.of("Cabo de Gata", "La Angeles", "El Barronal", "Los Mineros"),
            "Campo de Níjar", List.of("El Alquián", "La Venta", "Los Tarambayes", "El Sopalmo")
        )));
        almeriaProv.put("Vera", new Municipio("Vera", 37.2500, -1.8667, Map.of(
            "Vera Pueblo", List.of("Vera Centro", "El Badul", "Los Baños", "Llano de Don Juan", "La Jauca"),
            "Vera Playa", List.of("Vera Playa", "El Puerto", "La Gangosa", "Los Maños", "El Chamusc")
        )));
        almeriaProv.put("Adra", new Municipio("Adra", 36.7496, -3.0341, Map.of(
            "Adra Centro", List.of("Adra Centro", "El Lance", "La Curva", "San Miguel", "La Alcazaba"),
            "Adra Norte", List.of("El Lanzallave", "La Tref", "Los Maldiciones", "El Salado"),
            "Adra Sur", List.of("La Carata", "El Zarpoll", "Los Vientos", "La Molineta")
        )));
        almeriaProv.put("Cuevas del Almanzora", new Municipio("Cuevas del Almanzora", 37.2969, -1.8835, Map.of(
            "Cuevas Pueblo", List.of("Cuevas Centro", "San Juan", "El Arteal", "Los Lobos", "El Gabar"),
            "Puerto Rey", List.of("Puerto Rey", "Los Cocones", "El Saladillo", "La Jabonera")
        )));
        almeriaProv.put("Huércal-Overa", new Municipio("Huércal-Overa", 37.3939, -1.9429, Map.of(
            "Huércal Centro", List.of("Santa María", "El Realengo", "Los Medinas", "La Hornera", "El Calar"),
            "Huércal Sur", List.of("La Station", "Los Callejones", "El Arbit", "La Fuente")
        )));
        almeriaProv.put("Albox", new Municipio("Albox", 37.3789, -2.1467, Map.of(
            "Albox Centro", List.of("El Badul", "Los Baños", "Llano de Don Juan", "La Jauca", "El Argar"),
            "Albox Norte", List.of("Los Conejos", "La Cañada", "El Margen", "Los Curas")
        )));
        andalucia.put("Almería", new Provincia("Almería", almeriaProv));

        // Cádiz
        Map<String, Municipio> cadizProv = new LinkedHashMap<>();
        cadizProv.put("Jerez de la Frontera", new Municipio("Jerez de la Frontera", 36.6850, -6.1269, Map.of(
            "Centro", List.of("San Miguel", "Santiago", "San Dionisio", "La Ina", "El Portal"),
            "San Juan de Dios", List.of("El Altillo", "El Majuelo", "La Granja", "Los SSantos"),
            "Periferia", List.of("La Mash", "El Driego", "Los Remedios", "El Tío", "La Jara"),
            "Surf", List.of("Guadalete", "El Portal", "La Dehesa", "Los Califas")
        )));
        cadizProv.put("Algeciras", new Municipio("Algeciras", 36.1407, -5.4536, Map.of(
            "Centro", List.of("San García", "El Saladillo", "Getares", "El Rinconcillo", "La Juliana"),
            "Los Portales", List.of("Cotorrillo", "Campo de Gibraltar", "Las Huertas", "El Sol"),
            "San García", List.of("Los Barriales", "El Salado", "La Juncosa", "El Bukan", "Los Tarambanes")
        )));
        cadizProv.put("Cádiz", new Municipio("Cádiz", 36.5271, -6.1897, Map.of(
            "Centro", List.of("Santa María", "La Viña", "El Pópulo", "Catedral", "San Antonio"),
            "Plaza de España", List.of("Barriada ElBufador", "Zabal", "Cortadura", "Valcárcamo"),
            "La Manzanilla", List.of("Barriada Lesseps", "El Mentidero", "San Antonio", "El Junco")
        )));
        cadizProv.put("El Puerto de Santa María", new Municipio("El Puerto de Santa María", 36.5942, -6.2286, Map.of(
            "Centro", List.of("La Puntilla", "San Marcos", "Valdelagrana", "El Portal", "Los Frailes"),
            "La Margarita", List.of("El Carmen", "Pinillo", "Vistahermosa", "La Mariana"),
            "Costa", List.of("El Torreón", "Los盲区", "El Salado", "La Redondela")
        )));
        cadizProv.put("Sanlúcar de Barrameda", new Municipio("Sanlúcar de Barrameda", 36.7764, -6.3518, Map.of(
            "Centro", List.of("Bajo de Guía", "San Lázaro", "El Carmen", "La Jara", "Doña Blanco"),
            "Las Piletas", List.of("El Mancud", "Barriada de la Blanca", "Colinas", "Las Piletas"),
            "Bonanza", List.of("Bonanza", "El HDul", "Los Remedios", "El Salado", "La Perea")
        )));
        cadizProv.put("Chiclana de la Frontera", new Municipio("Chiclana de la Frontera", 36.4184, -6.1516, Map.of(
            "Centro", List.of("San Antonio", "El Arenal", "La Janda", "El Sotillo", "Los Boliches"),
            "San Juan de los Terreros", List.of("El Carbonal", "La Magdalena", "La Manchuela", "El Tío"),
            "La Banda", List.of("Los Predios", "El Badul", "La Jara", "El Salado", "Los Pinos")
        )));
        cadizProv.put("La Línea de la Concepción", new Municipio("La Línea de la Concepción", 36.1684, -5.3469, Map.of(
            "Centro", List.of("San Bernardo", "El Carmen", "La Atunara", "San Felipe", "Santa Margarita"),
            "El Puerto", List.of("Los Junquiles", "El Zabal", "La Granja", "Los Cucos")
        )));
        cadizProv.put("Rota", new Municipio("Rota", 36.6255, -6.3622, Map.of(
            "Centro", List.of("El Puerto", "Mina", "La Jodar", "Los Remedios", "El Carmen"),
            "San Bartolomé", List.of("Los Hoyos", "La Villa", "La Pita", "El Lance", "El Sopalmo")
        )));
        cadizProv.put("Arcos de la Frontera", new Municipio("Arcos de la Frontera", 36.7507, -5.6555, Map.of(
            "Centro Histórico", List.of("La Villa", "El Marquese", "San Miguel", "Los Molinos", "El Pilar"),
            "El Carmen", List.of("San Juan", "La Paz", "Los Predios", "El Badul", "La Jara")
        )));
        cadizProv.put("Puerto Real", new Municipio("Puerto Real", 36.5283, -6.1911, Map.of(
            "Centro", List.of("El Carmen", "La Music", "Los Remedios", "San José", "La Jarcia"),
            "El Zaporito", List.of("El Soldado", "Los Pinos", "La Pañol", "El Tío", "Los Pinos")
        )));
        cadizProv.put("Los Barrios", new Municipio("Los Barrios", 36.1858, -5.4921, Map.of(
            "Centro", List.of("Los Carros", "San Roque", "El Monatal", "La Jara", "Los Als"),
            "Los Patrón", List.of("Las Huertas", "El Campo", "San Andrés", "Los Patrón", "El Palmeral")
        )));
        andalucia.put("Cádiz", new Provincia("Cádiz", cadizProv));

        // Córdoba
        Map<String, Municipio> cordobaProv = new LinkedHashMap<>();
        cordobaProv.put("Córdoba", new Municipio("Córdoba", 37.8882, -4.7794, Map.of(
            "Centro", List.of("San Miguel", "Capuchinos", "La Magdalena", "San Pedro", "El Brillante"),
            "El Campo Madre de Dios", List.of("La Fuensanta", "Aguas Claras", "El Patriarca", "El.notset"),
            "Levantes", List.of("El Olivo", "Los Pólvoras", "El Pimiento", "La Experiment")
        )));
        cordobaProv.put("Lucena", new Municipio("Lucena", 37.4088, -4.4851, Map.of(
            "Centro", List.of("San Jorge", "El Cuesto", "La Mina", "San Francisco", "Los Remedios"),
            "El Carmen", List.of("La Estrella", "Los Llanos", "Puente del Zújar", "El Tío", "Los Pinos")
        )));
        cordobaProv.put("Puente Genil", new Municipio("Puente Genil", 37.3895, -4.7407, Map.of(
            "Centro", List.of("El Palero", "La Salceda", "San Miguel", "El Carmen", "Los Molinos"),
            "La Jara", List.of("El Pilar", "El Salvador", "San Roque", "Los Predios")
        )));
        cordobaProv.put("Montilla", new Municipio("Montilla", 37.5863, -4.6377, Map.of(
            "Centro", List.of("La Villa", "San Bartolomé", "El Carmen", "San Juan", "Los Remedios"),
            "El Pilar", List.of("Santa María", "San Francisco", "Los López", "El Badul", "La Jara")
        )));
        cordobaProv.put("Cabra", new Municipio("Cabra", 37.4725, -4.4369, Map.of(
            "Centro", List.of("La Villa", "San Juan de la Cruz", "El Carmen", "Los Remedios", "El Pilar"),
            "San José", List.of("La Paz", "El Castillo", "Los Predios", "El Tío", "Los Pinos")
        )));
        cordobaProv.put("Priego de Córdoba", new Municipio("Priego de Córdoba", 37.4381, -4.1959, Map.of(
            "Centro", List.of("La Villa", "San Juan de la Puerta", "El Carmen", "Los Remedios", "La Fuente"),
            "El Castillo", List.of("San Roque", "La Solanilla", "Los Tís", "El Badul", "La Jara")
        )));
        cordobaProv.put("Rute", new Municipio("Rute", 37.3261, -4.3671, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "La Jara", List.of("El Torno", "Los Puentec", "La Granja", "Los Predios", "Los Pinos")
        )));
        cordobaProv.put("Baena", new Municipio("Baena", 37.5898, -4.3279, Map.of(
            "Centro", List.of("La Villa", "San Francisco", "El Carmen", "Los Remedios", "La Fuente"),
            "El Pilar", List.of("San Roque", "Los Vizca", "La Alberquilla", "El Badul", "La Jara")
        )));
        cordobaProv.put("Castro del Río", new Municipio("Castro del Río", 37.6914, -4.4737, Map.of(
            "Centro", List.of("La Villa", "San Miguel", "El Carmen", "Los Remedios", "La Fuente"),
            "El Pilar", List.of("Los Predios", "El Badul", "La Jara", "El Tío", "Los Pinos")
        )));
        cordobaProv.put("Espejo", new Municipio("Espejo", 37.6564, -4.5595, Map.of(
            "Centro", List.of("La Villa", "San Miguel", "El Carmen", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Tío", "Los Pinos", "El LMati", "El Badul", "La Jara")
        )));
        andalucia.put("Córdoba", new Provincia("Córdoba", cordobaProv));

        // Granada
        Map<String, Municipio> granadaProv = new LinkedHashMap<>();
        granadaProv.put("Granada", new Municipio("Granada", 37.1773, -3.5986, Map.of(
            "Centro", List.of("Albaicín", "Realejo", "Sacromonte", "Alhambra", "Carmen de los Molinos"),
            "San Pedro", List.of("San Miguel", "El Serrallo", "La Cartuja", "El Notmap", "Los Pavos"),
            "Chana", List.of("La Chana", "El Chaparral", "Los Carmenes", "El Olivo", "La Vergüenza")
        )));
        granadaProv.put("Motril", new Municipio("Motril", 36.7507, -3.5179, Map.of(
            "Centro", List.of("El Varadero", "La Granada", "Torrecuéllar", "La Jara", "San Roque"),
            "El Carmen", List.of("Los Albores", "El Puerto", "La Guillerna", "Los Predios", "Los Pinos")
        )));
        granadaProv.put("Almuñécar", new Municipio("Almuñécar", 36.7379, -3.6906, Map.of(
            "Centro", List.of("San Miguel", "El Cervillejo", "La Herradura", "Punta de la Mona", "El Secano"),
            "Los Tarambanes", List.of("El Lano", "La Rivera", "San Nicolás", "El Caballo", "Los Chinos")
        )));
        granadaProv.put("Loja", new Municipio("Loja", 37.1688, -4.1512, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Torno", "Los Puentec", "La Granja", "El Tío", "Los Pinos")
        )));
        granadaProv.put("Alhama de Granada", new Municipio("Alhama de Granada", 37.0006, -3.9858, Map.of(
            "Centro", List.of("La Villa", "San Juan", "El Carmen", "Los Remedios", "El Llano"),
            "La Jara", List.of("El Tío", "Los Pinos", "La Granja", "El Badul", "Los Predios")
        )));
        granadaProv.put("Santa Fe", new Municipio("Santa Fe", 37.1876, -3.7259, Map.of(
            "Centro", List.of("La Jara", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "El Pilar", List.of("La Fuente", "San Roque", "Los Vizca", "El Badul", "La Jara")
        )));
        granadaProv.put("Illora", new Municipio("Illora", 37.2923, -3.8659, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "La Fuente"),
            "El Pilar", List.of("El Llano", "Los Pinos", "La Jara", "El Tío", "El Badul")
        )));
        granadaProv.put("Padul", new Municipio("Padul", 37.0233, -3.6870, Map.of(
            "Centro", List.of("El Carmen", "La Fuente", "San Juan", "Los Remedios", "El Llano"),
            "La Jara", List.of("El Tío", "Los Pinos", "La Granja", "El Badul", "Los Predios")
        )));
        granadaProv.put("Huétor Tájar", new Municipio("Huétor Tájar", 37.1844, -4.0505, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Badul", "La Jara", "El Tío")
        )));
        granadaProv.put("Ogíjares", new Municipio("Ogíjares", 37.1319, -3.6972, Map.of(
            "Centro", List.of("El Carmen", "La Fuente", "San Juan", "Los Remedios", "El Llano"),
            "La Jara", List.of("El Tío", "Los Pinos", "La Granja", "El Badul", "Los Predios")
        )));
        andalucia.put("Granada", new Provincia("Granada", granadaProv));

        // Huelva
        Map<String, Municipio> huelvaProv = new LinkedHashMap<>();
        huelvaProv.put("Huelva", new Municipio("Huelva", 37.2204, -6.9498, Map.of(
            "Centro", List.of("El Carmen", "San Sebastián", "Laorde", "Los Dolores", "El Matadero"),
            "La Placeta", List.of("El Molino", "Higueral", "Los Pinos", "Los Predios", "El Salado"),
            "Costa", List.of("Punta Umbría", "Isla Cristina", "Lepe", "Ayamonte", "Cartaya")
        )));
        huelvaProv.put("Lepe", new Municipio("Lepe", 37.2548, -7.2045, Map.of(
            "Centro", List.of("La Antilla", "El Terrón", "Los Limoneros", "El Choque", "La Redondela"),
            "Isla Cristina", List.of("El Puerto", "Los Predios", "El Salado", "La Redondela", "El LMati")
        )));
        huelvaProv.put("Almonte", new Municipio("Almonte", 37.1657, -6.5297, Map.of(
            "Centro", List.of("Matalascañas", "ElRocio", "Los Cabezos", "La NASA", "El LIR"),
            "Los Predios", List.of("El Pastizal", "El Salado", "La Perea", "El Tío", "Los Pinos")
        )));
        huelvaProv.put("Ayamonte", new Municipio("Ayamonte", 37.2098, -7.4097, Map.of(
            "Centro", List.of("La Villa", "El Puerto", "La Antilla", "Isla Cristina", "El Terrón"),
            "Los Predios", List.of("El Salado", "La Redondela", "El LMati", "El Badul", "La Jara")
        )));
        huelvaProv.put("Moguer", new Municipio("Moguer", 37.2756, -6.8385, Map.of(
            "Centro", List.of("Palos de la Frontera", "Mazagón", "ElLIR", "Los Predios", "El Puerto"),
            "La Fuente", List.of("El Carmen", "Los Pinos", "El Salado", "La Juncosa", "El Bukan")
        )));
        huelvaProv.put("Aljaraque", new Municipio("Aljaraque", 37.2674, -7.0218, Map.of(
            "Centro", List.of("El Torreón", "Los Predios", "El Salado", "La Fuente", "El Carmen"),
            "El Puerto", List.of("El Almendral", "La Jara", "El Tío", "Los Pinos", "La Granja")
        )));
        huelvaProv.put("Cartaya", new Municipio("Cartaya", 37.2819, -7.1504, Map.of(
            "Centro", List.of("El Puerto", "La Antilla", "El Terrón", "Los Predios", "El Salado"),
            "La Redondela", List.of("El Carmen", "Los Pinos", "El Zorro", "El Badul", "La Jara")
        )));
        huelvaProv.put("San Juan del Puerto", new Municipio("San Juan del Puerto", 37.3157, -6.8431, Map.of(
            "Centro", List.of("El Carmen", "La Fuente", "San Juan", "Los Remedios", "El Llano"),
            "La Jara", List.of("El Tío", "Los Pinos", "La Granja", "El Badul", "Los Predios")
        )));
        huelvaProv.put("Gibraleón", new Municipio("Gibraleón", 37.3714, -7.0718, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "San Roque", "Los Vizca", "El Badul", "La Jara")
        )));
        huelvaProv.put("Bollullos par del Condado", new Municipio("Bollullos par del Condado", 37.3384, -6.5378, Map.of(
            "Centro", List.of("El Carmen", "La Fuente", "San Juan", "Los Remedios", "El Llano"),
            "La Jara", List.of("El Tío", "Los Pinos", "La Granja", "El Badul", "Los Predios")
        )));
        andalucia.put("Huelva", new Provincia("Huelva", huelvaProv));

        // Jaén
        Map<String, Municipio> jaenProv = new LinkedHashMap<>();
        jaenProv.put("Jaén", new Municipio("Jaén", 37.7796, -3.7885, Map.of(
            "Centro", List.of("San Juan", "ElCarmen", "La Magdalena", "Los olivares", "El Almorran"),
            "La Jara", List.of("El Seda", "Los Predios", "El Zorro", "El Tío", "Los Pinos")
        )));
        jaenProv.put("Linares", new Municipio("Linares", 38.0873, -3.6321, Map.of(
            "Centro", List.of("El Carmen", "La Magdalena", "San Juan", "Los olivares", "El Almorran"),
            "La Jara", List.of("El Seda", "Los Predios", "El Tío", "Los Pinos", "La Granja")
        )));
        jaenProv.put("Andújar", new Municipio("Andújar", 38.0394, -4.0505, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Badul", "La Jara", "El Tío")
        )));
        jaenProv.put("Úbeda", new Municipio("Úbeda", 38.0115, -3.3705, Map.of(
            "Centro", List.of("La Villa", "El Salvador", "San Juan de la Cruz", "ElCarmen", "Los Remedios"),
            "San Lorenzo", List.of("El Llano", "La Jara", "El Tío", "Los Pinos", "La Granja")
        )));
        jaenProv.put("Alcazar de San Juan", new Municipio("Alcazar de San Juan", 39.4716, -3.2099, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Badul", "La Jara", "El Tío")
        )));
        jaenProv.put("Alcalá la Real", new Municipio("Alcalá la Real", 37.4617, -3.9226, Map.of(
            "Centro", List.of("La Villa", "El Alcázar", "San Juan", "El Carmen", "Los Remedios"),
            "La Fuente", List.of("El Pilar", "El Llano", "Los Pinos", "El Badul", "La Jara")
        )));
        jaenProv.put("Baeza", new Municipio("Baeza", 37.9972, -3.4686, Map.of(
            "Centro", List.of("La Villa", "El Pópulo", "San Juan de la Cruz", "El Carmen", "Los Remedios"),
            "La Catedral", List.of("El Llano", "La Jara", "Los Predios", "El Tío", "Los Pinos")
        )));
        jaenProv.put("Torredelcampo", new Municipio("Torredelcampo", 37.9503, -3.9664, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Badul", "La Jara", "El Tío")
        )));
        jaenProv.put("Martos", new Municipio("Martos", 37.7158, -3.9722, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Tío", "El Badul", "La Jara")
        )));
        jaenProv.put("Cabra del Santo Cristo", new Municipio("Cabra del Santo Cristo", 37.6497, -3.3836, Map.of(
            "Centro", List.of("La Villa", "El Carmen", "San Juan", "Los Remedios", "El Llano"),
            "La Jara", List.of("El Tío", "Los Pinos", "La Granja", "El Badul", "Los Predios")
        )));
        andalucia.put("Jaén", new Provincia("Jaén", jaenProv));

        // Málaga
        Map<String, Municipio> malagaProv = new LinkedHashMap<>();
        malagaProv.put("Málaga", new Municipio("Málaga", 36.7213, -4.4214, Map.of(
            "Centro", List.of("LaMalagueta", "ElPerchel", "Bailén-Miraflores", "Carretera de Cádiz"),
            "Teatinos-Universidad", List.of("Limonar", "El Palo", "Ciudad Jardín", "Málaga Este"),
            "El Puerto", List.of("La Cruz", "San Rafael", "ElEjido", "Los Nortes", "El Carmen")
        )));
        malagaProv.put("Marbella", new Municipio("Marbella", 36.5099, -4.8862, Map.of(
            "Centro", List.of("Marbella Pueblo", "San Pedro de Alcántara", "Nueva Andalucía", "Elviria"),
            "Las Chapas", List.of("Cabo Pino", "Artola", "Casablanca", "Río Real", "LosMontermoso"),
            "La Milla", List.of("LaMilla", "El Walab", "Los Predios", "El Salado", "El Zorro")
        )));
        malagaProv.put("Estepona", new Municipio("Estepona", 36.4276, -5.1477, Map.of(
            "Centro", List.of("Paseo Marítimo", "ElPadron", "LaJohana", "Isabel", "ReinoUnido"),
            "El Carmen", List.of("SanJosé", "LosGramos", "ElOlivar", "LaRambla", "Bel-Air")
        )));
        malagaProv.put("Torremolinos", new Municipio("Torremolinos", 36.6204, -4.4998, Map.of(
            "Centro", List.of("La Carihuela", "El Bajondillo", "LosMonterros", "Cruz de Humilladero"),
            "San Miguel", List.of("ElRetiro", "Los Predios", "LaJara", "El Carmen", "Los Pinos")
        )));
        malagaProv.put("Benalmádena", new Municipio("Benalmádena", 36.5748, -4.5122, Map.of(
            "Centro", List.of("Arroyo de la Miel", "Benalmádena Pueblo", "Torremuelle", "ElMuelle"),
            "La Cuesta", List.of("LosMonterros", "ElCarmen", "Los Pinos", "LaVera", "Los Predios")
        )));
        malagaProv.put("Fuengirola", new Municipio("Fuengirola", 36.5440, -4.6249, Map.of(
            "Centro", List.of("LosBoliches", "ElBoquetillo", "ElCarmen", "LosMonterros", "LaUnión"),
            "El Zorro", List.of("ElPuerto", "Los Pinos", "El Salado", "La Redondela", "El Tío")
        )));
        malagaProv.put("Mijas", new Municipio("Mijas", 36.5958, -4.6373, Map.of(
            "Centro", List.of("Mijas Pueblo", "LasLagunas", "LaCala", "ElChopillo", "Los Predios"),
            "El Calario", List.of("LaJara", "El Tío", "Los Pinos", "La Granja", "El Badul")
        )));
        malagaProv.put("Ronda", new Municipio("Ronda", 36.7462, -5.1610, Map.of(
            "Centro", List.of("ElMercado", "San Francisco", "ElCarmen", "San Miguel", "ElSalvador"),
            "La Ciudad", List.of("ElTajo", "Los Remedios", "LaRanita", "El Tío", "Los Pinos")
        )));
        malagaProv.put("Antequera", new Municipio("Antequera", 37.0193, -4.5615, Map.of(
            "Centro", List.of("LaVilla", "San Sebastián", "ElCarmen", "Los Remedios", "La Fuente"),
            "El Pilar", List.of("San Roque", "Los Predios", "El Tío", "El Badul", "La Jara")
        )));
        malagaProv.put("Nerja", new Municipio("Nerja", 36.7528, -3.8744, Map.of(
            "Centro", List.of("ElCañuelo", "ElSaladillo", "Maro", "ElTINT", "Los Predios"),
            "La Jara", List.of("ElTío", "Los Pinos", "El Carmen", "El Salado", "El Zorro")
        )));
        malagaProv.put("Torre del Mar", new Municipio("Torre del Mar", 36.7497, -4.0936, Map.of(
            "Centro", List.of("ElPuerto", "LaTorre", "Los Predios", "El Salado", "La Redondela"),
            "El Carmen", List.of("Los Pinos", "El Zorro", "La Perea", "El Tío", "Los Pinos")
        )));
        malagaProv.put("Alhaurín de la Torre", new Municipio("Alhaurín de la Torre", 36.6578, -4.5619, Map.of(
            "Centro", List.of("ElSantiago", "LaAlquería", "Los Predios", "ElSalado", "LaJara"),
            "El Tío", List.of("Los Pinos", "El Carmen", "La Granja", "El Badul", "Los Predios")
        )));
        malagaProv.put("Alhaurín el Grande", new Municipio("Alhaurín el Grande", 36.6396, -4.6843, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Badul", "La Jara", "El Tío")
        )));
        malagaProv.put("Coín", new Municipio("Coín", 36.6595, -4.7566, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Tío", "El Badul", "La Jara")
        )));
        malagaProv.put("Río Real", new Municipio("Río Real", 36.5519, -4.8765, Map.of(
            "Centro", List.of("ElTajoe", "LosBoliches", "ElCarmen", "Los Predios", "LaJara"),
            "El Tío", List.of("Los Pinos", "ElPuerto", "Los Monteros", "El Salado", "La Perea")
        )));
        andalucia.put("Málaga", new Provincia("Málaga", malagaProv));

        // Sevilla
        Map<String, Municipio> sevillaProv = new LinkedHashMap<>();
        sevillaProv.put("Sevilla", new Municipio("Sevilla", 37.3891, -5.9845, Map.of(
            "Nervión", List.of("Nervión-Santa Justa", "San Pablo-Santa Justa", "El Alcalá", "Losiber"),
            "Alameda de Hércules", List.of("Macarena", "Triana", "Cerro-Amate", "Los Remedios"),
            "Casco Antiguo", List.of("Sur", "Este", "Norte", "Los Predios", "El Salado")
        )));
        sevillaProv.put("Dos Hermanas", new Municipio("Dos Hermanas", 37.2863, -5.9841, Map.of(
            "Centro", List.of("San José de Orellana", "ElRocio", "LaNazarena", "Los Predios", "El Clem"),
            "La Jara", List.of("ElTío", "Los Pinos", "El Carmen", "El Salado", "La Perea")
        )));
        sevillaProv.put("Alcalá de Guadaíra", new Municipio("Alcalá de Guadaíra", 37.3381, -5.8731, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Badul", "La Jara", "El Tío")
        )));
        sevillaProv.put("Utrera", new Municipio("Utrera", 37.1852, -5.7806, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Tío", "El Badul", "La Jara")
        )));
        sevillaProv.put("Mairena del Aljarafe", new Municipio("Mairena del Aljarafe", 37.3327, -6.0631, Map.of(
            "Centro", List.of("Los Predios", "LaJara", "ElTío", "Los Pinos", "El Carmen"),
            "El Salado", List.of("La Redondela", "El Zorro", "El Badul", "La Jara", "Los Pinos")
        )));
        sevillaProv.put("Los Palacios y Villafranca", new Municipio("Los Palacios y Villafranca", 37.2299, -5.9177, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Badul", "La Jara", "El Tío")
        )));
        sevillaProv.put("Carmona", new Municipio("Carmona", 37.4713, -5.6393, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Tío", "El Badul", "La Jara")
        )));
        sevillaProv.put("Écija", new Municipio("Écija", 37.5422, -5.0818, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Badul", "La Jara", "El Tío")
        )));
        sevillaProv.put("Osuna", new Municipio("Osuna", 37.2370, -5.1066, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Tío", "El Badul", "La Jara")
        )));
        sevillaProv.put("Lora del Río", new Municipio("Lora del Río", 37.2879, -5.5306, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Fuente", List.of("El Pilar", "Los Predios", "El Badul", "La Jara", "El Tío")
        )));
        sevillaProv.put("Camas", new Municipio("Camas", 37.4014, -6.0348, Map.of(
            "Centro", List.of("LaVilla", "ElCarmen", "SanJuan", "Los Remedios", "El Llano"),
            "La Jara", List.of("El Tío", "Los Pinos", "La Granja", "El Badul", "Los Predios")
        )));
        andalucia.put("Sevilla", new Provincia("Sevilla", sevillaProv));

        MAPA_ESPANA.put("Andalucía", new ComunidadAutonoma("Andalucía", andalucia));

        // ================================================
        // COMUNIDAD DE MADRID
        // ================================================
        Map<String, Provincia> madrid = new LinkedHashMap<>();
        Map<String, Municipio> madridProv = new LinkedHashMap<>();
        madridProv.put("Madrid", new Municipio("Madrid", 40.4168, -3.7038, Map.ofEntries(
                Map.entry("Centro", List.of("Sol", "Ópera", "Callao", "Gran Vía", "Chueca")),
                Map.entry("Retiro", List.of("Ibiza", "Niño Jesús", "Jerónimos", "Los Molinos", "Adelfas")),
                Map.entry("Salamanca", List.of("Salamanca", "Goya", "Lista", "Fuencarral", "El Viso")),
                Map.entry("Chamartín", List.of("Chamartín", "Hispanoamérica", "La Paz", "Ciudad Binéfar", "El Plantío")),
                Map.entry("Tetuán", List.of("Tetuán", "Cuzco", "Castillejos", "Almenara", "Valdeacederas")),
                Map.entry("Moncloa-Aravaca", List.of("Moncloa", "Aravaca", "Casa de Velázquez", "El Plantío", "Los Molinos")),
                Map.entry("Latina", List.of("Latina", "Los Cármenes", "Puerta del Ángel", "Alto del Extremadura", "Campamento")),
                Map.entry("Carabanchel", List.of("Carabanchel", "Buena Vista", "Opa", "San Isidro", "Los Pintores")),
                Map.entry("Usera", List.of("Usera", "Orcasur", "San Fermín", "Almendrales", "Moscardó")),
                Map.entry("Puente de Vallecas", List.of("Puente de Vallecas", "Entrevías", "San Diego", "Palomeras", "Numancia")),
                Map.entry("Moratalaz", List.of("Moratalaz", "Horcajo", "Marroquina", "Media Legua", "Pacífico")),
                Map.entry("Ciudad Lineal", List.of("Ciudad Lineal", "Ventas", "Pueblo Nuevo", "Quintana", "Conde", "San Juan Bautista", "Colina", "Atalaya", "Costillares")),
                Map.entry("San Blas-Canillejas", List.of("Canillejas", "Amposta", "Arcos", "Los Juan", "El Olivar")),
                Map.entry("Barajas", List.of("Barajas", "Alameda de Osuna", "Casco Histórico", "Timón", "Corralejos")),
                Map.entry("Fuencarral-El Pardo", List.of("El Pardo", "Fuentelarreyna", "Peñagrande", "Pilar", "La Paz")),
                Map.entry("Villa de Vallecas", List.of("Villa de Vallecas", "Ensanche Vallecas", "Santa Eugenia", "Cerro del Tajo")),
                Map.entry("Villaverde", List.of("Villaverde", "San Cristóbal", "Los Rosales", "Butarque", "Los Angeles"))
        )));
        madridProv.put("Alcalá de Henares", new Municipio("Alcalá de Henares", 40.4819, -3.3640, Map.of(
            "Centro", List.of("Centro", "Chapinería", "Río", "Unión", "San"),
            "Ensanche", List.of("El Laurel", "La Margarita", "Los Pinos", "El Carmen", "La Jara"),
            "El Balconcillo", List.of("San Isidoro", "Los Auch", "El Tío", "Los Pinos", "La Granja")
        )));
        madridProv.put("Fuenlabrada", new Municipio("Fuenlabrada", 40.2842, -3.8199, Map.of(
            "Centro", List.of("El Centro", "El Remedio", "La", "Los", "El"),
            "Loranca", List.of("Loranca", "Polvoranca", "Boadilla", "Fuenlabrada", "El"),
            "El Norte", List.of("El Zaina", "Los Predios", "La Jara", "El Tío", "Los Pinos")
        )));
        madridProv.put("Móstoles", new Municipio("Móstoles", 40.3226, -3.8650, Map.of(
            "Centro", List.of("El Salvador", "San Andrés", "La Unidad", "Los", "El"),
            "Móstoles", List.of("El Rey", "Villafontana", "Pintores", "Los", "El Notset"),
            "Norte", List.of("Soto de Móstoles", "El Soto", "Los Not", "El Salado", "La Perea")
        )));
        madridProv.put("Leganés", new Municipio("Leganés", 40.3281, -3.7645, Map.of(
            "Centro", List.of("Centro", "San Nicasio", "Soledad", "Los", "El"),
            "Leganés", List.of("Vereda de los", "El Predio", "Los Pinos", "El Carmen", "La Jara"),
            "Sur", List.of("Los Predios", "El Salado", "La Redondela", "El Zorro", "El Tío")
        )));
        madridProv.put("Getafe", new Municipio("Getafe", 40.3072, -3.7300, Map.of(
            "Centro", List.of("Getafe Centro", "San José", "Perales del Río", "El Bercial", "Los Santos"),
            "Sector", List.of("Sector 3", "El Maestro", "Los Predios", "El Salado", "La Jara")
        )));
        madridProv.put("Alcorcón", new Municipio("Alcorcón", 40.3458, -3.8333, Map.of(
            "Centro", List.of("San José de Valderas", "Los", "El", "X", "Y"),
            "Alcorcón", List.of("Pradillo", "El Soto", "Los Not", "El Salado", "La Perea"),
            "Norte", List.of("Los Predios", "El Tío", "Los Pinos", "La Granja", "El Badul")
        )));
        madridProv.put("Parla", new Municipio("Parla", 40.2364, -3.7679, Map.of(
            "Centro", List.of("Parla Centro", "San Blas", "El Torreón", "Los Predios", "El"),
            "Parla", List.of("Parla Este", "Parla Norte", "Los Predios", "El Salado", "La Jara")
        )));
        madridProv.put("Alcobendas", new Municipio("Alcobendas", 40.5475, -3.6400, Map.of(
            "Centro", List.of("Alcobendas Centro", "Rosa", "El notset", "F", "Los"),
            "La Chopera", List.of("La Chopera", "Arcos", "Los Juan", "El Salvador", "San"),
            "Valdemas", List.of("Valdelasfuentes", "Los Predios", "El Tío", "Los Pinos", "La Granja")
        )));
        madridProv.put("Torrejón de Ardoz", new Municipio("Torrejón de Ardoz", 40.4553, -3.4697, Map.of(
            "Centro", List.of("Torrejón Centro", "El", "Los", "F", "La"),
            "El Corte", List.of("El Corte", "Los Predios", "El Tío", "Los Pinos", "La Granja"),
            "San isidro", List.of("San isidro", "Los Predios", "El Salado", "La Jara", "El Tío")
        )));
        madridProv.put("Rivas-Vaciamadrid", new Municipio("Rivas-Vaciamadrid", 40.3379, -3.6966, Map.of(
            "Centro", List.of("Rivas Centro", "Santa Mónica", "Los", "El", "La"),
            "Rivas", List.of("Rivas Norte", "Rivas Sur", "Los Predios", "El Salado", "La Perea"),
            "Vaciamadrid", List.of("Vaciamadrid", "El Zorro", "El Tío", "Los Pinos", "La Granja")
        )));
        madridProv.put("San Sebastián de los Reyes", new Municipio("San Sebastián de los Reyes", 40.5770, -3.6259, Map.of(
            "Centro", List.of("San Sebastián Centro", "Rosa", "El notset", "F", "Los"),
            "De los Reyes", List.of("De los Reyes", "Los Predios", "El Tío", "Los Pinos", "La Granja"),
            "Mosrtros", List.of("Mosrtros", "El Salado", "La Jara", "El Tío", "Los Pinos")
        )));
        madridProv.put("Pozuelo de Alarcón", new Municipio("Pozuelo de Alarcón", 40.4327, -3.8137, Map.of(
            "Centro", List.of("Pozuelo Centro", "El", "Los", "La", "San"),
            "Pozuelo", List.of("Pozuelo Norte", "Pozuelo Sur", "Los Predios", "El Salado", "La Perea"),
            "Aravaca", List.of("Aravaca", "El Zorro", "El Tío", "Los Pinos", "La Granja")
        )));
        madridProv.put("Coslada", new Municipio("Coslada", 40.4276, -3.5606, Map.of(
            "Centro", List.of("Coslada Centro", "Barrio del", "Los", "El", "La"),
            "Coslada", List.of("Coslada Norte", "Coslada Sur", "Los Predios", "El Tío", "Los Pinos")
        )));
        madridProv.put("Rociana del Condé", new Municipio("Rociana del Condé", 37.2845, -6.6076, Map.of(
            "Centro", List.of("El Centro", "La", "Los", "El", "San")
        )));
        madrid.put("Madrid", new Provincia("Madrid", madridProv));
        MAPA_ESPANA.put("Comunidad de Madrid", new ComunidadAutonoma("Comunidad de Madrid", madrid));

        // ================================================
        // CATALUÑA
        // ================================================
        Map<String, Provincia> cataluna = new LinkedHashMap<>();
        Map<String, Municipio> barcelonaProv = new LinkedHashMap<>();
        barcelonaProv.put("Barcelona", new Municipio("Barcelona", 41.3851, 2.1734, Map.of(
            "Ciutat Vella", List.of("Barri Gòtic", "El Raval", "Barri Xinès", "Sant Pere, Santa Caterina i la Ribera"),
            "Eixample", List.of("Dreta de l'Eixample", "Esquerra de l'Eixample", "Antiga Esquerra de l'Eixample", "Nova Esquerra de l'Eixample"),
            "Gràcia", List.of("Gràcia", "Vila de Gràcia", "El Coll", "La Salut", "Camp d'en Grassot i Gràcia Nova"),
            "Horta-Guinardó", List.of("Horta", "Montbau", "Sant Genís", "El Carmel", "La Teixonera"),
            "Les Corts", List.of("Les Corts", "La Maternitat", "Pedralbes", "Les Planes", "Can Mantega"),
            "Nou Barris", List.of("Porta", "Torre Baró", "Ciutat Meridiana", "Vallbona", "Nou Barris"),
            "Sant Andreu", List.of("Sant Andreu", "Sant Andreu de Palomar", "Bon Pastor", "Baró de Viver", "Trinitat Vella"),
            "Sant Martí", List.of("Sant Martí", "El Clot", "El Camp del'Arpa", "El Poblenou", "Diagonal Mar"),
            "Sants-Montjuïc", List.of("Sants", "Hostafrancs", "La Bordeta", "Magòria", "Zona Franca"),
            "Sarrià-Sant Gervasi", List.of("Sarrià", "Sant Gervasi", "El Putxet", "El Farró", "Sarrià")
        )));
        barcelonaProv.put("L'Hospitalet de Llobregat", new Municipio("L'Hospitalet de Llobregat", 41.3597, 2.1003, Map.of(
            "Centro", List.of("Centre", "El Carrer", "Santa Eulàlia", "Sanfeliu", "Ciutat"),
            "Norte", List.of("Norte", "Collblanc", "Torrassa", "La Florida", "Les Planes"),
            "Sur", List.of("Bell", "La Torrasa", "El Sans", "San", "Los")
        )));
        barcelonaProv.put("Badalona", new Municipio("Badalona", 41.4489, 2.2454, Map.of(
            "Centro", List.of("Badalona Centre", "El Progrés", "Sant Roc", "Gorg", "Canyet"),
            "Norte", List.of("Sant Mori", "Bufalà", "Moreras", "Los Predios", "El Salado")
        )));
        barcelonaProv.put("Sabadell", new Municipio("Sabadell", 41.5483, 2.1094, Map.of(
            "Centro", List.of("Sabadell Centre", "Gràcia", "Concòrdia", "Creu de Barberà", "Can Rull"),
            "Norte", List.of("Sant Oleguer", "Els Padris", "Ca n'Oriac", "Los Not", "Los"),
            "Sur", List.of("La Concòrdia", "Can Maragall", "Can Rull", "Los Not", "El Salado")
        )));
        barcelonaProv.put("Terrassa", new Municipio("Terrassa", 41.5630, 2.0085, Map.of(
            "Centro", List.of("Terrassa Centre", "Casc Antic", "Montserrat", "El", "Los"),
            "Norte", List.of("Norte", "Sant Pere", "Les Martes", "Can Palet", "Vista"),
            "Sur", List.of("Sur", "Araints", "Can Roca", "Los Not", "El Salado")
        )));
        barcelonaProv.put("Mataró", new Municipio("Mataró", 41.5421, 2.4446, Map.of(
            "Centro", List.of("Mataró Centre", "Cerdanyola", "Cirera", "PerNAM", "Los Not"),
            "Mataró", List.of("Mataró Nord", "Mataró Sud", "Rocafonda", "Los Predios", "Los")
        )));
        barcelonaProv.put("Santa Coloma de Gramenet", new Municipio("Santa Coloma de Gramenet", 41.5285, 2.2096, Map.of(
            "Centro", List.of("Centre", "Singuerlín", "Santa Coloma", "Fondo", "Can Fran"),
            "Singuerlín", List.of("Singuerlín Nord", "Singuerlín Sud", "Los Predios", "Los Pinos", "La Granja")
        )));
        barcelonaProv.put("Granollers", new Municipio("Granollers", 41.6080, 2.2876, Map.of(
            "Centro", List.of("Granollers Centre", "El Congrés", "Los Not", "El", "La"),
            "Granollers", List.of("Granollers Nord", "Granollers Sud", "Palou", "Los Not", "Los")
        )));
        barcelonaProv.put("Manresa", new Municipio("Manresa", 41.7281, 1.8214, Map.of(
            "Centre", List.of("Manresa Centre", "Carme", "Valldaura", "Escodines", "Mion"),
            "Manresa", List.of("Manresa Nord", "Manresa Sud", "Los Not", "Els Pous", "La SE")
        )));
        barcelonaProv.put("Vilanova i la Geltrú", new Municipio("Vilanova i la Geltrú", 41.2249, 1.7256, Map.of(
            "Centre", List.of("Vilanova Centre", "Cubelles", "Garraf", "Los Not", "El"),
            "Vilanova", List.of("Vilanova Nord", "Vilanova Sud", "Sant", "Roc", "Los")
        )));
        barcelonaProv.put("Reus", new Municipio("Reus", 41.1561, 1.1085, Map.of(
            "Centre", List.of("Reus Centre", "El Cambril", "Sant", "La", "Els"),
            "Reus", List.of("Reus Nord", "Reus Sud", "Montserrat", "Los Not", "Els")
        )));
        barcelonaProv.put("Rubí", new Municipio("Rubí", 41.4923, 2.0330, Map.of(
            "Centre", List.of("Rubí Centre", "Sant", "Els", "El", "La"),
            "Rubí", List.of("Rubí Nord", "Rubí Sud", "Los Not", "El Salado", "La Perea")
        )));
        barcelonaProv.put("Viladecans", new Municipio("Viladecans", 41.2241, 2.0307, Map.of(
            "Centre", List.of("Viladecans Centre", "Can Palmer", "Sant", "Els", "El"),
            "Viladecans", List.of("Viladecans Nord", "Viladecans Sud", "Los Predios", "Los Pinos", "La Granja")
        )));
        barcelonaProv.put("Castelldefels", new Municipio("Castelldefels", 41.2779, 1.9705, Map.of(
            "Centre", List.of("Castelldefels Centre", "Bellamar", "Los Not", "El", "La"),
            "Castelldefels", List.of("Castelldefels Platja", "Castelldefels X", "Los Predios", "Los Pinos", "La Granja")
        )));
        barcelonaProv.put("Sant Boi de Llobregat", new Municipio("Sant Boi de Llobregat", 41.3468, 2.0345, Map.of(
            "Centre", List.of("Sant Boi Centre", "Canyars", "Los Not", "El", "La"),
            "Sant Boi", List.of("Sant Boi Nord", "Sant Boi Sud", "Los Predios", "El Salado", "La Jara")
        )));
        barcelonaProv.put("Cornellà de Llobregat", new Municipio("Cornellà de Llobregat", 41.3588, 2.0706, Map.of(
            "Centre", List.of("Cornellà Centre", "Sant", "Los Not", "El", "La"),
            "Cornellà", List.of("Cornellà Nord", "Cornellà Sud", "Los Predios", "El Tío", "Los Pinos")
        )));
        barcelonaProv.put("El Prat de Llobregat", new Municipio("El Prat de Llobregat", 41.3304, 2.0950, Map.of(
            "Centre", List.of("El Prat Centre", "Sant", "Los Not", "El", "La"),
            "El Prat", List.of("El Prat Nord", "El Prat Sud", "Les Canyes", "Los Not", "Els")
        )));
        cataluna.put("Barcelona", new Provincia("Barcelona", barcelonaProv));

        Map<String, Municipio> tarragonaProv = new LinkedHashMap<>();
        tarragonaProv.put("Tarragona", new Municipio("Tarragona", 41.1189, 1.2451, Map.of(
            "Centre", List.of("Casc Antic", "Port", "Sant", "Eixample", "Llevant"),
            "Tarragona", List.of("Tarragona Nord", "Tarragona Sud", "Bonavista", "El", "Els")
        )));
        tarragonaProv.put("Reus", new Municipio("Reus", 41.1561, 1.1085, Map.of(
            "Centre", List.of("Reus Centre", "Sant", "Casc Antic", "El", "Els")
        )));
        tarragonaProv.put("El Vendrell", new Municipio("El Vendrell", 41.2196, 1.5372, Map.of(
            "Centre", List.of("El Vendrell Centre", "Sant", "Comarruga", "Los Not", "El"),
            "El Vendrell", List.of("El Vendrell Nord", "El Vendrell Sud", "Los Predios", "Los Pinos", "La Granja")
        )));
        tarragonaProv.put("Tortosa", new Municipio("Tortosa", 40.8125, 0.5212, Map.of(
            "Centre", List.of("Tortosa Centre", "Sant", "Los Not", "El", "La"),
            "Tortosa", List.of("Tortosa Nord", "Tortosa Sud", "Remolins", "Los Not", "Els")
        )));
        cataluna.put("Tarragona", new Provincia("Tarragona", tarragonaProv));

        Map<String, Municipio> gironaProv = new LinkedHashMap<>();
        gironaProv.put("Girona", new Municipio("Girona", 41.9794, 2.8212, Map.of(
            "Centre", List.of("Girona Centre", "Barri Vell", "Mercer", "Sant", "Los Not"),
            "Girona", List.of("Girona Nord", "Girona Sud", "Santa Eugènia", "El", "Els")
        )));
        gironaProv.put("Figueres", new Municipio("Figueres", 42.2660, 2.9615, Map.of(
            "Centre", List.of("Figueres Centre", "Sant", "Los Not", "El", "La"),
            "Figueres", List.of("Figueres Nord", "Figueres Sud", "Los Not", "Els Pinos", "La Granja")
        )));
        gironaProv.put("Blanes", new Municipio("Blanes", 41.6741, 2.7721, Map.of(
            "Centre", List.of("Blanes Centre", "Sant", "Los Not", "El", "La"),
            "Blanes", List.of("Blanes Nord", "Blanes Sud", "Los Predios", "Los Pinos", "La Granja")
        )));
        gironaProv.put("Lleida", new Municipio("Lleida", 41.6176, 0.6200, Map.of(
            "Centre", List.of("Lleida Centre", "Casc Antic", "Xalets", "Sant", "Los Not"),
            "Lleida", List.of("Lleida Nord", "Lleida Sud", "Canto", "El", "Els")
        )));
        cataluna.put("Girona", new Provincia("Girona", gironaProv));
        cataluna.put("Lleida", new Provincia("Lleida", new LinkedHashMap<>()));
        cataluna.put("Tarragona", new Provincia("Tarragona", tarragonaProv));
        MAPA_ESPANA.put("Cataluña", new ComunidadAutonoma("Cataluña", cataluna));

        // ================================================
        // COMUNIDAD VALENCIANA
        // ================================================
        Map<String, Provincia> comunidadValenciana = new LinkedHashMap<>();
        Map<String, Municipio> alicanteProv = new LinkedHashMap<>();
        alicanteProv.put("Alicante", new Municipio("Alicante", 38.3452, -0.4815, Map.of(
            "Centro", List.of("Centro", "Santa María", "San Fernando", "El Carmen", "Barri Vell"),
            "Ensanche", List.of("Ensanche", "Playa San Juan", "Benalúa", "San Gabriel", "El Altet"),
            "Norte", List.of("Norte", "Ciudad Asís", "San Pascual", "Los", "El")
        )));
        alicanteProv.put("Elche", new Municipio("Elche", 38.2669, -0.6986, Map.of(
            "Centro", List.of("Centro", "Altzave", "El Plantío", "Torre de", "Los"),
            "Elche", List.of("Elche Norte", "Elche Sud", "Los Not", "Els", "Les"),
            "Campus", List.of("Campus", "Altabix", "San Nicolás", "El Argar", "Los Not")
        )));
        alicanteProv.put("Benidorm", new Municipio("Benidorm", 38.5382, -0.1315, Map.of(
            "Centro", List.of("Centro", "Levante", "Casco Antiguo", "El Terrer", "Els"),
            "Benidorm", List.of("Benidorm Nord", "Benidorm Sud", "Finestrat", "La Cala", "Los"),
            "Sierra", List.of("Sierra Helada", "Los Not", "El Tío", "Los Pinos", "La Granja")
        )));
        alicanteProv.put("Alcoy", new Municipio("Alcoy", 38.7054, -0.4745, Map.of(
            "Centro", List.of("Centro", "Santa María", "Sant Roc", "El", "Els"),
            "Alcoy", List.of("Alcoy Nord", "Alcoy Sud", "Evaristo", "Los Not", "Els")
        )));
        alicanteProv.put("Torrevieja", new Municipio("Torrevieja", 37.9786, -0.6823, Map.of(
            "Centro", List.of("Centro", "La Mata", "Los", "El", "La"),
            "Torrevieja", List.of("Torrevieja Nord", "Torrevieja Sud", "La Morgade", "Los Not", "Els")
        )));
        alicanteProv.put("Orihuela", new Municipio("Orihuela", 38.0847, -0.9449, Map.of(
            "Centro", List.of("Centro", "Sant", "Los Not", "El", "La"),
            "Orihuela", List.of("Orihuela Nord", "Orihuela Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        alicanteProv.put("San Vicente del Raspeig", new Municipio("San Vicente del Raspeig", 38.3966, -0.5253, Map.of(
            "Centre", List.of("Sant Vicent Centre", "Los Not", "El", "La", "Los"),
            "San Vicente", List.of("San Vicente Nord", "San Vicente Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        comunidadValenciana.put("Alicante", new Provincia("Alicante", alicanteProv));

        Map<String, Municipio> castellonProv = new LinkedHashMap<>();
        castellonProv.put("Castellón de la Plana", new Municipio("Castellón de la Plana", 39.9864, -0.0513, Map.of(
            "Centro", List.of("Centro", "Playa", "Els", "El", "La"),
            "Castellón", List.of("Castellón Nord", "Castellón Sud", "El Rafa", "Los Not", "Els")
        )));
        castellonProv.put("Villarreal", new Municipio("Villarreal", 39.9366, -0.1012, Map.of(
            "Centro", List.of("Villarreal Centre", "Sant", "Los Not", "El", "La"),
            "Villarreal", List.of("Villarreal Nord", "Villarreal Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castellonProv.put("Burriana", new Municipio("Burriana", 39.8889, -0.0917, Map.of(
            "Centro", List.of("Borriana Centre", "Sant", "Los Not", "El", "La"),
            "Burriana", List.of("Burriana Nord", "Burriana Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castellonProv.put("Benicasim", new Municipio("Benicasim", 40.0525, 0.0632, Map.of(
            "Centro", List.of("Benicàssim Centre", "Los Not", "El", "La", "Los"),
            "Benicasim", List.of("Benicàssim Platja", "El物", "Los Predios", "Els Pinos", "La Granja")
        )));
        comunidadValenciana.put("Castellón", new Provincia("Castellón", castellonProv));

        Map<String, Municipio> valenciaProv = new LinkedHashMap<>();
        valenciaProv.put("Valencia", new Municipio("Valencia", 39.4699, -0.3763, Map.ofEntries(
                Map.entry("Ciutat Vella", List.of("El Carme", "El Mercat", "Sant Francesc", "La Seu", "El Pilar")),
                Map.entry("Eixample", List.of("Ruzafa", "Russafa", "El Botànic", "La Petxina", "Aiora")),
                Map.entry("Extramurs", List.of("El Toll", "El Botànic", "La Petxina", "Sant Antoni", "Barri del Carme")),
                Map.entry("Campanar", List.of("Campanar", "Sant Pau", "Tossal", "La Creu", "Els")),
                Map.entry("Poblats Marítims", List.of("El Grau", "El Cabanyal", "Canyamelar", "Nazaret", "Beteró")),
                Map.entry("Benimaclet", List.of("Benimaclet", "Camí de Vera", "La Lluna", "El Plantío", "Sant")),
                Map.entry("Quatre Carreres", List.of("Malilla", "Fonteta Sant", "Omet", "Massalanes", "La Torre")),
                Map.entry("Paterna", List.of("Paterna Centre", "Boger", "La Canyada", "Campament", "El")),
                Map.entry("Alzira", List.of("Alzira Centre", "Sant", "Los Not", "El", "La")),
                Map.entry("Gandía", List.of("Gandía Centre", "Sant", "Los Not", "El", "La")),
                Map.entry("Oliva", List.of("Oliva Centre", "Sant", "Los Not", "El", "La")),
                Map.entry("La Pobla de Vallbona", List.of("La Pobla Centre", "Sant", "Los Not", "El", "La"))
        )));
        valenciaProv.put("Mislata", new Municipio("Mislata", 39.4839, -0.4182, Map.of(
            "Centro", List.of("Mislata Centre", "Sant", "Los Not", "El", "La"),
            "Mislata", List.of("Mislata Nord", "Mislata Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        valenciaProv.put("Burjassot", new Municipio("Burjassot", 39.5105, -0.4533, Map.of(
            "Centro", List.of("Borjassot Centre", "Sant", "Los Not", "El", "La"),
            "Burjassot", List.of("Burjassot Nord", "Burjassot Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        valenciaProv.put("Paterna", new Municipio("Paterna", 39.5009, -0.4385, Map.of(
            "Centro", List.of("Paterna Centre", "Boger", "La Canyada", "Campament", "El"),
            "Paterna", List.of("Paterna Nord", "Paterna Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        valenciaProv.put("Alzira", new Municipio("Alzira", 39.1494, -0.4353, Map.of(
            "Centro", List.of("Alzira Centre", "Sant", "Los Not", "El", "La"),
            "Alzira", List.of("Alzira Nord", "Alzira Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        valenciaProv.put("Gandía", new Municipio("Gandía", 38.9680, -0.1807, Map.of(
            "Centro", List.of("Gandía Centre", "Sant", "Los Not", "El", "La"),
            "Gandía", List.of("Gandía Platja", "Gandía Centre", "Los Predios", "Els Pinos", "La Granja")
        )));
        valenciaProv.put("Sagunto", new Municipio("Sagunto", 39.6833, -0.2833, Map.of(
            "Centro", List.of("Sagunt Centre", "Port", "Los Not", "El", "La"),
            "Sagunto", List.of("Sagunt Nord", "Sagunt Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        comunidadValenciana.put("Valencia", new Provincia("Valencia", valenciaProv));
        MAPA_ESPANA.put("Comunidad Valenciana", new ComunidadAutonoma("Comunidad Valenciana", comunidadValenciana));

        // ================================================
        // PAÍS VASCO
        // ================================================
        Map<String, Provincia> paisVasco = new LinkedHashMap<>();
        Map<String, Municipio> bizkaiaProv = new LinkedHashMap<>();
        bizkaiaProv.put("Bilbao", new Municipio("Bilbao", 43.2630, -2.9350, Map.of(
            "Casco Viejo", List.of("Bilbao La Vieja", "San Adrián", "Done Bikendi", "Atxuri", "Errekalde"),
            "Ensanche", List.of("Indautxu", "Abando", "Ensanche", "San Francisco", "Ibaiondo"),
            "Uribarri", List.of("Uribarri", "Santutxu", "Txurdinaga", "Otxarkoaga", "Larraskitu"),
            "Deustu", List.of("Deusto", "San Pedro de Deusto", "Deustuko San Andres", "Ibarrekolanda", "Arangoiti"),
            "Begoña", List.of("Begoña", "Bolueta", "Kareaga", "Miribilla", "San Isidro"),
            "Basurto", List.of("Basurto", "Ametzola", "Zorrotza", "Kaskeas", "Olabeaga"),
            "Rekalde", List.of("Rekalde", "Isuri", "Errekalde", "Santutxu", "Colector"),
            "Algorta", List.of("Algorta", "Getxo", "Las Arenas", "Areeta", "Negubegizen")
        )));
        bizkaiaProv.put("Getxo", new Municipio("Getxo", 43.3569, -3.0116, Map.of(
            "Centro", List.of("Getxo Centro", "Algorta", "Las Arenas", "Areeta", "Los", "El"),
            "Getxo", List.of("Getxo Nord", "Getxo Sud", "Mungi", "Elexalde", "Las Arenas")
        )));
        bizkaiaProv.put("Barakaldo", new Municipio("Barakaldo", 43.2955, -2.9833, Map.of(
            "Centro", List.of("Barakaldo Centro", "San Vicente", "Lutxana", "Barakaldo", "San"),
            "Barakaldo", List.of("Barakaldo Nord", "Barakaldo Sud", "Errebal", "Los Not", "Els")
        )));
        bizkaiaProv.put("Portugalete", new Municipio("Portugalete", 43.3219, -3.0331, Map.of(
            "Centro", List.of("Portugalete Centro", "Sant", "Los Not", "El", "La"),
            "Portugalete", List.of("Portugalete Nord", "Portugalete Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        bizkaiaProv.put("Santurtzi", new Municipio("Santurtzi", 43.3153, -3.0276, Map.of(
            "Centro", List.of("Santurtzi Centro", "Sant", "Los Not", "El", "La"),
            "Santurtzi", List.of("Santurtzi Nord", "Santurtzi Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        bizkaiaProv.put("Basauri", new Municipio("Basauri", 43.2358, -2.8984, Map.of(
            "Centro", List.of("Basauri Centro", "Sant", "Los Not", "El", "La"),
            "Basauri", List.of("Basauri Nord", "Basauri Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        bizkaiaProv.put("Leioa", new Municipio("Leioa", 43.3263, -2.9885, Map.of(
            "Centro", List.of("Leioa Centro", "Sant", "Los Not", "El", "La"),
            "Leioa", List.of("Leioa Nord", "Leioa Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        bizkaiaProv.put("Durango", new Municipio("Durango", 43.1634, -2.6342, Map.of(
            "Centro", List.of("Durango Centro", "Sant", "Los Not", "El", "La"),
            "Durango", List.of("Durango Nord", "Durango Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        paisVasco.put("Bizkaia", new Provincia("Bizkaia", bizkaiaProv));

        Map<String, Municipio> gipuzkoaProv = new LinkedHashMap<>();
        gipuzkoaProv.put("San Sebastián", new Municipio("San Sebastián", 43.3183, -1.9812, Map.of(
            "Centro", List.of("Centro", "Parte Vieja", "Kursaal", "La Bretxa", "Alde Zaharra"),
            "Ensanche", List.of("Ensanche", "Amara", "Antiguo", "Loiola", "Egia"),
            "Zarautz", List.of("Zarautz Centro", "Zarautz", "Los Predios", "Els Pinos", "La Granja")
        )));
        gipuzkoaProv.put("Donostia", new Municipio("Donostia", 43.3183, -1.9812, Map.of(
            "Centro", List.of("Donostia Centro", "Parte Vieja", "Kursaal", "La Bretxa", "Alde Zaharra"),
            "Donostia", List.of("Donostia Nord", "Donostia Sud", "Aiete", "Ibaeta", "Miramon")
        )));
        gipuzkoaProv.put("Irún", new Municipio("Irún", 43.3390, -1.7894, Map.of(
            "Centro", List.of("Irún Centro", "Sant", "Los Not", "El", "La"),
            "Irún", List.of("Irún Nord", "Irún Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        gipuzkoaProv.put("Errenteria", new Municipio("Errenteria", 43.3137, -1.9028, Map.of(
            "Centro", List.of("Errenteria Centro", "Sant", "Los Not", "El", "La"),
            "Errenteria", List.of("Errenteria Nord", "Errenteria Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        gipuzkoaProv.put("Hernani", new Municipio("Hernani", 43.3347, -1.9141, Map.of(
            "Centro", List.of("Hernani Centro", "Sant", "Los Not", "El", "La"),
            "Hernani", List.of("Hernani Nord", "Hernani Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        gipuzkoaProv.put("Eibar", new Municipio("Eibar", 43.1851, -2.4717, Map.of(
            "Centro", List.of("Eibar Centro", "Sant", "Los Not", "El", "La"),
            "Eibar", List.of("Eibar Nord", "Eibar Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        paisVasco.put("Gipuzkoa", new Provincia("Gipuzkoa", gipuzkoaProv));

        Map<String, Municipio> alavaProv = new LinkedHashMap<>();
        alavaProv.put("Vitoria-Gasteiz", new Municipio("Vitoria-Gasteiz", 42.8599, -2.6828, Map.of(
            "Centro", List.of("Casco Viejo", "Ensanche", "Zaramaga", "San Martín", "Santa Maria"),
            "Ensanche", List.of("Ensanche", "Nuevo", "El", "Los", "La"),
            "Zabalgana", List.of("Zabalgana", "Txagorritxu", "Salburua", "Arriaga", "Adurza"),
            "Vitoria", List.of("Vitoria Nord", "Vitoria Sud", "Gamarra", "Zabale", "El Ariscal")
        )));
        alavaProv.put("Llodio", new Municipio("Llodio", 43.1467, -2.9633, Map.of(
            "Centro", List.of("Llodio Centro", "Sant", "Los Not", "El", "La"),
            "Llodio", List.of("Llodio Nord", "Llodio Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        alavaProv.put("Amurrio", new Municipio("Amurrio", 43.0521, -3.0011, Map.of(
            "Centro", List.of("Amurrio Centro", "Sant", "Los Not", "El", "La"),
            "Amurrio", List.of("Amurrio Nord", "Amurrio Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        paisVasco.put("Araba", new Provincia("Araba/Álava", alavaProv));
        MAPA_ESPANA.put("País Vasco", new ComunidadAutonoma("País Vasco", paisVasco));

        // ================================================
        // GALICIA
        // ================================================
        Map<String, Provincia> galicia = new LinkedHashMap<>();
        Map<String, Municipio> corunaProv = new LinkedHashMap<>();
        corunaProv.put("A Coruña", new Municipio("A Coruña", 43.3623, -8.4115, Map.of(
            "Centro", List.of("Ciudad Vieja", "Plaza de María Pita", "La Marina", "Riazor", "Orzán"),
            "Ensanche", List.of("Ensanche", "Juan Flórez", "Nostramo", "El temple", "Los", "La"),
            "Olas", List.of("Las Olmos", "Elvana", "Santa Lucía", "San Diego", "San Pedro"),
            "A Coruña", List.of("A Coruña Nord", "A Coruña Sud", "O Burgo", "Mesoiro", "El Temple")
        )));
        corunaProv.put("Santiago de Compostela", new Municipio("Santiago de Compostela", 42.8782, -8.5448, Map.of(
            "Centro", List.of("Casco Histórico", "A Mercede", "Santiago Centro", "San Marcos", "Facultades"),
            "Santiago", List.of("Santiago Nord", "Santiago Sud", "As Fontiñas", "O ensemble", "El")
        )));
        corunaProv.put("Ferrol", new Municipio("Ferrol", 43.4830, -8.2329, Map.of(
            "Centro", List.of("Ferrol Centro", "La Marina", "Real", "Catabois", "El"),
            "Ferrol", List.of("Ferrol Nord", "Ferrol Sud", "Ferrol Vello", "A Gándara", "O")
        )));
        corunaProv.put("Narón", new Municipio("Narón", 43.5093, -8.2090, Map.of(
            "Centro", List.of("Narón Centro", "Sant", "Los Not", "El", "La"),
            "Narón", List.of("Narón Nord", "Narón Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        corunaProv.put("Arteijo", new Municipio("Arteijo", 43.5016, -8.5096, Map.of(
            "Centro", List.of("Arteixo Centro", "Sant", "Los Not", "El", "La"),
            "Arteijo", List.of("Arteijo Nord", "Arteijo Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        corunaProv.put("Oleiros", new Municipio("Oleiros", 43.3619, -8.3266, Map.of(
            "Centro", List.of("Oleiros Centro", "Sant", "Los Not", "El", "La"),
            "Oleiros", List.of("Oleiros Nord", "Oleiros Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        corunaProv.put("Cambre", new Municipio("Cambre", 43.4128, -8.3438, Map.of(
            "Centro", List.of("Cambre Centro", "Sant", "Los Not", "El", "La"),
            "Cambre", List.of("Cambre Nord", "Cambre Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        corunaProv.put("Betanzos", new Municipio("Betanzos", 43.2795, -8.2134, Map.of(
            "Centro", List.of("Betanzos Centro", "Sant", "Los Not", "El", "La"),
            "Betanzos", List.of("Betanzos Nord", "Betanzos Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        galicia.put("A Coruña", new Provincia("A Coruña", corunaProv));

        Map<String, Municipio> lugoProv = new LinkedHashMap<>();
        lugoProv.put("Lugo", new Municipio("Lugo", 43.0099, -7.5560, Map.of(
            "Centro", List.of("Lugo Centro", "Casco Histórico", "A Mariña", "Santiago", "San Roque"),
            "Lugo", List.of("Lugo Nord", "Lugo Sud", "O Padornelo", "A Friger", "El")
        )));
        lugoProv.put("Monforte de Lemos", new Municipio("Monforte de Lemos", 42.5215, -7.5143, Map.of(
            "Centro", List.of("Monforte Centro", "Sant", "Los Not", "El", "La"),
            "Monforte", List.of("Monforte Nord", "Monforte Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        lugoProv.put("Viveiro", new Municipio("Viveiro", 43.6622, -7.5935, Map.of(
            "Centro", List.of("Viveiro Centro", "Sant", "Los Not", "El", "La"),
            "Viveiro", List.of("Viveiro Nord", "Viveiro Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        galicia.put("Lugo", new Provincia("Lugo", lugoProv));

        Map<String, Municipio> ourenseProv = new LinkedHashMap<>();
        ourenseProv.put("Ourense", new Municipio("Ourense", 42.3407, -7.8643, Map.of(
            "Centro", List.of("Ourense Centro", "Casco Histórico", "A Burbulla", "O Couto", "O POS"),
            "Ourense", List.of("Ourense Nord", "Ourense Sud", "O Cardal", "A Carra", "El")
        )));
        ourenseProv.put("O Barco de Valdeorras", new Municipio("O Barco de Valdeorras", 42.4181, -7.0003, Map.of(
            "Centro", List.of("O Barco Centro", "Sant", "Los Not", "El", "La"),
            "O Barco", List.of("O Barco Nord", "O Barco Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        ourenseProv.put("Verín", new Municipio("Verín", 41.9411, -7.4362, Map.of(
            "Centro", List.of("Verín Centro", "Sant", "Los Not", "El", "La"),
            "Verín", List.of("Verín Nord", "Verín Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        ourenseProv.put("Xinzo de Limia", new Municipio("Xinzo de Limia", 42.0607, -7.6167, Map.of(
            "Centro", List.of("Xinzo Centro", "Sant", "Los Not", "El", "La"),
            "Xinzo", List.of("Xinzo Nord", "Xinzo Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        galicia.put("Ourense", new Provincia("Ourense", ourenseProv));

        Map<String, Municipio> pontevedraProv = new LinkedHashMap<>();
        pontevedraProv.put("Vigo", new Municipio("Vigo", 42.2401, -8.7205, Map.of(
            "Centro", List.of("Casco Vello", "Plaza de Compostela", "O Berbés", "Isla de Samert", "La"),
            "Ensanche", List.of("Pizarro", "Cardenal", "Gran Vía", "El T", "Los"),
            "Vigo", List.of("Vigo Nord", "Vigo Sud", "Beiramar", "Coruxo", "O")
        )));
        pontevedraProv.put("Pontevedra", new Municipio("Pontevedra", 42.4299, -8.6444, Map.of(
            "Centro", List.of("Pontevedra Centro", "Casco Histórico", "A Peregrina", "San José", "O"),
            "Pontevedra", List.of("Pontevedra Nord", "Pontevedra Sud", "Monte Porreiro", "Los Not", "Els")
        )));
        pontevedraProv.put("Ponteareas", new Municipio("Ponteareas", 42.1693, -8.5054, Map.of(
            "Centro", List.of("Ponteareas Centro", "Sant", "Los Not", "El", "La"),
            "Ponteareas", List.of("Ponteareas Nord", "Ponteareas Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        pontevedraProv.put("Redondela", new Municipio("Redondela", 42.2845, -8.6208, Map.of(
            "Centro", List.of("Redondela Centro", "Sant", "Los Not", "El", "La"),
            "Redondela", List.of("Redondela Nord", "Redondela Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        pontevedraProv.put("Cangas", new Municipio("Cangas", 42.2626, -8.7834, Map.of(
            "Centro", List.of("Cangas Centro", "Sant", "Los Not", "El", "La"),
            "Cangas", List.of("Cangas Nord", "Cangas Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        pontevedraProv.put("Moaña", new Municipio("Moaña", 42.2488, -8.7438, Map.of(
            "Centro", List.of("Moaña Centro", "Sant", "Los Not", "El", "La"),
            "Moaña", List.of("Moaña Nord", "Moaña Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        pontevedraProv.put("Tui", new Municipio("Tui", 42.0485, -8.6452, Map.of(
            "Centro", List.of("Tui Centro", "Sant", "Los Not", "El", "La"),
            "Tui", List.of("Tui Nord", "Tui Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        pontevedraProv.put("Sanxenxo", new Municipio("Sanxenxo", 42.3957, -8.8083, Map.of(
            "Centro", List.of("Sanxenxo Centro", "Portonovo", "Sant", "Los Not", "El"),
            "Sanxenxo", List.of("Sanxenxo Nord", "Sanxenxo Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        pontevedraProv.put("O Grove", new Municipio("O Grove", 42.4626, -8.8714, Map.of(
            "Centro", List.of("O Grove Centro", "Sant", "Los Not", "El", "La"),
            "O Grove", List.of("O Grove Nord", "O Grove Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        galicia.put("Pontevedra", new Provincia("Pontevedra", pontevedraProv));
        MAPA_ESPANA.put("Galicia", new ComunidadAutonoma("Galicia", galicia));

        // ================================================
        // CASTILLA Y LEÓN
        // ================================================
        Map<String, Provincia> castillaLeon = new LinkedHashMap<>();
        Map<String, Municipio> avilaProv = new LinkedHashMap<>();
        avilaProv.put("Ávila", new Municipio("Ávila", 40.6566, -4.7000, Map.of(
            "Centro", List.of("Casco Histórico", "Centro", "Los", "El", "La"),
            "Ávila", List.of("Ávila Nord", "Ávila Sud", "San Antonio", "Los Not", "Els")
        )));
        avilaProv.put("Arévalo", new Municipio("Arévalo", 41.0654, -4.7163, Map.of(
            "Centro", List.of("Arévalo Centro", "Sant", "Los Not", "El", "La"),
            "Arévalo", List.of("Arévalo Nord", "Arévalo Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        avilaProv.put("Cedillo del Condado", new Municipio("Cedillo del Condado", 40.1256, -4.1065, Map.of(
            "Centro", List.of("Cedillo Centro", "Sant", "Los Not", "El", "La"),
            "Cedillo", List.of("Cedillo Nord", "Cedillo Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castillaLeon.put("Ávila", new Provincia("Ávila", avilaProv));

        Map<String, Municipio> burgosProv = new LinkedHashMap<>();
        burgosProv.put("Burgos", new Municipio("Burgos", 42.3500, -3.6800, Map.of(
            "Centro", List.of("Casco Histórico", "Centro", "Plaza Mayor", "Los", "El"),
            "Burgos", List.of("Burgos Nord", "Burgos Sud", "Gamonal", "El茄", "Els")
        )));
        burgosProv.put("Miranda de Ebro", new Municipio("Miranda de Ebro", 42.6858, -2.9474, Map.of(
            "Centro", List.of("Miranda Centro", "Sant", "Los Not", "El", "La"),
            "Miranda", List.of("Miranda Nord", "Miranda Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        burgosProv.put("Aranda de Duero", new Municipio("Aranda de Duero", 41.6734, -3.6799, Map.of(
            "Centro", List.of("Aranda Centro", "Sant", "Los Not", "El", "La"),
            "Aranda", List.of("Aranda Nord", "Aranda Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        burgosProv.put("Briviesca", new Municipio("Briviesca", 42.4239, -3.3072, Map.of(
            "Centro", List.of("Briviesca Centro", "Sant", "Los Not", "El", "La"),
            "Briviesca", List.of("Briviesca Nord", "Briviesca Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castillaLeon.put("Burgos", new Provincia("Burgos", burgosProv));

        Map<String, Municipio> leonProv = new LinkedHashMap<>();
        leonProv.put("León", new Municipio("León", 42.5987, -5.5671, Map.of(
            "Centro", List.of("Casco Histórico", "Centro", "Prado", "Los", "El"),
            "León", List.of("León Nord", "León Sud", "Los Not", "Els", "La")
        )));
        leonProv.put("Ponferrada", new Municipio("Ponferrada", 42.5466, -6.5985, Map.of(
            "Centro", List.of("Ponferrada Centro", "Sant", "Los Not", "El", "La"),
            "Ponferrada", List.of("Ponferrada Nord", "Ponferrada Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        leonProv.put("San Andrés del Rabanedo", new Municipio("San Andrés del Rabanedo", 42.6111, -5.6115, Map.of(
            "Centro", List.of("San Andrés Centro", "Sant", "Los Not", "El", "La"),
            "San Andrés", List.of("San Andrés Nord", "San Andrés Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        leonProv.put("Astorga", new Municipio("Astorga", 42.4592, -6.0637, Map.of(
            "Centro", List.of("Astorga Centro", "Sant", "Los Not", "El", "La"),
            "Astorga", List.of("Astorga Nord", "Astorga Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        leonProv.put("Bembibre", new Municipio("Bembibre", 42.6157, -6.4180, Map.of(
            "Centro", List.of("Bembibre Centro", "Sant", "Los Not", "El", "La"),
            "Bembibre", List.of("Bembibre Nord", "Bembibre Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        leonProv.put("Villafranca del Bierzo", new Municipio("Villafranca del Bierzo", 42.6101, -6.8124, Map.of(
            "Centro", List.of("Villafranca Centro", "Sant", "Los Not", "El", "La"),
            "Villafranca", List.of("Villafranca Nord", "Villafranca Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castillaLeon.put("León", new Provincia("León", leonProv));

        Map<String, Municipio> palenciaProv = new LinkedHashMap<>();
        palenciaProv.put("Palencia", new Municipio("Palencia", 42.0097, -4.7388, Map.of(
            "Centro", List.of("Palencia Centro", "Casco Histórico", "Los", "El", "La"),
            "Palencia", List.of("Palencia Nord", "Palencia Sud", "Los Not", "Els", "La")
        )));
        palenciaProv.put("Guardo", new Municipio("Guardo", 42.7929, -4.8505, Map.of(
            "Centro", List.of("Guardo Centro", "Sant", "Los Not", "El", "La"),
            "Guardo", List.of("Guardo Nord", "Guardo Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        palenciaProv.put("Aguilar de Campoo", new Municipio("Aguilar de Campoo", 42.7935, -4.2597, Map.of(
            "Centro", List.of("Aguilar Centro", "Sant", "Los Not", "El", "La"),
            "Aguilar", List.of("Aguilar Nord", "Aguilar Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castillaLeon.put("Palencia", new Provincia("Palencia", palenciaProv));

        Map<String, Municipio> salamancaProv = new LinkedHashMap<>();
        salamancaProv.put("Salamanca", new Municipio("Salamanca", 40.9650, -5.6639, Map.of(
            "Centro", List.of("Casco Histórico", "Centro", "Plaza Mayor", "Los", "El"),
            "Salamanca", List.of("Salamanca Nord", "Salamanca Sud", "Tormes", "Los Not", "Els")
        )));
        salamancaProv.put("Béjar", new Municipio("Béjar", 40.3828, -5.7642, Map.of(
            "Centro", List.of("Béjar Centro", "Sant", "Los Not", "El", "La"),
            "Béjar", List.of("Béjar Nord", "Béjar Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        salamancaProv.put("Zamora", new Municipio("Zamora", 41.6561, -5.7447, Map.of(
            "Centro", List.of("Zamora Centro", "Casco Histórico", "Los", "El", "La"),
            "Zamora", List.of("Zamora Nord", "Zamora Sud", "Los Not", "Els", "La")
        )));
        salamancaProv.put("Toro", new Municipio("Toro", 41.5232, -5.3973, Map.of(
            "Centro", List.of("Toro Centro", "Sant", "Los Not", "El", "La"),
            "Toro", List.of("Toro Nord", "Toro Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        salamancaProv.put("Benavente", new Municipio("Benavente", 42.0021, -5.5873, Map.of(
            "Centro", List.of("Benavente Centro", "Sant", "Los Not", "El", "La"),
            "Benavente", List.of("Benavente Nord", "Benavente Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castillaLeon.put("Salamanca", new Provincia("Salamanca", salamancaProv));
        castillaLeon.put("Zamora", new Provincia("Zamora", new LinkedHashMap<>()));

        Map<String, Municipio> segoviaProv = new LinkedHashMap<>();
        segoviaProv.put("Segovia", new Municipio("Segovia", 40.9429, -4.1088, Map.of(
            "Centro", List.of("Casco Histórico", "Centro", "Plaza Mayor", "Los", "El"),
            "Segovia", List.of("Segovia Nord", "Segovia Sud", "Los Not", "Els", "La")
        )));
        segoviaProv.put("Cuéllar", new Municipio("Cuéllar", 41.4037, -4.3073, Map.of(
            "Centro", List.of("Cuéllar Centro", "Sant", "Los Not", "El", "La"),
            "Cuéllar", List.of("Cuéllar Nord", "Cuéllar Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        segoviaProv.put("El Espinar", new Municipio("El Espinar", 40.9239, -4.2495, Map.of(
            "Centro", List.of("El Espinar Centro", "Sant", "Los Not", "El", "La"),
            "El Espinar", List.of("El Espinar Nord", "El Espinar Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        segoviaProv.put("San Ildefonso", new Municipio("San Ildefonso", 40.9007, -4.0070, Map.of(
            "Centro", List.of("San Ildefonso Centro", "Sant", "Los Not", "El", "La"),
            "San Ildefonso", List.of("San Ildefonso Nord", "San Ildefonso Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castillaLeon.put("Segovia", new Provincia("Segovia", segoviaProv));

        Map<String, Municipio> soriaProv = new LinkedHashMap<>();
        soriaProv.put("Soria", new Municipio("Soria", 41.7640, -2.4649, Map.of(
            "Centro", List.of("Soria Centro", "Casco Histórico", "Los", "El", "La"),
            "Soria", List.of("Soria Nord", "Soria Sud", "Los Not", "Els", "La")
        )));
        soriaProv.put("Almazán", new Municipio("Almazán", 41.4778, -2.5288, Map.of(
            "Centro", List.of("Almazán Centro", "Sant", "Los Not", "El", "La"),
            "Almazán", List.of("Almazán Nord", "Almazán Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        soriaProv.put("Golmayo", new Municipio("Golmayo", 41.7771, -2.4715, Map.of(
            "Centro", List.of("Golmayo Centro", "Sant", "Los Not", "El", "La"),
            "Golmayo", List.of("Golmayo Nord", "Golmayo Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castillaLeon.put("Soria", new Provincia("Soria", soriaProv));

        Map<String, Municipio> valladolidProv = new LinkedHashMap<>();
        valladolidProv.put("Valladolid", new Municipio("Valladolid", 41.6523, -4.7245, Map.of(
            "Centro", List.of("Casco Histórico", "Centro", "Plaza Mayor", "Pajaritos", "Casa del Rey"),
            "Valladolid", List.of("Valladolid Nord", "Valladolid Sud", "Parquesol", "La", "Els")
        )));
        valladolidProv.put("Medina del Campo", new Municipio("Medina del Campo", 41.3126, -4.9168, Map.of(
            "Centro", List.of("Medina Centro", "Sant", "Los Not", "El", "La"),
            "Medina", List.of("Medina Nord", "Medina Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        valladolidProv.put("Laguna de Duero", new Municipio("Laguna de Duero", 41.5850, -4.7564, Map.of(
            "Centro", List.of("Laguna Centro", "Sant", "Los Not", "El", "La"),
            "Laguna", List.of("Laguna Nord", "Laguna Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        valladolidProv.put("Arroyo de la Encomienda", new Municipio("Arroyo de la Encomienda", 41.6356, -4.7877, Map.of(
            "Centro", List.of("Arroyo Centro", "Sant", "Los Not", "El", "La"),
            "Arroyo", List.of("Arroyo Nord", "Arroyo Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        valladolidProv.put("Tordesillas", new Municipio("Tordesillas", 41.5026, -5.0028, Map.of(
            "Centro", List.of("Tordesillas Centro", "Sant", "Los Not", "El", "La"),
            "Tordesillas", List.of("Tordesillas Nord", "Tordesillas Sud", "Los Predios", "Els Pinos", "La Granja")
        )));
        castillaLeon.put("Valladolid", new Provincia("Valladolid", valladolidProv));
        MAPA_ESPANA.put("Castilla y León", new ComunidadAutonoma("Castilla y León", castillaLeon));
    }

    public static List<String> getAllMunicipios() {
        List<String> municipios = new ArrayList<>();
        for (ComunidadAutonoma comunidad : MAPA_ESPANA.values()) {
            for (Provincia provincia : comunidad.provincias().values()) {
                for (Municipio municipio : provincia.municipios().values()) {
                    municipios.add(municipio.nombre());
                }
            }
        }
        return municipios;
    }

    public static Municipio getMunicipio(String nombre) {
        for (ComunidadAutonoma comunidad : MAPA_ESPANA.values()) {
            for (Provincia provincia : comunidad.provincias().values()) {
                Municipio municipio = provincia.municipios().get(nombre);
                if (municipio != null) {
                    return municipio;
                }
            }
        }
        return null;
    }

    public static String getComunidad(String nombreMunicipio) {
        for (Map.Entry<String, ComunidadAutonoma> entry : MAPA_ESPANA.entrySet()) {
            for (Provincia provincia : entry.getValue().provincias().values()) {
                if (provincia.municipios().containsKey(nombreMunicipio)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static String getProvincia(String nombreMunicipio) {
        for (ComunidadAutonoma comunidad : MAPA_ESPANA.values()) {
            for (Map.Entry<String, Provincia> entry : comunidad.provincias().entrySet()) {
                if (entry.getValue().municipios().containsKey(nombreMunicipio)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}
