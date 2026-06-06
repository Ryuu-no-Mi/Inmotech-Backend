package com.ryuunomi.inmotech.dto;

import com.ryuunomi.inmotech.entities.ImagenPropiedad;

import java.math.BigDecimal;
import java.util.List;

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
        List<ImagenPropiedadDTO> imagenes

) {}
