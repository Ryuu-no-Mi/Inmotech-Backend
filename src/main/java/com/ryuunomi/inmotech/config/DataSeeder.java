package com.ryuunomi.inmotech.config;

import com.ryuunomi.inmotech.config.MapaGeograficoEstatico.ComunidadAutonoma;
import com.ryuunomi.inmotech.config.MapaGeograficoEstatico.Municipio;
import com.ryuunomi.inmotech.config.MapaGeograficoEstatico.Provincia;
import com.ryuunomi.inmotech.dto.CityGeoData;
import com.ryuunomi.inmotech.entities.*;
import com.ryuunomi.inmotech.enums.*;
import com.ryuunomi.inmotech.repositories.*;
import com.ryuunomi.inmotech.services.geocoding.GeocodingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AgenciaRepository agenciaRepository;
    private final PlanRepository planRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final PropiedadRepository propiedadRepository;
    private final GeocodingService geocodingService;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random(42);

    private static final String[] CALLES = {
        "Calle Mayor", "Avenida Gran Via", "Paseo de la Castellana", "Calle Serrano",
        "Avenida Diagonal", "Calle Alcala", "Paseo de Recoletos", "Calle Preciados",
        "Avenida America", "Calle Ortega y Gasset", "Calle del Prado", "Paseo de la Vera",
        "Calle del Mar", "Avenida de la Playa", "Calle del Puerto", "Avenida del Puerto",
        "Calle Real", "Paseo de la Alameda", "Calle Nueva", "Avenida de la Constitucion",
        "Calle Fuencarral", "Calle Gran Via", "Calle Preciados", "Calle Arenal",
        "Calle del Carmen", "Calle del Barco", "Calle Desengaño", "Calle Montera",
        "Avenida Reyes Catolicos", "Avenida Dr. Maranon", "Avenida de la Universidad",
        "Calle Larios", "Calle Nueva", "Calle de la Marina", "Avenida de Cervantes",
        "Calle de la Moneda", "Plaza de la Constitucion", "Calle de los Herreros",
        "Calle del Almirante", "Calle de la Paz", "Calle de San Juan",
        "Calle del Pilar", "Avenida de la Palmera", "Calle del Sol", "Avenida del Mar",
        "Calle Ronda", "Paseo Maritimo", "Calle del Rio", "Avenida del Parador"
    };

    private static final String[] TIPOS = {"PISO", "CASA", "CHALET", "ATICO", "ESTUDIO", "DUPLEX", "PENTHOUSE", "VILLA"};

    private static final EstadoPropiedad[] ESTADOS = EstadoPropiedad.values();

    private static final CertificacionEnergetica[] CERTIFICACIONES = CertificacionEnergetica.values();

    private static final String[] AGENCIAS_PREMIUM = {
        "Inmotech Madrid Centro", "Inmotech Barcelona Costa", "Inmotech Valencia Mediterranea",
        "Inmotech Andalucia Sur", "Inmotech Pais Vasco Norte", "Inmotech Galicia Atlantico",
        "Inmotech Castilla Leon", "Inmotech Murcia Sureste", "Inmotech Aragon Este", "Inmotech Canarias Global"
    };

    private static final String[] AGENCIAS_BASIC = {
        "Inmotech Leon Minor", "Inmotech Cuenca Este", "Inmotech Jaen Sur",
        "Inmotech Tarragona Costa", "Inmotech Albacete Norte"
    };

    private List<String> listaMunicipios;

    public DataSeeder(UsuarioRepository usuarioRepository, AgenciaRepository agenciaRepository,
                      PlanRepository planRepository, SuscripcionRepository suscripcionRepository,
                      PropiedadRepository propiedadRepository, GeocodingService geocodingService,
                      PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.agenciaRepository = agenciaRepository;
        this.planRepository = planRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.propiedadRepository = propiedadRepository;
        this.geocodingService = geocodingService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            System.out.println("=== DATA SEEDER: Datos ya existentes, omitiendo ===");
            return;
        }

        listaMunicipios = MapaGeograficoEstatico.getAllMunicipios();
        System.out.println("=== DATA SEEDER: Mapa geografico cargado con " + listaMunicipios.size() + " municipios ===");

        System.out.println("=== DATA SEEDER: Creando datos demo ===");

        Plan gratuitoPlan = planRepository.save(new Plan("Gratuito", 2, 4, 1, BigDecimal.ZERO));
        Plan premiumPlan = planRepository.save(new Plan("Premium", Integer.MAX_VALUE, Integer.MAX_VALUE, 1, new BigDecimal("9.99")));
        Plan agenciaBasicPlan = planRepository.save(new Plan("Agencia Basic", 20, 50, 5, new BigDecimal("29.99")));
        Plan agenciaPremiumPlan = planRepository.save(new Plan("Agencia Premium", Integer.MAX_VALUE, Integer.MAX_VALUE, 10, new BigDecimal("49.99")));

        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setApellido("Inmotech");
        admin.setEmail("admin@inmotech.com");
        admin.setContrasenia(passwordEncoder.encode("123456"));
        admin.setTelefono("600123456");
        admin.setFechaNacimiento(LocalDate.of(1990, 1, 15));
        admin.setFechaRegistro(LocalDate.now());
        admin.setSuscripcion(suscripcionRepository.save(new Suscripcion(TipoSuscripcion.PREMIUM)));
        admin.setFechaExpiracionPremium(LocalDate.now().plusDays(30));
        admin.setProvider(AuthProvider.LOCAL);
        admin.setCapacidades(Set.of(CapacidadUsuario.ADMIN, CapacidadUsuario.USUARIO));
        admin = usuarioRepository.save(admin);

        long startTime = System.currentTimeMillis();

        int propsUsuariosNormales = crearUsuariosNormales(premiumPlan);

        int totalAgencias = 15;
        int agentesPorAgenciaPremium = 5;
        int agentesPorAgenciaBasic = 3;
        int propsPorAgente = 4;

        int totalPropsAgencias = (10 * agentesPorAgenciaPremium + 5 * agentesPorAgenciaBasic) * propsPorAgente;

        Set<Integer> agenciasInactivas = new HashSet<>();
        while (agenciasInactivas.size() < 3) {
            agenciasInactivas.add(random.nextInt(15));
        }

        List<Usuario> todosLosAgentes = new ArrayList<>();
        List<Usuario> agentesInactivos = new ArrayList<>();

        for (int a = 0; a < AGENCIAS_PREMIUM.length; a++) {
            LocalDate expiryPlan = LocalDate.now().plusDays(60 + random.nextInt(180));
            boolean esAgenciaInactiva = agenciasInactivas.contains(a);
            if (esAgenciaInactiva) {
                expiryPlan = LocalDate.now().minusDays(random.nextInt(5) + 1);
            }

            Agencia agencia = new Agencia();
            agencia.setNombre(AGENCIAS_PREMIUM[a]);
            agencia.setDescripcion("Agencia inmobiliaria " + AGENCIAS_PREMIUM[a] + " con mas de 10 anos de experiencia");
            agencia.setPlan(agenciaPremiumPlan);
            agencia.setFechaExpiracionPlan(expiryPlan);
            agencia = agenciaRepository.save(agencia);

            for (int ag = 0; ag < agentesPorAgenciaPremium; ag++) {
                LocalDate expiryAgente = esAgenciaInactiva
                    ? LocalDate.now().minusDays(random.nextInt(3) + 1)
                    : LocalDate.now().plusDays(30 + random.nextInt(365));

                Suscripcion susc = new Suscripcion(TipoSuscripcion.PREMIUM);
                susc.setFechaFin(esAgenciaInactiva ? LocalDate.now().minusDays(1) : expiryAgente);
                susc = suscripcionRepository.save(susc);

                Usuario agente = new Usuario();
                agente.setNombre("AgenteP" + (a + 1) + "_" + (ag + 1));
                agente.setApellido("Inmotech");
                agente.setEmail("agente_p" + (a + 1) + "_" + (ag + 1) + "@inmotech.com");
                agente.setContrasenia(passwordEncoder.encode("123456"));
                agente.setTelefono("600" + String.format("%06d", 100000 + a * 1000 + ag * 100));
                agente.setFechaNacimiento(LocalDate.of(1980 + random.nextInt(15), 1 + random.nextInt(12), 1 + random.nextInt(28)));
                agente.setFechaRegistro(LocalDate.now().minusDays(random.nextInt(365)));
                agente.setAgencia(agencia);
                agente.setSuscripcion(susc);
                agente.setFechaExpiracionPremium(expiryAgente);
                agente.setProvider(AuthProvider.LOCAL);
                agente.setCapacidades(Set.of(CapacidadUsuario.AGENTE, CapacidadUsuario.USUARIO));
                agente = usuarioRepository.save(agente);

                todosLosAgentes.add(agente);
                if (esAgenciaInactiva || expiryAgente.isBefore(LocalDate.now())) {
                    agentesInactivos.add(agente);
                }
            }
        }

        for (int a = 0; a < AGENCIAS_BASIC.length; a++) {
            LocalDate expiryPlan = LocalDate.now().plusDays(60 + random.nextInt(180));
            boolean esAgenciaInactiva = agenciasInactivas.contains(10 + a);
            if (esAgenciaInactiva) {
                expiryPlan = LocalDate.now().minusDays(random.nextInt(5) + 1);
            }

            Agencia agencia = new Agencia();
            agencia.setNombre(AGENCIAS_BASIC[a]);
            agencia.setDescripcion("Agencia inmobiliaria " + AGENCIAS_BASIC[a] + " con mas de 5 anos de experiencia");
            agencia.setPlan(agenciaBasicPlan);
            agencia.setFechaExpiracionPlan(expiryPlan);
            agencia = agenciaRepository.save(agencia);

            for (int ag = 0; ag < agentesPorAgenciaBasic; ag++) {
                LocalDate expiryAgente = esAgenciaInactiva
                    ? LocalDate.now().minusDays(random.nextInt(3) + 1)
                    : LocalDate.now().plusDays(30 + random.nextInt(365));

                Suscripcion susc = new Suscripcion(TipoSuscripcion.PREMIUM);
                susc.setFechaFin(esAgenciaInactiva ? LocalDate.now().minusDays(1) : expiryAgente);
                susc = suscripcionRepository.save(susc);

                Usuario agente = new Usuario();
                agente.setNombre("AgenteB" + (a + 1) + "_" + (ag + 1));
                agente.setApellido("Inmotech");
                agente.setEmail("agente_b" + (a + 1) + "_" + (ag + 1) + "@inmotech.com");
                agente.setContrasenia(passwordEncoder.encode("123456"));
                agente.setTelefono("600" + String.format("%06d", 900000 + a * 1000 + ag * 100));
                agente.setFechaNacimiento(LocalDate.of(1980 + random.nextInt(15), 1 + random.nextInt(12), 1 + random.nextInt(28)));
                agente.setFechaRegistro(LocalDate.now().minusDays(random.nextInt(365)));
                agente.setAgencia(agencia);
                agente.setSuscripcion(susc);
                agente.setFechaExpiracionPremium(expiryAgente);
                agente.setProvider(AuthProvider.LOCAL);
                agente.setCapacidades(Set.of(CapacidadUsuario.AGENTE, CapacidadUsuario.USUARIO));
                agente = usuarioRepository.save(agente);

                todosLosAgentes.add(agente);
                if (esAgenciaInactiva || expiryAgente.isBefore(LocalDate.now())) {
                    agentesInactivos.add(agente);
                }
            }
        }

        int propsCreadas = crearPropiedadesBatch(todosLosAgentes, propsPorAgente, propsPorAgente);
        int propsInactivas = crearPropiedadesBatch(agentesInactivos, propsPorAgente / 2, 5);

        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("");
        System.out.println("=== DATA SEEDER: Datos de prueba creados ===");
        System.out.println("Admin: admin@inmotech.com / 123456");
        System.out.println("Usuarios normales:");
        System.out.println("  gratuito@inmotech.com / 123456 (Gratuito, ~10 props)");
        System.out.println("  premium@inmotech.com / 123456 (Premium, ~8 props, activas)");
        System.out.println("  expira-hoy@inmotech.com / 123456 (Premium, ~6 props, inactivas)");
        System.out.println("  expirado-ayer@inmotech.com / 123456 (Premium, ~6 props, inactivas)");
        System.out.println("15 Agencias (10 Premium, 5 Basic)");
        System.out.println("  Agentes Premium: " + (10 * agentesPorAgenciaPremium) + " | Agentes Basic: " + (5 * agentesPorAgenciaBasic));
        System.out.println("  Agencias con props inactivas: " + agenciasInactivas.size());
        System.out.println("  Agentes con suscripcion inactiva: " + agentesInactivos.size());
        System.out.println("Total propiedades: " + propsUsuariosNormales + " usuarios + " + propsCreadas + " activas + " + propsInactivas + " inactivas = " + (propsUsuariosNormales + propsCreadas + propsInactivas));
        System.out.println("Tiempo total: " + totalTime + "ms (" + (totalTime / 1000) + "s)");
    }

    private int crearUsuariosNormales(Plan premiumPlan) {
        int[] propsCounts = {10, 8, 6, 6};
        String[][] usuariosData = {
            {"Gratuito", "gratuito", "GRATIS", "0"},
            {"Premium", "premium", "PREMIUM", String.valueOf(random.nextInt(365) + 30)},
            {"ExpiraHoy", "expira-hoy", "PREMIUM", "0"},
            {"ExpiradoAyer", "expirado-ayer", "PREMIUM", String.valueOf(-random.nextInt(2) - 1)}
        };

        int totalPropiedades = 0;
        for (int i = 0; i < usuariosData.length; i++) {
            String[] data = usuariosData[i];
            int propCount = propsCounts[i];
            LocalDate expiry;
            if (data[0].equals("ExpiraHoy")) {
                expiry = LocalDate.now();
            } else if (data[0].equals("ExpiradoAyer")) {
                expiry = LocalDate.now().minusDays(1);
            } else if (data[0].equals("Gratuito")) {
                expiry = LocalDate.now().plusMonths(1);
            } else {
                expiry = LocalDate.now().plusDays(Integer.parseInt(data[3]));
            }

            Suscripcion susc = new Suscripcion(TipoSuscripcion.valueOf(data[2]));
            susc.setFechaFin(expiry.isAfter(LocalDate.now()) ? expiry : LocalDate.now().minusDays(1));
            susc = suscripcionRepository.save(susc);

            Usuario usuario = new Usuario();
            usuario.setNombre("Usuario");
            usuario.setApellido(data[0]);
            usuario.setEmail(data[1] + "@inmotech.com");
            usuario.setContrasenia(passwordEncoder.encode("123456"));
            usuario.setTelefono("600" + String.format("%06d", 111111 + i * 11111));
            usuario.setFechaNacimiento(LocalDate.of(1985 + random.nextInt(15), 1 + random.nextInt(12), 1 + random.nextInt(28)));
            usuario.setFechaRegistro(LocalDate.now().minusMonths(random.nextInt(12) + 1));
            usuario.setSuscripcion(susc);
            usuario.setFechaExpiracionPremium(expiry);
            usuario.setProvider(AuthProvider.LOCAL);
            usuario.setCapacidades(Set.of(CapacidadUsuario.USUARIO));
            usuario = usuarioRepository.save(usuario);

            totalPropiedades += crearPropiedadesBatch(List.of(usuario), propCount, propCount);
        }
        return totalPropiedades;
    }

    private int crearPropiedadesBatch(List<Usuario> usuarios, int propsPorUsuario, int batchSize) {
        List<Propiedad> batch = new ArrayList<>(batchSize);
        int total = 0;

        for (Usuario usuario : usuarios) {
            for (int i = 0; i < propsPorUsuario; i++) {
                batch.add(crearPropiedad(usuario, i));
                if (batch.size() >= batchSize) {
                    propiedadRepository.saveAll(batch);
                    total += batch.size();
                    batch.clear();
                    if (total % 1000 == 0) {
                        System.out.printf("  %d propiedades guardadas (%d usuarios)%n", total, usuarios.size());
                    }
                }
            }
        }
        if (!batch.isEmpty()) {
            propiedadRepository.saveAll(batch);
            total += batch.size();
        }
        return total;
    }

    private Propiedad crearPropiedad(Usuario usuario, int index) {
        String ciudad = listaMunicipios.get(random.nextInt(listaMunicipios.size()));
        String tipo = TIPOS[random.nextInt(TIPOS.length)];
        boolean esAlquiler = random.nextDouble() < 0.3;

        BigDecimal precio = esAlquiler
            ? BigDecimal.valueOf(400 + random.nextInt(1900)).multiply(BigDecimal.valueOf(50))
            : BigDecimal.valueOf(80000 + random.nextInt(420000));

        CityGeoData geo = geocodingService.getGeoData(ciudad);
        String provincia = geocodingService.getProvincia(ciudad);
        String comunidad = geocodingService.getComunidad(ciudad);
        String distrito = geocodingService.getDistritoAleatorio(ciudad);
        String barrio = geocodingService.getBarrioAleatorio(ciudad, distrito);
        String calle = CALLES[random.nextInt(CALLES.length)] + " " + (1 + random.nextInt(200));

        double latitudBase = geo.latitud();
        double longitudBase = geo.longitud();
        double latitud = latitudBase + (random.nextDouble() - 0.5) * 0.1;
        double longitud = longitudBase + (random.nextDouble() - 0.5) * 0.1;

        String descripcion = generarDescripcion(tipo, ciudad, barrio, calle, geo, esAlquiler);

        Propiedad p = new Propiedad();
        p.setTitulo(tipo + " en " + barrio + ", " + ciudad);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setSuperficie(BigDecimal.valueOf(40 + random.nextInt(350)));
        p.setDireccion(calle);
        p.setCiudad(ciudad);
        p.setProvincia(provincia != null ? provincia : ciudad);
        p.setCodigoPostal(generarCodigoPostal(provincia, comunidad));
        p.setLatitud(latitud);
        p.setLongitud(longitud);
        p.setFechaPublicacion(LocalDateTime.now().minusDays(random.nextInt(180)));
        p.setUsuario(usuario);
        p.setTipo(tipo);
        p.setOperacion(esAlquiler ? "ALQUILER" : "VENTA");
        p.setDistrito(distrito);
        p.setBarrio(barrio);
        p.setEliminada(false);
        p.setHabitaciones(1 + random.nextInt(6));
        p.setBanos(1 + random.nextInt(4));
        p.setParking(random.nextBoolean());
        p.setAnoConstruccion(1970 + random.nextInt(56));
        p.setEstado(ESTADOS[random.nextInt(ESTADOS.length)]);
        p.setCertificacionEnergetica(CERTIFICACIONES[random.nextInt(CERTIFICACIONES.length)]);
        return p;
    }

    private String generarDescripcion(String tipo, String ciudad, String barrio, String calle, CityGeoData geo, boolean esAlquiler) {
        String[] extras = {
            " Propiedad con excelentes acabados, ubicacion privilegiada y vistas magnificas.",
            " Inmueble en perfecto estado de conservacion, listo para entrar a vivir.",
            " Excelente oportunidad tanto para vivienda habitual como para inversion.",
            " Destaca por su luminosidad natural y distribucion funcional de los espacios.",
            " Situada en una de las zonas mas demandadas de la ciudad, con todos los servicios cercanos."
        };

        String uso = esAlquiler
            ? "alquiler a largo plazo o estudiantes"
            : "familias o inversores que buscan una propiedad de calidad";

        String tipoDesc = switch (tipo) {
            case "PISO", "ESTUDIO" -> "dispone de salon-cocina, dormitorios amplios y banos completos";
            case "CASA", "CHALET", "VILLA" -> "ofrece jardin privado, amplias habitaciones y maxima privacidad";
            case "ATICO", "PENTHOUSE" -> "cuenta con terraza panoramica, vistas 360 grados y espacios exteriores";
            case "DUPLEX" -> "presenta distribucion en dos plantas, maxima luminosidad y diseño moderno";
            default -> "se encuentra en excelentes condiciones, lista para ocupar";
        };

        return tipo + " en el barrio de " + (barrio != null ? barrio : "la ciudad") + ", " + ciudad +
               ". Ubicada en " + calle + ". Esta propiedad tipo " + tipo + " " + tipoDesc +
               ". Ideal para " + uso + "." + extras[random.nextInt(extras.length)];
    }

    private String generarCodigoPostal(String provincia, String comunidad) {
        if (provincia == null) provincia = "";
        if (comunidad == null) comunidad = "";

        String provNorm = provincia.toLowerCase().replace("á", "a").replace("é", "e")
            .replace("í", "i").replace("ó", "o").replace("ú", "u");

        return switch (provNorm) {
            case "madrid" -> "28" + String.format("%03d", random.nextInt(100));
            case "barcelona" -> "08" + String.format("%03d", random.nextInt(100));
            case "valencia", "alicante", "castellón" -> "46" + String.format("%03d", random.nextInt(100));
            case "sevilla", "cádiz", "huelva", "córdoba", "jaén", "granada", "almería", "málaga" -> "41" + String.format("%03d", random.nextInt(100));
            case "bizkaia", "gipuzkoa", "araba" -> "48" + String.format("%03d", random.nextInt(100));
            case "a coruña", "lugo", "ourense", "pontevedra" -> "15" + String.format("%03d", random.nextInt(100));
            case "asturias" -> "33" + String.format("%03d", random.nextInt(100));
            case "cantabria" -> "39" + String.format("%03d", random.nextInt(100));
            case "burgos", "león", "palencia", "salamanca", "segovia", "soria", "valladolid", "zamora", "ávila" -> "09" + String.format("%03d", random.nextInt(100));
            case "toledo", "cuenca", "guadalajara", "albacete", "ciudad real" -> "16" + String.format("%03d", random.nextInt(100));
            case "badajoz", "cáceres" -> "06" + String.format("%03d", random.nextInt(100));
            case "murcia" -> "30" + String.format("%03d", random.nextInt(100));
            case "zaragoza", "huesca" -> "50" + String.format("%03d", random.nextInt(100));
            case "tarragona", "girona", "lleida" -> "25" + String.format("%03d", random.nextInt(100));
            case "las palmas", "santa cruz de tenerife" -> "35" + String.format("%03d", random.nextInt(100));
            case "illes balears" -> "07" + String.format("%03d", random.nextInt(100));
            case "navarra" -> "31" + String.format("%03d", random.nextInt(100));
            case "la rioja" -> "26" + String.format("%03d", random.nextInt(100));
            case "hueiva" -> "21" + String.format("%03d", random.nextInt(100));
            case "teruel" -> "44" + String.format("%03d", random.nextInt(100));
            default -> "28" + String.format("%03d", random.nextInt(100));
        };
    }
}
