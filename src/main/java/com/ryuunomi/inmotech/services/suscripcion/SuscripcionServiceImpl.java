package com.ryuunomi.inmotech.services.suscripcion;

import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.Plan;
import com.ryuunomi.inmotech.entities.Suscripcion;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.TipoSuscripcion;
import com.ryuunomi.inmotech.repositories.PropiedadRepository;
import com.ryuunomi.inmotech.repositories.SuscripcionRepository;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuscripcionServiceImpl implements ISuscripcionService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    @Override
    @Transactional
    public void activarPremium(Long usuarioId, String stripeSubscriptionId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return;

        Suscripcion suscripcion = usuario.getSuscripcion();
        if (suscripcion == null) {
            suscripcion = new Suscripcion(TipoSuscripcion.PREMIUM);
            suscripcion = suscripcionRepository.save(suscripcion);
            usuario.setSuscripcion(suscripcion);
        } else {
            suscripcion.setTipo(TipoSuscripcion.PREMIUM);
        }
        suscripcion.setStripeSubscriptionId(stripeSubscriptionId);
        usuarioRepository.save(usuario);
        System.out.println("Usuario " + usuarioId + " upgraded to PREMIUM via Stripe");
    }

    @Override
    @Transactional
    public void desactivarPremium(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null || usuario.getSuscripcion() == null) return;

        Suscripcion suscripcion = usuario.getSuscripcion();
        suscripcion.setTipo(TipoSuscripcion.GRATIS);
        suscripcion.setStripeSubscriptionId(null);
        usuarioRepository.save(usuario);
        System.out.println("Usuario " + usuarioId + " downgraded to FREE");
    }
}
