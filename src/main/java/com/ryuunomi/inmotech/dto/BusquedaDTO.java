package com.ryuunomi.inmotech.dto;

import java.io.Serializable;
import java.util.List;

public record BusquedaDTO(
    String operacion,
    String texto,
    String ciudad,
    String provincia,
    List<String> tipos,
    String precioMin,
    String precioMax,
    String superficieMin,
    String superficieMax,
    String distrito,
    String barrio
) implements Serializable {}