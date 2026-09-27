package com.ryuunomi.inmotech.security;

import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.Consulta;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.CapacidadUsuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UsuarioRepository usuarioRepository;

    public AuthorizationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario requireCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException("Autenticacion requerida");
        }

        return usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));
    }

    public boolean isGlobalAdmin(Usuario usuario) {
        return hasRole(usuario, CapacidadUsuario.ADMIN);
    }

    public boolean canManageUser(Usuario actor, Long targetUserId) {
        return isGlobalAdmin(actor) || actor.getId().equals(targetUserId);
    }

    public boolean canManageProperty(Usuario actor, Propiedad propiedad) {
        if (isGlobalAdmin(actor)) {
            return true;
        }

        boolean isOwner = propiedad.getUsuario() != null
                && actor.getId().equals(propiedad.getUsuario().getId());
        if (isOwner) {
            return true;
        }

        return hasRole(actor, CapacidadUsuario.AGENTE)
                && sameAgency(actor, propiedad.getAgencia());
    }

    public boolean canManageAgency(Usuario actor, Agencia agencia) {
        return isGlobalAdmin(actor)
                || (agencia.getIdUsuarioAdmin() != null
                && agencia.getIdUsuarioAdmin().equals(actor.getId()));
    }

    public boolean canAccessConsulta(Usuario actor, Consulta consulta) {
        if (isGlobalAdmin(actor)) {
            return true;
        }

        boolean isAuthor = consulta.getUsuario() != null
                && actor.getId().equals(consulta.getUsuario().getId());
        return isAuthor || (consulta.getPropiedad() != null
                && canManageProperty(actor, consulta.getPropiedad()));
    }

    public boolean canModifyConsulta(Usuario actor, Consulta consulta) {
        return canAccessConsulta(actor, consulta);
    }

    private boolean sameAgency(Usuario actor, Agencia agencia) {
        return actor.getAgencia() != null
                && agencia != null
                && actor.getAgencia().getId().equals(agencia.getId());
    }

    private boolean hasRole(Usuario usuario, CapacidadUsuario role) {
        return usuario.getCapacidades() != null && usuario.getCapacidades().contains(role);
    }
}
