package com.ryuunomi.inmotech.dto;

import java.math.BigDecimal;

public record PropiedadDTO(
        Long id,
        String titulo,
        String descripcion,
        BigDecimal precio,
        BigDecimal superficie,
        String direccion,
        String ciudad,
        String provincia,
        String codigoPostal,
        Double latitud,
        Double longitud,
        String fechaPublicacion,
        Long idUsuario,
        Long idAgencia,
        String imagenPortadaUrl
) {}
