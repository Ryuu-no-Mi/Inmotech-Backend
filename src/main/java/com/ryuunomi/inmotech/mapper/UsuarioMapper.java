package com.ryuunomi.inmotech.mapper;

import com.ryuunomi.inmotech.dto.UsuarioDTO;
import com.ryuunomi.inmotech.dto.UsuarioRegistroDTO;
import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.ImagenUsuario;
import com.ryuunomi.inmotech.entities.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getTelefono(),
                u.getFechaNacimiento() != null ? u.getFechaNacimiento().toString() : null,
                u.getFechaRegistro() != null ? u.getFechaRegistro().toString() : null,
                u.getAgencia() != null ? u.getAgencia().getId() : null,
                u.getImagen() != null ? u.getImagen().getUrl() : null

        );
    }

    // necesito convertir DTO → Entidad:

    public static Usuario fromDTO(UsuarioDTO usuarioDTO) {
        Usuario u = new Usuario();
        u.setId(usuarioDTO.id());
        u.setNombre(usuarioDTO.nombre());
        u.setApellido(usuarioDTO.apellido());
        u.setEmail(usuarioDTO.email());
        u.setTelefono(usuarioDTO.telefono());

        if (usuarioDTO.imagenUrl() != null) {
            ImagenUsuario img = new ImagenUsuario();
            img.setUrl(usuarioDTO.imagenUrl());
            u.setImagen(img);
        }

        if (usuarioDTO.idAgencia() != null) {
            Agencia a = new Agencia();
            a.setId(usuarioDTO.idAgencia());
            u.setAgencia(a);
        }

        return u;
    }
}
