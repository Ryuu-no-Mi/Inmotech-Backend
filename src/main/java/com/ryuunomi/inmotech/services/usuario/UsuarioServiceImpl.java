package com.ryuunomi.inmotech.services.usuario;

import com.ryuunomi.inmotech.entities.ImagenUsuario;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

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

        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setFechaNacimiento(usuario.getFechaNacimiento());
        usuarioExistente.setAgencia(usuario.getAgencia());
        usuarioExistente.setCapacidades(usuario.getCapacidades());

        // Gestión de contraseña
        if (!passwordEncoder.matches(usuario.getContrasenia(), usuarioExistente.getContrasenia())) {
            usuarioExistente.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        }

        // 6) Actualizar imagen (si viene en la petición)
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
    public Usuario createUser(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new IllegalArgumentException("Email ya registrado");
        }
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));

        if (usuario.getImagen() !=null ){
            ImagenUsuario imagenUsuario = usuario.getImagen();
            imagenUsuario.setUsuario(usuario);
            usuario.setImagen(imagenUsuario);
        }

        return usuarioRepository.save(usuario);
    }

    /*
    @Override
    public Usuario save(Usuario usuario) {
        //busco el usuario en la BDD
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuario.getId());

        if (usuarioOptional.isEmpty()) {
            // Nuevo usuario
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new IllegalArgumentException("Email ya registrado");
            }
            usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        } else {
            Usuario usuarioExistente = usuarioOptional.get();

            // Verificar si cambió el email
            if (!usuario.getEmail().equals(usuarioExistente.getEmail()) &&
                    usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new IllegalArgumentException("El nuevo email ya está registrado");
            }

            // Verificar si cambió la contraseña
            if (!passwordEncoder.matches(usuario.getContrasenia(), usuarioExistente.getContrasenia())) {
                usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
            } else {
                usuario.setContrasenia(usuarioExistente.getContrasenia());
            }
        }

        return usuarioRepository.save(usuario);
    }
    */

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
