package com.ryuunomi.inmotech.config;

import com.ryuunomi.inmotech.entities.*;
import com.ryuunomi.inmotech.enums.*;
import com.ryuunomi.inmotech.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AgenciaRepository agenciaRepository;
    private final PlanRepository planRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final PropiedadRepository propiedadRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random(42);

    private static final String[] CIUDADES = {
        "Madrid", "Barcelona", "Valencia", "Malaga", "Sevilla", "Bilbao", "Zaragoza", "Murcia",
        "Palma de Mallorca", "Las Palmas de Gran Canaria", "Cordoba", "Alicante", "Valladolid",
        "Vigo", "Gijon", "Granada", "Elche", "Oviedo", "Santa Cruz de Tenerife", "Pamplona",
        "Almeria", "San Sebastian", "Burgos", "Santander", "Toledo", "Segovia", "Soria",
        "Cuenca", "Huesca", "Teruel", "Lleida", "Girona", "Tarragona", "Castellon",
        "Badajoz", "Caceres", "Huelva", "Cadiz", "Jaen", "Albacete",
        "Ciudad Real", "Guadalajara", "Salamanca", "Zamora", "Leon", "Palencia",
        "Pontevedra", "Lugo", "Ourense", "A Coruna", "Santiago de Compostela", "Avila",
        "Talavera de la Reina", "Rivas-Vaciamadrid", "Mostoles", "Alcorcon",
        "Getafe", "Fuenlabrada", "Leganes", "Alcobendas", "Torrejon de Ardoz", "Parla",
        "Alcala de Henares", "Manresa", "Mataro", "Granollers", "Vilanova i la Geltru",
        "Reus", "Badalona", "Hospitalet de Llobregat", "Sabadell", "Terrassa",
        "Marbella", "Estepona", "Torremolinos", "Benalmadena", "Fuengirola", "Velez-Malaga",
        "Cartagena", "Lorca", "Mazarrón", "Orihuela", "Elche", "Alcoy", "Benidorm", "Torrevieja",
        "Jerez de la Frontera", "Algeciras", "San Roque", "Linares", "Ubeda", "Baeza",
        "Merida", "Don Benito", "Almendralejo", "Zafra", "Trujillo", "Montijo",
        "Plasencia", "Navalmoral de la Mata", "Castuera", "Villafranca de los Barros",
        "Logroño", "Haro", "Calahorra", "Barbastro", "Monzon", "Calatayud",
        "Motril", "Roquetas de Mar", "El Ejido", "Punta Umbria", "Lepe", "Aljaraque"
    };

    private static final String[] CALLES = {
        "Calle Mayor", "Avenida Gran Via", "Paseo de la Castellana", "Calle Serrano",
        "Avenida Diagonal", "Calle Alcala", "Paseo de Recoletos", "Calle Preciados",
        "Avenida America", "Calle Ortega y Gasset", "Calle del Prado", "Paseo de la Vera",
        "Calle del Mar", "Avenida de la Playa", "Calle del Puerto", "Avenida del Puerto",
        "Calle Real", "Paseo de la Alameda", "Calle Nueva", "Avenida de la Constitucion"
    };

    private static final String[] TIPOS = {"PISO", "CASA", "CHALET", "ATICO", "ESTUDIO", "DUPLEX", "PENTHOUSE", "VILLA"};

    private static final String[] AGENCIAS_NOMBRES = {
        "Inmotech Madrid Centro", "Inmotech Barcelona Costa", "Inmotech Valencia Mediterranea",
        "Inmotech Andalucia Sur", "Inmotech Pais Vasco Norte", "Inmotech Galicia Atlantico",
        "Inmotech Castilla Leon", "Inmotech Murcia Sureste", "Inmotech Aragon Este", "Inmotech Canarias Global"
    };

    public DataSeeder(UsuarioRepository usuarioRepository, AgenciaRepository agenciaRepository,
                      PlanRepository planRepository, SuscripcionRepository suscripcionRepository,
                      PropiedadRepository propiedadRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.agenciaRepository = agenciaRepository;
        this.planRepository = planRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.propiedadRepository = propiedadRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            System.out.println("=== DATA SEEDER: Datos ya existentes, omitiendo ===");
            return;
        }

        System.out.println("=== DATA SEEDER: Creando datos masivos de prueba ===");

        Plan gratuito = new Plan("Gratuito", 2, 4, 1, BigDecimal.ZERO);
        gratuito.setStripePriceId("price_gratis");
        gratuito = planRepository.save(gratuito);

        Plan premium = new Plan("Premium", Integer.MAX_VALUE, Integer.MAX_VALUE, 1, new BigDecimal("9.99"));
        premium.setStripePriceId("price_premium");
        premium = planRepository.save(premium);

        Plan agenciaBasic = new Plan("Agencia Basic", 20, 50, 5, new BigDecimal("29.99"));
        agenciaBasic.setStripePriceId("price_agencia_basic");
        agenciaBasic = planRepository.save(agenciaBasic);

        Plan agenciaPremium = new Plan("Agencia Premium", Integer.MAX_VALUE, Integer.MAX_VALUE, 10, new BigDecimal("49.99"));
        agenciaPremium.setStripePriceId("price_agencia_premium");
        agenciaPremium = planRepository.save(agenciaPremium);

        Suscripcion suscGratis = new Suscripcion(TipoSuscripcion.GRATIS);
        suscGratis = suscripcionRepository.save(suscGratis);

        Suscripcion suscPremium = new Suscripcion(TipoSuscripcion.PREMIUM);
        suscPremium = suscripcionRepository.save(suscPremium);

        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setApellido("Inmotech");
        admin.setEmail("admin@inmotech.com");
        admin.setContrasenia(passwordEncoder.encode("123456"));
        admin.setTelefono("600123456");
        admin.setFechaNacimiento(LocalDate.of(1990, 1, 15));
        admin.setFechaRegistro(LocalDate.now());
        admin.setSuscripcion(suscPremium);
        admin.setFechaExpiracionPremium(LocalDate.now().plusDays(30));
        admin.setProvider(AuthProvider.LOCAL);
        admin.setCapacidades(Set.of(CapacidadUsuario.ADMIN, CapacidadUsuario.USUARIO));
        admin = usuarioRepository.save(admin);

        long startTime = System.currentTimeMillis();
        int totalPropiedadesCreadas = 0;

        for (int a = 0; a < AGENCIAS_NOMBRES.length; a++) {
            Plan planAgencia = (a < 7) ? agenciaPremium : agenciaBasic;
            LocalDate expiryPlan = LocalDate.now().plusDays(60 + random.nextInt(180));

            Agencia agencia = new Agencia();
            agencia.setNombre(AGENCIAS_NOMBRES[a]);
            agencia.setDescripcion("Agencia inmobiliaria " + AGENCIAS_NOMBRES[a] + " con mas de 10 anos de experiencia en el sector");
            agencia.setPlan(planAgencia);
            agencia.setFechaExpiracionPlan(expiryPlan);
            agencia = agenciaRepository.save(agencia);

            Suscripcion agenteSuscripcion = new Suscripcion(TipoSuscripcion.PREMIUM);
            agenteSuscripcion.setFechaFin(expiryPlan);
            agenteSuscripcion = suscripcionRepository.save(agenteSuscripcion);

            Usuario agente = new Usuario();
            agente.setNombre("Agente" + (a + 1));
            agente.setApellido("Inmotech");
            agente.setEmail("agente" + (a + 1) + "@inmotech.com");
            agente.setContrasenia(passwordEncoder.encode("123456"));
            agente.setTelefono("600" + String.format("%06d", 100000 + a * 11111));
            agente.setFechaNacimiento(LocalDate.of(1980 + random.nextInt(15), 1 + random.nextInt(12), 1 + random.nextInt(28)));
            agente.setFechaRegistro(LocalDate.now().minusDays(random.nextInt(365)));
            agente.setAgencia(agencia);
            agente.setSuscripcion(agenteSuscripcion);
            agente.setFechaExpiracionPremium(expiryPlan);
            agente.setProvider(AuthProvider.LOCAL);
            agente.setCapacidades(Set.of(CapacidadUsuario.AGENTE, CapacidadUsuario.USUARIO));
            agente = usuarioRepository.save(agente);

            int propiedadesCreadas = crearPropiedadesAgencia(agente, a);
            totalPropiedadesCreadas += propiedadesCreadas;

            if ((a + 1) % 2 == 0 || a == AGENCIAS_NOMBRES.length - 1) {
                long elapsed = System.currentTimeMillis() - startTime;
                System.out.printf("  Agencies %d/%d | %d props | %dms%n",
                    a + 1, AGENCIAS_NOMBRES.length, totalPropiedadesCreadas, elapsed);
            }
        }

        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("");
        System.out.println("=== DATA SEEDER: Datos de prueba creados ===");
        System.out.println("Admin: admin@inmotech.com / 123456");
        System.out.println("10 Agencias con plan (7 Premium, 3 Basic)");
        System.out.println("1 Agente por agencia con suscripcion propia");
        System.out.println("Ciudades: " + CIUDADES.length + " ciudades de Espana");
        System.out.println("Total propiedades creadas: " + totalPropiedadesCreadas);
        System.out.println("Tiempo total: " + totalTime + "ms");
    }

    private int crearPropiedadesAgencia(Usuario agente, int agenciaIndex) {
        int count = 0;
        int ciudadBase = agenciaIndex * 50;

        for (int i = 0; i < 50; i++) {
            String ciudad = CIUDADES[(ciudadBase + i) % CIUDADES.length];
            String tipo = TIPOS[random.nextInt(TIPOS.length)];
            boolean esAlquiler = random.nextDouble() < 0.3;

            BigDecimal precio;
            if (esAlquiler) {
                precio = BigDecimal.valueOf(400 + random.nextInt(1900)).multiply(BigDecimal.valueOf(50));
            } else {
                precio = BigDecimal.valueOf(80000 + random.nextInt(420000));
            }

            Propiedad p = new Propiedad();
            p.setTitulo(tipo + " en " + ciudad + " #" + (agenciaIndex * 50 + i));
            p.setDescripcion("Propiedad en " + ciudad + " con excelentes acabados, "
                + "ubicacion privilegiada y vistas magnificas. Ideal para "
                + (esAlquiler ? "alquiler a largo plazo o estudiantes" : "familias o inversores")
                + ". " + getDescripcionExtra(tipo));
            p.setPrecio(precio);
            p.setSuperficie(BigDecimal.valueOf(40 + random.nextInt(350)));
            p.setDireccion(CALLES[random.nextInt(CALLES.length)] + " " + (1 + random.nextInt(200)));
            p.setCiudad(ciudad);
            p.setProvincia(getProvincia(ciudad));
            p.setCodigoPostal(generarCodigoPostal(ciudad));
            p.setLatitud(generarLatitud(ciudad));
            p.setLongitud(generarLongitud(ciudad));
            p.setFechaPublicacion(LocalDateTime.now().minusDays(random.nextInt(180)));
            p.setUsuario(agente);
            p.setTipo(tipo);
            p.setOperacion(esAlquiler ? "ALQUILER" : "VENTA");
            p.setEliminada(false);
            propiedadRepository.save(p);
            count++;
        }
        return count;
    }

    private String getDescripcionExtra(String tipo) {
        return switch (tipo) {
            case "PISO", "ESTUDIO" -> "Dispone de salon-cocina, dormitorios amplios y banos completos.";
            case "CASA", "CHALET", "VILLA" -> "Villa unifamiliar con jardin privado, piscina y garaje.";
            case "ATICO", "PENTHOUSE" -> "Atico duplex con terraza panoramica y vistas 360 grados.";
            case "DUPLEX" -> "Duplex moderno con distribucion en dos plantas y maxima luminosidad.";
            default -> "Propiedad en excelentes condiciones, lista para entrar a vivir.";
        };
    }

    private String getProvincia(String ciudad) {
        String[] madrid = {"Madrid", "Alcala de Henares", "Fuenlabrada", "Mostoles", "Leganes", "Getafe", "Alcobendas", "Parla", "Torrejon de Ardoz", "Rivas-Vaciamadrid"};
        String[] barcelona = {"Barcelona", "L'Hospitalet", "Badalona", "Sabadell", "Terrassa", "Mataro", "Granollers", "Manresa", "Vilanova i la Geltru", "Reus"};
        String[] valencia = {"Valencia", "Alicante", "Elche", "Castellon", "Alcoy", "Benidorm", "Orihuela", "Torrevieja"};
        String[] sevilla = {"Sevilla", "Cadiz", "Jerez de la Frontera", "Algeciras", "Huelva", "Cordoba", "Granada", "Malaga", "Marbella", "Estepona"};
        String[] bilbao = {"Bilbao", "Vitoria-Gasteiz", "San Sebastian", "Santander", "Burgos", "Logroño"};
        String[] galicia = {"A Coruna", "Santiago de Compostela", "Lugo", "Ourense", "Pontevedra", "Vigo", "Gijon"};
        String[] castillaLe = {"Valladolid", "Leon", "Palencia", "Zamora", "Salamanca", "Segovia", "Avila", "Soria", "Burgos"};
        String[] murcia = {"Murcia", "Cartagena", "Lorca", "Mazarrón", "Orihuela"};
        String[] aragon = {"Zaragoza", "Huesca", "Teruel", "Calatayud", "Barbastro", "Monzon"};
        String[] canarias = {"Las Palmas de Gran Canaria", "Santa Cruz de Tenerife"};

        if (contains(madrid, ciudad)) return "Madrid";
        if (contains(barcelona, ciudad)) return "Barcelona";
        if (contains(valencia, ciudad)) return "Valencia";
        if (contains(sevilla, ciudad)) return "Sevilla";
        if (contains(bilbao, ciudad)) return "Bizkaia";
        if (contains(galicia, ciudad)) return "Galicia";
        if (contains(castillaLe, ciudad)) return "Castilla y Leon";
        if (contains(murcia, ciudad)) return "Murcia";
        if (contains(aragon, ciudad)) return "Aragon";
        if (contains(canarias, ciudad)) return "Canarias";
        return ciudad;
    }

    private boolean contains(String[] arr, String val) {
        for (String s : arr) if (s.equalsIgnoreCase(val)) return true;
        return false;
    }

    private String generarCodigoPostal(String ciudad) {
        int cp = switch (getProvincia(ciudad)) {
            case "Madrid" -> 28000 + random.nextInt(100);
            case "Barcelona" -> 8000 + random.nextInt(100);
            case "Valencia" -> 46000 + random.nextInt(100);
            case "Sevilla" -> 41000 + random.nextInt(100);
            case "Bizkaia" -> 48000 + random.nextInt(100);
            case "Galicia" -> 15000 + random.nextInt(100);
            case "Castilla y Leon" -> 47000 + random.nextInt(100);
            case "Murcia" -> 30000 + random.nextInt(100);
            case "Aragon" -> 50000 + random.nextInt(100);
            case "Canarias" -> 35000 + random.nextInt(100);
            default -> 28000 + random.nextInt(50000);
        };
        return String.valueOf(cp);
    }

    private double generarLatitud(String ciudad) {
        if (ciudad.contains("Canaria") || ciudad.contains("Las Palmas") || ciudad.contains("Tenerife")) {
            return 27.0 + random.nextDouble() * 2;
        }
        return 36.0 + random.nextDouble() * 8;
    }

    private double generarLongitud(String ciudad) {
        if (ciudad.contains("Canaria") || ciudad.contains("Las Palmas") || ciudad.contains("Tenerife")) {
            return -18.0 + random.nextDouble() * 2;
        }
        return -9.0 + random.nextDouble() * 5;
    }
}