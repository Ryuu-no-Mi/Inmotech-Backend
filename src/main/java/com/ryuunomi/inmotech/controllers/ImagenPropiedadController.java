package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.ImagenPropiedadDTO;
import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.mapper.ImagenMapper;
import com.ryuunomi.inmotech.services.imagenpropiedad.IImagenPropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/property/{propertyId}/images")
public class ImagenPropiedadController {

    @Autowired
    private IImagenPropiedadService imagenService;

    // GET /api/property/{propiedadId}/images
    @GetMapping
    public ResponseEntity<List<ImagenPropiedadDTO>> listar(@PathVariable Long propiedadId) {
        try {
            List<ImagenPropiedad> lista = imagenService.listarPorPropiedad(propiedadId);
            List<ImagenPropiedadDTO> dtoList = new ArrayList<>();
            for (ImagenPropiedad img : lista) {
                dtoList.add(ImagenMapper.toPropiedadDTO(img));
            }
            return ResponseEntity.ok(dtoList);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // POST /api/property/{propiedadId}/images
    @PostMapping
    public ResponseEntity<?> subir(
            @PathVariable Long propiedadId,
            @RequestParam("files") MultipartFile[] files) {

        try {
            List<ImagenPropiedad> creadas = imagenService.subirImagenes(propiedadId, files);
            List<ImagenPropiedadDTO> dtos = new ArrayList<>();
            for (ImagenPropiedad img : creadas) {
                dtos.add(ImagenMapper.toPropiedadDTO(img));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar archivos: " + ex.getMessage());
        }
    }

    // DELETE /api/property/{propiedadId}/images/{imageId}
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

    // PUT /api/property/{propiedadId}/images/{imageId}
    @PutMapping("/{imageId}")
    public ResponseEntity<?> cambiarOrden(
            @PathVariable Long propiedadId,
            @PathVariable Long imageId,
            @RequestBody ImagenPropiedadDTO imagen) {

        Integer nuevoOrden = imagen.orden();
        if (nuevoOrden == null) {
            return ResponseEntity.badRequest().body("Debe indicar el campo 'orden' en el JSON.");
        }

        try {
            ImagenPropiedad actualizada = imagenService.actualizarOrden(propiedadId, imageId, nuevoOrden);
            return ResponseEntity.ok(ImagenMapper.toPropiedadDTO(actualizada));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}