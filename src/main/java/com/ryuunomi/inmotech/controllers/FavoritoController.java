package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.FavoritoDTO;
import com.ryuunomi.inmotech.entities.Favorito;
import com.ryuunomi.inmotech.mapper.FavoritoMapper;
import com.ryuunomi.inmotech.services.favorito.IFavoritoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/favourite")
public class FavoritoController {

    @Autowired
    private IFavoritoService favoritoService;

    @PostMapping("/{userId}/{propertyId}")
    public ResponseEntity<FavoritoDTO> add(
            @PathVariable Long userId,
            @PathVariable Long propertyId) {

        Favorito favorito = favoritoService.agregarFavorito(userId, propertyId);

        if (favorito != null) {
            return ResponseEntity.ok(FavoritoMapper.toDTO(favorito));
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/{propertyId}")
    @Transactional
    public ResponseEntity<Void> delete(
            @PathVariable Long userId,
            @PathVariable Long propertyId) {
        favoritoService.eliminarFavorito(userId, propertyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public List<FavoritoDTO> listByUser(@PathVariable Long userId) {
        List<Favorito> favoritos = favoritoService.obtenerFavoritosPorUsuario(userId);
        List<FavoritoDTO> dtos = new ArrayList<>();

        for (Favorito fav : favoritos) {
            dtos.add(FavoritoMapper.toDTO(fav));
        }

        return dtos;
    }

    @GetMapping("/exist/{userId}/{propertyId}")
    public boolean isFavourite(
            @PathVariable Long userId,
            @PathVariable Long propertyId) {
        return favoritoService.esFavorito(userId, propertyId);
    }
}
