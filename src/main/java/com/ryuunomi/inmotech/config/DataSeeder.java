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
import java.util.ArrayList;
import java.util.List;
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

    private final String[] ciudades = {"Madrid", "Barcelona", "Valencia", "Malaga", "Sevilla", "Bilbao", "Zaragoza", "Murcia", " Palma de Mallorca", "Las Palmas"};
    private final String[] calles = {"Calle Mayor", "Avenida Gran Via", "Paseo de la Castellana", "Calle Serrano", "Avenida Diagonal", "Calle Alcala", "Paseo de Recoletos", "Calle Preciados", "Avenida America", "Calle Ortega y Gasset"};
    private final String[] tipos = {"PISO", "CASA", "CHALET", "ATICO", "ESTUDIO", "DUPLEX", "PENTHOUSE", "VILLA"};
    private final Random random = new Random(42);

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

        Plan gratuita = new Plan("Gratuito", 2, 4, BigDecimal.ZERO);
        gratuita.setStripePriceId("price_gratis");
        gratuita = planRepository.save(gratuita);

        Plan premium = new Plan("Premium", Integer.MAX_VALUE, Integer.MAX_VALUE, new BigDecimal("9.99"));
        premium.setStripePriceId("price_premium");
        premium = planRepository.save(premium);

        Suscripcion suscGratis = new Suscripcion(TipoSuscripcion.GRATIS);
        suscGratis = suscripcionRepository.save(suscGratis);

        Suscripcion suscPremium = new Suscripcion(TipoSuscripcion.PREMIUM);
        suscPremium = suscripcionRepository.save(suscPremium);

        Agencia inmotech = new Agencia();
        inmotech.setNombre("Inmotech");
        inmotech.setDescripcion("Inmobiliaria lider con mas de 10 anos de experiencia");
        inmotech.setPlan(gratuita);
        inmotech = agenciaRepository.save(inmotech);

        // Usuario admin premium
        Usuario admin = new Usuario();
        admin.setNombre("Admin");
        admin.setApellido("Inmotech");
        admin.setEmail("admin@inmotech.com");
        admin.setContrasenia(passwordEncoder.encode("123456"));
        admin.setTelefono("600123456");
        admin.setFechaNacimiento(LocalDate.of(1990, 1, 15));
        admin.setFechaRegistro(LocalDate.now());
        admin.setAgencia(inmotech);
        admin.setSuscripcion(suscPremium);
        admin.setFechaExpiracionPremium(LocalDate.now().plusDays(30));
        admin.setProvider(AuthProvider.LOCAL);
        admin.setCapacidades(Set.of(CapacidadUsuario.ADMIN, CapacidadUsuario.USUARIO));
        admin = usuarioRepository.save(admin);

        // Usuario juan@test.com - gratuito con propiedades al limite
        Usuario juan = new Usuario();
        juan.setNombre("Juan");
        juan.setApellido("Garcia");
        juan.setEmail("juan@test.com");
        juan.setContrasenia(passwordEncoder.encode("123456"));
        juan.setTelefono("600111222");
        juan.setFechaNacimiento(LocalDate.of(1985, 5, 20));
        juan.setFechaRegistro(LocalDate.now());
        juan.setSuscripcion(suscGratis);
        juan.setProvider(AuthProvider.LOCAL);
        juan.setCapacidades(Set.of(CapacidadUsuario.USUARIO));
        juan = usuarioRepository.save(juan);

        // Crear 28 usuarios más con diferentes estados
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(admin);
        usuarios.add(juan);

        // Estados de suscripcion para variety
        // 10 usuarios PREMIUM activos (fechaFin > hoy + 7)
        // 5 usuarios PREMIUM cancelados (fechaFin > hoy pero cancelado)
        // 5 usuarios PREMIUM expirados (fechaFin < hoy)
        // 5 usuarios gratuita con propiedades limitadas
        // 4 usuarios gratuita sin propiedades

        for (int i = 1; i <= 28; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombre("Usuario" + i);
            usuario.setApellido("Test" + i);
            usuario.setEmail("user" + i + "@test.com");
            usuario.setContrasenia(passwordEncoder.encode("123456"));
            usuario.setTelefono("600" + String.format("%06d", random.nextInt(1000000)));
            usuario.setFechaNacimiento(LocalDate.of(1970 + random.nextInt(30), 1 + random.nextInt(12), 1 + random.nextInt(28)));
            usuario.setFechaRegistro(LocalDate.now().minusDays(random.nextInt(365)));
            usuario.setProvider(AuthProvider.LOCAL);
            usuario.setCapacidades(Set.of(CapacidadUsuario.USUARIO));

            Suscripcion susc = new Suscripcion(TipoSuscripcion.GRATIS);
            susc = suscripcionRepository.save(susc);
            usuario.setSuscripcion(susc);

            // Asignar estado de suscripcion segun tipo
            if (i <= 10) {
                // PREMIUM activo
                usuario.setFechaExpiracionPremium(LocalDate.now().plusDays(7 + random.nextInt(60)));
            } else if (i <= 15) {
                // PREMIUM cancelado - expirara pronto
                usuario.setFechaExpiracionPremium(LocalDate.now().plusDays(3 + random.nextInt(7)));
            } else if (i <= 20) {
                // PREMIUM expirado
                usuario.setFechaExpiracionPremium(LocalDate.now().minusDays(1 + random.nextInt(30)));
            }
            // Los ultimos 9 son gratuitos

            usuario = usuarioRepository.save(usuario);
            usuarios.add(usuario);
        }

        // Crear propiedades para cada usuario
        int propiedadIndex = 0;
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            int numPropiedades;

            if (i == 0) {
                // Admin: 10 propiedades premium
                numPropiedades = 10;
            } else if (i == 1) {
                // Juan: 2 propiedades (limite gratuito)
                numPropiedades = 2;
            } else if (i <= 10) {
                // Usuarios premium activos: 5-15 propiedades
                numPropiedades = 5 + random.nextInt(11);
            } else if (i <= 15) {
                // Usuarios cancelados: 3-8 propiedades
                numPropiedades = 3 + random.nextInt(6);
            } else if (i <= 20) {
                // Usuarios expirados: 5-10 propiedades (algunas pausadas)
                numPropiedades = 5 + random.nextInt(6);
            } else {
                // Usuarios gratuita: 0-3 propiedades
                numPropiedades = random.nextInt(4);
            }

            for (int j = 0; j < numPropiedades; j++) {
                crearPropiedad(usuario, propiedadIndex++, j);
            }
        }

        // Marcar propiedades como pausadas para usuarios con suscripcion expirada
        // Los usuarios expirados (indice 12-17) tienen mas de 2 propiedades
        // Las que sobren deben estar pausadas (eliminada = true)
        for (int i = 12; i <= 17; i++) {
            Usuario usuario = usuarios.get(i);
            List<Propiedad> props = propiedadRepository.findByUsuarioId(usuario.getId());
            if (props.size() > 2) {
                // Las primeras 2 تبقى aktif, el resto pausadas
                for (int j = 2; j < props.size(); j++) {
                    Propiedad p = props.get(j);
                    p.setEliminada(true);
                    p.setFechaEliminacion(LocalDateTime.now().minusDays(random.nextInt(10)));
                    propiedadRepository.save(p);
                }
            }
        }

        System.out.println("=== DATA SEEDER: Datos de prueba creados ===");
        System.out.println("Admin: admin@inmotech.com / 123456 (Premium activo)");
        System.out.println("Usuario gratis: juan@test.com / 123456 (Gratuito, 2 propiedades)");
        System.out.println("Usuarios test: user1@test.com hasta user28@test.com / 123456");
        System.out.println("");
        System.out.println("Estados de suscripcion:");
        System.out.println("- Users 1-10: PREMIUM activo");
        System.out.println("- Users 11-15: PREMIUM cancelado (expira pronto)");
        System.out.println("- Users 16-20: PREMIUM expirado (propiedades pausadas si > 2)");
        System.out.println("- Users 21-28: Gratuito");
        System.out.println("");
        System.out.println("Total: " + usuarios.size() + " usuarios, " + propiedadRepository.count() + " propiedades");
    }

    private void crearPropiedad(Usuario usuario, int index, int offset) {
        Propiedad p = new Propiedad();
        p.setTitulo(tipos[random.nextInt(tipos.length)] + " en " + ciudades[random.nextInt(ciudades.length)] + " #" + index);
        p.setDescripcion("Hermosa propiedad con excelentes vistas y acabados de primera calidad. Ideal para familias.");
        p.setPrecio(BigDecimal.valueOf(80000 + random.nextInt(420000)));
        p.setSuperficie(BigDecimal.valueOf(40 + random.nextInt(200)));
        p.setDireccion(calles[random.nextInt(calles.length)] + " " + (10 + random.nextInt(200)));
        p.setCiudad(ciudades[random.nextInt(ciudades.length)]);
        p.setProvincia(p.getCiudad());
        p.setCodigoPostal(String.valueOf(28000 + random.nextInt(1000)));
        p.setLatitud(40.0 + random.nextDouble() * 2);
        p.setLongitud(-4.0 + random.nextDouble() * 3);
        p.setFechaPublicacion(LocalDateTime.now().minusDays(random.nextInt(180)));
        p.setUsuario(usuario);
        p.setEliminada(false);
        propiedadRepository.save(p);
    }
}