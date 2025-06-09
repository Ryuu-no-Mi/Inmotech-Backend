package com.ryuunomi.inmotech.dto;

public record ConsultaDTO(
        Long id,
        String mensaje,
        String fecha,
        Long idUsuario,
        Long idPropiedad
) {}
