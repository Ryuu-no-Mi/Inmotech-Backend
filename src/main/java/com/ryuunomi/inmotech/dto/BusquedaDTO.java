package com.ryuunomi.inmotech.dto;

import java.io.Serializable;

public record BusquedaDTO(
    String texto,
    String ciudad,
    String provincia,
    String tipo,
    String precioMin,
    String precioMax,
    String superficieMin,
    String superficieMax
) implements Serializable {}