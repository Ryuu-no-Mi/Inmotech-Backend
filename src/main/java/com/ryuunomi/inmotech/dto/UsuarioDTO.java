package com.ryuunomi.inmotech.dto;

public record UsuarioDTO(
        Long id,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String fechaNacimiento,
        String fechaRegistro,
        Long idAgencia,
        String imagenUrl
) {}
