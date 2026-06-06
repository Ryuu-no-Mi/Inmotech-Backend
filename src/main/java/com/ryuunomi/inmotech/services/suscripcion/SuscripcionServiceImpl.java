package com.ryuunomi.inmotech.services.suscripcion;

import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.Plan;
import com.ryuunomi.inmotech.entities.Suscripcion;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.TipoSuscripcion;
import com.ryuunomi.inmotech.repositories.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuscripcionServiceImpl implements ISuscripcionService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Override
    public boolean puedePublicar(Usuario usuario) {
        return propiedadesRestantes(usuario) > 0;
    }

    @Override
    public int propiedadesRestantes(Usuario usuario) {
        SuscripcionLimitsDTO limites = obtenerLimites(usuario);
        return limites.propiedadesRestantes();
    }

    @Override
    public SuscripcionLimitsDTO obtenerLimites(Usuario usuario) {
        if (usuario.getAgencia() != null) {
            Agencia agencia = usuario.getAgencia();
            Plan plan = agencia.getPlan();

            if (plan != null && plan.getMaxPropiedadesAgencia() == Integer.MAX_VALUE) {
                long actuales = propiedadRepository.countByAgenciaId(agencia.getId());
                return new SuscripcionLimitsDTO(
                    (int) actuales, Integer.MAX_VALUE,
                    Integer.MAX_VALUE, true, plan.getNombre()
                );
            }

            int limite = plan != null ? plan.getMaxPropiedadesAgencia() : 4;
            long actuales = propiedadRepository.countByAgenciaId(agencia.getId());
            int restantes = limite - (int) actuales;
            return new SuscripcionLimitsDTO(
                (int) actuales, limite, Math.max(restantes, 0), false,
                plan != null ? plan.getNombre() : "Gratuito"
            );
        }

        Suscripcion suscripcion = usuario.getSuscripcion();
        if (suscripcion != null && suscripcion.getTipo() == TipoSuscripcion.PREMIUM) {
            long actuales = propiedadRepository.countByUsuarioId(usuario.getId());
            return new SuscripcionLimitsDTO(
                (int) actuales, Integer.MAX_VALUE,
                Integer.MAX_VALUE, true, "Premium"
            );
        }

        int limite = 2;
        long actuales = propiedadRepository.countByUsuarioId(usuario.getId());
        int restantes = limite - (int) actuales;
        return new SuscripcionLimitsDTO(
            (int) actuales, limite, Math.max(restantes, 0), false, "Gratuito"
        );
    }
}
