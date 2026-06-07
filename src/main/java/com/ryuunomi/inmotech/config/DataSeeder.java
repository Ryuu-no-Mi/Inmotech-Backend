package com.ryuunomi.inmotech.config;

import com.ryuunomi.inmotech.entities.*;
import com.ryuunomi.inmotech.enums.*;
import com.ryuunomi.inmotech.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AgenciaRepository agenciaRepository;
    private final PlanRepository planRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final PropiedadRepository propiedadRepository;
    private final PasswordEncoder passwordEncoder;

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
        if (usuarioRepository.count() > 0) return;

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
        admin.setProvider(AuthProvider.LOCAL);
        admin.setCapacidades(Set.of(CapacidadUsuario.ADMIN, CapacidadUsuario.USUARIO));
        admin = usuarioRepository.save(admin);

        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Juan");
        usuario1.setApellido("Garcia");
        usuario1.setEmail("juan@test.com");
        usuario1.setContrasenia(passwordEncoder.encode("123456"));
        usuario1.setTelefono("600111222");
        usuario1.setFechaNacimiento(LocalDate.of(1985, 5, 20));
        usuario1.setFechaRegistro(LocalDate.now());
        usuario1.setSuscripcion(suscGratis);
        usuario1.setProvider(AuthProvider.LOCAL);
        usuario1.setCapacidades(Set.of(CapacidadUsuario.USUARIO));
        usuario1 = usuarioRepository.save(usuario1);

        crearPropiedad(admin, inmotech, "Piso centro ciudad", "Bonito piso en el centro con 3 habitaciones", new BigDecimal("185000"), new BigDecimal("85"), "Calle Mayor 15", "Madrid", "Madrid", "28001", 40.4168, -3.7038);
        crearPropiedad(admin, inmotech, "Chalet pareado", "Chalet pareado con jardin y piscina privada", new BigDecimal("450000"), new BigDecimal("200"), "Avenida de los Pinos 8", "Barcelona", "Barcelona", "08001", 41.3851, 2.1734);
        crearPropiedad(admin, inmotech, "Atico duplex", "Atico duplex con terraza panoramic views", new BigDecimal("320000"), new BigDecimal("120"), "Paseo de la Castellana 50", "Madrid", "Madrid", "28001", 40.4168, -3.7038);
        crearPropiedad(usuario1, null, "Estudio en el centro", "Estudio ideal para estudiantes", new BigDecimal("95000"), new BigDecimal("40"), "Calle Gran Via 30", "Valencia", "Valencia", "46001", 39.4699, -0.3763);
        crearPropiedad(usuario1, null, "Apartamento playera", "Apartamento a 100m de la playa", new BigDecimal("175000"), new BigDecimal("65"), "Avenida del Mar 5", "Malaga", "Malaga", "29001", 36.7213, -4.4214);

        System.out.println("=== DATA SEEDER: Datos de prueba creados ===");
        System.out.println("Usuario admin: admin@inmotech.com / 123456");
        System.out.println("Usuario test: juan@test.com / 123456");
    }

    private void crearPropiedad(Usuario usuario, Agencia agencia, String titulo, String desc, BigDecimal precio, BigDecimal superficie, String direccion, String ciudad, String provincia, String cp, double lat, double lon) {
        Propiedad p = new Propiedad();
        p.setTitulo(titulo);
        p.setDescripcion(desc);
        p.setPrecio(precio);
        p.setSuperficie(superficie);
        p.setDireccion(direccion);
        p.setCiudad(ciudad);
        p.setProvincia(provincia);
        p.setCodigoPostal(cp);
        p.setLatitud(lat);
        p.setLongitud(lon);
        p.setFechaPublicacion(java.time.LocalDateTime.now());
        p.setUsuario(usuario);
        p.setAgencia(agencia);
        propiedadRepository.save(p);
    }
}