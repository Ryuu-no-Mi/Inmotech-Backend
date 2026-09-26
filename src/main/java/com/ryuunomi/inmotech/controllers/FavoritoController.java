package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.FavoritoDTO;
import com.ryuunomi.inmotech.entities.Favorito;
import com.ryuunomi.inmotech.mapper.FavoritoMapper;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.security.AuthorizationService;
import com.ryuunomi.inmotech.services.favorito.IFavoritoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/favourite")
public class FavoritoController {

    @Autowired
    private IFavoritoService favoritoService;

    @Autowired
    private AuthorizationService authorizationService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{userId}/{propertyId}")
    public ResponseEntity<FavoritoDTO> add(
            @PathVariable Long userId,
            @PathVariable Long propertyId,
            Authentication authentication) {

        Usuario actor = authorizationService.requireCurrentUser(authentication);
        if (!authorizationService.canManageUser(actor, userId)) {
            return ResponseEntity.status(403).build();
        }

        Favorito favorito = favoritoService.agregarFavorito(actor.getId(), propertyId);

        if (favorito != null) {
            return ResponseEntity.ok(FavoritoMapper.toDTO(favorito));
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/{propertyId}")
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(
            @PathVariable Long userId,
            @PathVariable Long propertyId,
            Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        if (!authorizationService.canManageUser(actor, userId)) {
            return ResponseEntity.status(403).build();
        }
        favoritoService.eliminarFavorito(actor.getId(), propertyId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public ResponseEntity<?> listByUser(@PathVariable Long userId, Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        if (!authorizationService.canManageUser(actor, userId)) {
            return ResponseEntity.status(403).body("No tienes permiso para consultar estos favoritos");
        }
        List<Favorito> favoritos = favoritoService.obtenerFavoritosPorUsuario(actor.getId());
        List<FavoritoDTO> dtos = new ArrayList<>();

        for (Favorito fav : favoritos) {
            dtos.add(FavoritoMapper.toDTO(fav));
        }

        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/exist/{userId}/{propertyId}")
    public ResponseEntity<?> isFavourite(
            @PathVariable Long userId,
            @PathVariable Long propertyId,
            Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        if (!authorizationService.canManageUser(actor, userId)) {
            return ResponseEntity.status(403).body("No tienes permiso para consultar estos favoritos");
        }
        return ResponseEntity.ok(favoritoService.esFavorito(actor.getId(), propertyId));
    }
}
