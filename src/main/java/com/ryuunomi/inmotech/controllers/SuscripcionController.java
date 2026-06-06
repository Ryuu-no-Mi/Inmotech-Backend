package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.security.util.JwtUtils;
import com.ryuunomi.inmotech.services.suscripcion.ISuscripcionService;
import com.ryuunomi.inmotech.services.suscripcion.SuscripcionLimitsDTO;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
public class SuscripcionController {

    @Autowired
    private ISuscripcionService suscripcionService;

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/limits")
    public ResponseEntity<?> getLimits(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = JwtUtils.getEmailFromToken(token);
            Usuario usuario = usuarioService.findByEmail(email).orElse(null);

            if (usuario == null) {
                return ResponseEntity.status(401).body("Usuario no encontrado");
            }

            SuscripcionLimitsDTO limites = suscripcionService.obtenerLimites(usuario);
            return ResponseEntity.ok(limites);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token invalido");
        }
    }
}
