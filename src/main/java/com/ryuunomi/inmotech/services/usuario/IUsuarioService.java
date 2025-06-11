package com.ryuunomi.inmotech.services.usuario;

import com.ryuunomi.inmotech.dto.UsuarioRegistroDTO;
import com.ryuunomi.inmotech.entities.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUsuarioService {
    List<Usuario> findAll();

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findById(Long id);

    Usuario updateUser(Long id, Usuario usuario);

    Usuario registerNewUser(UsuarioRegistroDTO usuarioRegistroDTO);

    Usuario createUserByAdmin(UsuarioRegistroDTO usuarioRegistroDTO);

    void deleteByEmail(String email);

    void deleteById(Long id);

    boolean existsByEmail(String email);
}

