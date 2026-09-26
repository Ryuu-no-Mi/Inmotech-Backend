package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.security.AuthorizationService;
import com.ryuunomi.inmotech.services.suscripcion.ISuscripcionService;
import com.ryuunomi.inmotech.services.suscripcion.SuscripcionLimitsDTO;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/subscription")
public class SuscripcionController {

    @Autowired
    private ISuscripcionService suscripcionService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private AuthorizationService authorizationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/limits")
    public ResponseEntity<?> getLimits(Authentication authentication) {
        try {
            Usuario usuario = authorizationService.requireCurrentUser(authentication);
            SuscripcionLimitsDTO limites = suscripcionService.obtenerLimites(usuario);
            return ResponseEntity.ok(limites);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token invalido");
        }
    }

}
