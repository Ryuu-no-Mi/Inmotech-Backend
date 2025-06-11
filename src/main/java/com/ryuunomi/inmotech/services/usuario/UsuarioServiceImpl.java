package com.ryuunomi.inmotech.services.usuario;

import com.ryuunomi.inmotech.dto.UsuarioDTO;
import com.ryuunomi.inmotech.dto.UsuarioRegistroDTO;
import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.ImagenUsuario;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.CapacidadUsuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.mapper.UsuarioMapper;
import com.ryuunomi.inmotech.mapper.UsuarioRegistroMapper;
import com.ryuunomi.inmotech.repositories.AgenciaRepository;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario updateUser(Long id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Si cambia el email, comprobamos duplicados
        if (!usuario.getEmail().equals(usuarioExistente.getEmail())
                && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El nuevo email ya está registrado");
        }

        if (usuario.getEmail() != null) {
            usuarioExistente.setEmail(usuario.getEmail());
        }
        if (usuario.getNombre() != null) {
            usuarioExistente.setNombre(usuario.getNombre());
        }
        if (usuario.getApellido() != null) {
            usuarioExistente.setApellido(usuario.getApellido());
        }
        if (usuario.getTelefono() != null) {
            usuarioExistente.setTelefono(usuario.getTelefono());
        }
        if (usuario.getFechaNacimiento() != null) {
            usuarioExistente.setFechaNacimiento(usuario.getFechaNacimiento());
        }
        if (usuario.getAgencia() != null) {
            usuarioExistente.setAgencia(usuario.getAgencia());
        }

        // Actualizar capacidades
        if (usuario.getCapacidades() != null) {
            Set<CapacidadUsuario> caps = usuario.getCapacidades().stream()
                    .collect(Collectors.toSet());
            usuarioExistente.setCapacidades(caps);
        }

        // Gestión de contraseña solo si viene en la petición
        if (usuario.getContrasenia() != null && !usuario.getContrasenia().isBlank()) {
            String raw = usuario.getContrasenia();
            System.err.println("Raw password recibido: {}" + raw);
            String hashed = passwordEncoder.encode(raw);
            System.err.println("Password codificada: {}" + hashed);
            usuarioExistente.setContrasenia(hashed);
        }

        // Actualizar imagen (si viene en la petición)
        if (usuario.getImagen() != null) {
            ImagenUsuario imagenEntrante = usuario.getImagen();
            ImagenUsuario imagenExistente = usuarioExistente.getImagen();
            if (imagenExistente != null) {
                imagenExistente.setUrl(imagenEntrante.getUrl());
                imagenExistente.setNombreArchivo(imagenEntrante.getNombreArchivo());
            } else {
                imagenEntrante.setUsuario(usuarioExistente);
                usuarioExistente.setImagen(imagenEntrante);
            }
        }

        return usuarioRepository.save(usuarioExistente);
    }

    // verificar que ese email no existe
    @Override
    public Usuario registerNewUser(UsuarioRegistroDTO usuarioRegistroDTO) {
        if (usuarioRepository.existsByEmail(usuarioRegistroDTO.email())){
            throw new IllegalArgumentException("Email ya registrado");
        }

        String rawPassword = usuarioRegistroDTO.password(); // ¡VERIFICA ESTA LÍNEA!
        System.out.println("Contraseña recibida en el servicio: " + rawPassword);
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
           System.err.println("¡Advertencia! La contraseña es nula o vacía antes de codificar.");
        }

        // 3. Codificar la contraseña. Esta es la línea que está fallando.
        String encodedPassword = passwordEncoder.encode(rawPassword); // Esta es la línea 84 que se menciona en el error

        Usuario nuevoUsuario = UsuarioRegistroMapper.fromRegisterDTO(usuarioRegistroDTO);
        nuevoUsuario.setContrasenia(encodedPassword);
        //nuevoUsuario.setFechaRegistro(LocalDate.now());

        Set<CapacidadUsuario> capacidadUsuarios = new HashSet<>();
        capacidadUsuarios.add(CapacidadUsuario.USUARIO);
        nuevoUsuario.setCapacidades(capacidadUsuarios);

        usuarioRepository.save(nuevoUsuario);

        return nuevoUsuario; // Retorna el usuario creado
    }

    // Se crea un usuario  agente o admin
    public Usuario createUserByAdmin(UsuarioRegistroDTO usuarioRegistroDTO) {
        if (usuarioRepository.existsByEmail(usuarioRegistroDTO.email())){
            throw new IllegalArgumentException("Email ya registrado");
        }

        String rawPassword = usuarioRegistroDTO.password();
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña para el usuario creado por admin no puede ser nula o vacía.");
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Usuario nuevoUsuario = UsuarioRegistroMapper.fromRegisterDTO(usuarioRegistroDTO);
        nuevoUsuario.setContrasenia(encodedPassword);
        nuevoUsuario.setFechaRegistro(LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))));

        // Lógica de asignación de roles basada en idAgencia
        Set<CapacidadUsuario> roles = new HashSet<>();

        if (usuarioRegistroDTO.idAgencia() != null) {
            // Si tiene idAgencia, es un AGENTE
            roles.add(CapacidadUsuario.AGENTE);
            Optional<Agencia> agencia = agenciaRepository.findById(usuarioRegistroDTO.idAgencia());
            if (agencia.isEmpty()){
                throw new ResourceNotFoundException("Error al buscar la agencia");
            }
            nuevoUsuario.setAgencia(agencia.get());

        } else {
            // Si no tiene idAgencia, el admin podría estar creando otro ADMIN
            // O podrías tener un campo adicional en el DTO para que el admin especifique el rol.
            // Por simplicidad, asumamos que si no es agente, es un ADMIN si lo crea un admin.
            roles.add(CapacidadUsuario.ADMIN);
        }

        nuevoUsuario.setCapacidades(roles);

        usuarioRepository.save(nuevoUsuario);
        return nuevoUsuario;
    }

    @Override
    public void deleteByEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        /*
         * if (usuario.isPresent()){
         *             Usuario u = usuario.get();
         *             usuarioRepository.delete(u);
         *         }
         */

        // si el optional contiene un usuario, se ejecuta la lamda con ese usuario
        // La parte u -> … significa: “toma el parámetro u (el usuario contenido en el Optional) y ejecuta la expresión a la derecha de la flecha
        /*
        usuario.ifPresent(new Consumer<Usuario>() {
            @Override
            public void accept(Usuario u) {
                usuarioRepository.delete(u);
            }
        });
         */
        usuario.ifPresent(u -> usuarioRepository.delete(u));

    }

    @Override
    public void deleteById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        usuario.ifPresent(u -> usuarioRepository.delete(u));
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
