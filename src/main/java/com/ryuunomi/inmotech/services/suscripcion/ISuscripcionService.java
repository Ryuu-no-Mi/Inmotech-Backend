package com.ryuunomi.inmotech.services.suscripcion;

import com.ryuunomi.inmotech.entities.Usuario;

public interface ISuscripcionService {
    boolean puedePublicar(Usuario usuario);
    int propiedadesRestantes(Usuario usuario);
    SuscripcionLimitsDTO obtenerLimites(Usuario usuario);
    void activarPremium(Long usuarioId, String stripeSubscriptionId);
    void desactivarPremium(Long usuarioId);
}
