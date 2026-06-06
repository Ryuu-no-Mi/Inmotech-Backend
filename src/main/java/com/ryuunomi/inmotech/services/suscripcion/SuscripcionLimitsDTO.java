package com.ryuunomi.inmotech.services.suscripcion;

public record SuscripcionLimitsDTO(
    int propiedadesActuales,
    int limiteMaximo,
    int propiedadesRestantes,
    boolean esPremium,
    String planNombre
) {}
