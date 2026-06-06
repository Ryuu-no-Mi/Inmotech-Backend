package com.ryuunomi.inmotech.mapper;

import com.ryuunomi.inmotech.dto.UsuarioDTO;
import com.ryuunomi.inmotech.dto.UsuarioRegistroDTO;
import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.ImagenUsuario;
import com.ryuunomi.inmotech.entities.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UsuarioRegistroMapper {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // ahora con usuarioRegistroDTO ->

        // necesito convertir DTO → Entidad:

    public static Usuario fromRegisterDTO(UsuarioRegistroDTO usuarioRegistroDTO) {
        Usuario u = new Usuario();

        u.setNombre(usuarioRegistroDTO.nombre());
        u.setApellido(usuarioRegistroDTO.apellido());
        u.setEmail(usuarioRegistroDTO.email());
        u.setTelefono(usuarioRegistroDTO.telefono());

        //fecha de naciemiento
        if (usuarioRegistroDTO.fechaNacimiento() != null && !usuarioRegistroDTO.fechaNacimiento().isBlank()) {
            try {
                u.setFechaNacimiento(LocalDate.parse(usuarioRegistroDTO.fechaNacimiento(), DATE_FORMATTER));
            } catch (DateTimeParseException e) {
                System.err.println("Error al parsear fecha de nacimiento: " + usuarioRegistroDTO.fechaNacimiento());
                throw new IllegalArgumentException("Formato de fecha de nacimiento inválido. Esperado dd/MM/yyyy. Recibido: " + usuarioRegistroDTO.fechaNacimiento(), e);
            }
        }

        if (usuarioRegistroDTO.imagenUrl() != null &&
                !usuarioRegistroDTO.imagenUrl().isBlank() &&
                !usuarioRegistroDTO.imagenUrl().equalsIgnoreCase("null")) {

            ImagenUsuario img = new ImagenUsuario();
            img.setUrl(usuarioRegistroDTO.imagenUrl());
            img.setUsuario(u);
            u.setImagen(img);
        }


        if (usuarioRegistroDTO.idAgencia() != null) {
            Agencia agencia = new Agencia();
            agencia.setId(usuarioRegistroDTO.idAgencia());
            u.setAgencia(agencia);
        }
        System.err.println("Password introducida: " + usuarioRegistroDTO.password());
        u.setContrasenia(usuarioRegistroDTO.password());

        return u;
    }
}
