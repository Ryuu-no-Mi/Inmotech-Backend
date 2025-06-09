package com.ryuunomi.inmotech.dto;

public record AgenciaDTO(
        Long id,
        String nombre,
        String descripcion,
        Long idUsuarioAdmin
) {}
