package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.repository.ImagenPropiedadRepository;
import com.ryuunomi.inmotech.repository.PropiedadRepository;
import com.ryuunomi.inmotech.services.imagenpropiedad.IImagenPropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImagenPropiedadController {

    @Autowired
    private IImagenPropiedadService imagenService;

    /**
     * GET /api/property/{propiedadId}/images
     * Lista todas las imágenes de la propiedad (con id = propiedadId), ordenadas por "orden".
     */
    @GetMapping
    public ResponseEntity<List<ImagenPropiedad>> listar(
            @PathVariable Long propiedadId) {

        try {
            List<ImagenPropiedad> lista = imagenService.listarPorPropiedad(propiedadId);
            return ResponseEntity.ok(lista);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * POST /api/property/{propiedadId}/images
     * Recibe uno o varios archivos en "files" (multipart/form-data).
     * Devuelve la lista de ImagenPropiedad recién creadas.
     */
    @PostMapping
    public ResponseEntity<?> subir(
            @PathVariable Long propiedadId,
            @RequestParam("files") MultipartFile[] files) {

        try {
            List<ImagenPropiedad> creadas = imagenService.subirImagenes(propiedadId, files);
            return ResponseEntity.status(HttpStatus.CREATED).body(creadas);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar archivos: " + ex.getMessage());
        }
    }

    /**
     * DELETE /api/property/{propiedadId}/images/{imageId}
     * Elimina la imagen física y su registro en BD.
     */
    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> borrar(
            @PathVariable Long propiedadId,
            @PathVariable Long imageId) {

        try {
            imagenService.eliminarImagen(propiedadId, imageId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al borrar el archivo: " + ex.getMessage());
        }
    }

    /**
     * PUT /api/property/{propiedadId}/images/{imageId}
     * (Opcional) cambia sólo el campo "orden" de la imagen.
     * Body JSON → { "orden": 2 }
     */
    @PutMapping("/{imageId}")
    public ResponseEntity<?> cambiarOrden(
            @PathVariable Long propiedadId,
            @PathVariable Long imageId,
            @RequestBody ImagenPropiedad payload) {

        Integer nuevoOrden = payload.getOrden();
        if (nuevoOrden == null) {
            return ResponseEntity.badRequest().body("Debe indicar el campo 'orden' en el JSON.");
        }

        try {
            ImagenPropiedad actualizada = imagenService.actualizarOrden(propiedadId, imageId, nuevoOrden);
            return ResponseEntity.ok(actualizada);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}