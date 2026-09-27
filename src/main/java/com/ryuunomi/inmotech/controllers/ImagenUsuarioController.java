package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.ImagenUsuarioDTO;
import com.ryuunomi.inmotech.entities.ImagenUsuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.mapper.ImagenMapper;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.security.AuthorizationService;
import com.ryuunomi.inmotech.services.imagenusuario.IImagenUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.io.IOException;

@RestController
@RequestMapping("/api/imageUser")
public class ImagenUsuarioController {

    @Autowired
    private IImagenUsuarioService imagenService;

    @Autowired
    private AuthorizationService authorizationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public ResponseEntity<ImagenUsuarioDTO> getImage(@PathVariable Long userId,
                                                     Authentication authentication) {
        try {
            Usuario actor = authorizationService.requireCurrentUser(authentication);
            if (!authorizationService.canManageUser(actor, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            ImagenUsuario imagen = imagenService.obtenerPorUsuario(userId);
            return ResponseEntity.ok(ImagenMapper.toUsuarioDTO(imagen));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{userId}")
    public ResponseEntity<?> upload(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        try {
            Usuario actor = authorizationService.requireCurrentUser(authentication);
            if (!authorizationService.canManageUser(actor, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso sobre este usuario");
            }
            System.out.println("Recibiendo imagen para el usuario ID: " + userId);
            System.out.println("Nombre archivo: " + file.getOriginalFilename());
            ImagenUsuario creada = imagenService.subirImagen(userId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(ImagenMapper.toUsuarioDTO(creada));
        } catch (ResourceNotFoundException ex) {
            System.err.println("Resource not found: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IOException ex) {
            System.err.println("IO ERROR al subir imagen: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar archivo: " + ex.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al subir imagen:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable Long userId, Authentication authentication) {
        try {
            Usuario actor = authorizationService.requireCurrentUser(authentication);
            if (!authorizationService.canManageUser(actor, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso sobre este usuario");
            }
            imagenService.eliminarImagen(userId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al borrar archivo: " + ex.getMessage());
        }
    }
}
