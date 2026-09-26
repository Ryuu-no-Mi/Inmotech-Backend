package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.ImagenPropiedadDTO;
import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.mapper.ImagenMapper;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.security.AuthorizationService;
import com.ryuunomi.inmotech.services.imagenpropiedad.IImagenPropiedadService;
import com.ryuunomi.inmotech.services.propiedad.IPropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/imageProperty")
public class ImagenPropiedadController {

    @Autowired
    private IImagenPropiedadService imagenService;

    @Autowired
    private IPropiedadService propiedadService;

    @Autowired
    private AuthorizationService authorizationService;


    // GET /api/property/{propiedadId}/images
    @GetMapping("/{propiedadId}")
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

    // POST /api/property/{propiedadId}
    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @PostMapping("/{propiedadId}")
    public ResponseEntity<?> subir(
            @PathVariable Long propiedadId,
            @RequestParam("files") MultipartFile[] files,
            Authentication authentication) {

        try {
            Usuario actor = authorizationService.requireCurrentUser(authentication);
            Propiedad propiedad = propiedadService.findById(propiedadId)
                    .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
            if (!authorizationService.canManageProperty(actor, propiedad)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso sobre esta propiedad");
            }
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
    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @DeleteMapping("/{propiedadId}/{imageId}")
    public ResponseEntity<?> borrar(
            @PathVariable Long propiedadId,
            @PathVariable Long imageId,
            Authentication authentication) {

        try {
            Usuario actor = authorizationService.requireCurrentUser(authentication);
            Propiedad propiedad = propiedadService.findById(propiedadId)
                    .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
            if (!authorizationService.canManageProperty(actor, propiedad)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso sobre esta propiedad");
            }
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
    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @PutMapping("/{propiedadId}/{imageId}")
    public ResponseEntity<?> cambiarOrden(
            @PathVariable Long propiedadId,
            @PathVariable Long imageId,
            @RequestBody ImagenPropiedadDTO imagen,
            Authentication authentication) {

        Integer nuevoOrden = imagen.orden();
        if (nuevoOrden == null) {
            return ResponseEntity.badRequest().body("Debe indicar el campo 'orden' en el JSON.");
        }

        try {
            Usuario actor = authorizationService.requireCurrentUser(authentication);
            Propiedad propiedad = propiedadService.findById(propiedadId)
                    .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
            if (!authorizationService.canManageProperty(actor, propiedad)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso sobre esta propiedad");
            }
            ImagenPropiedad actualizada = imagenService.actualizarOrden(propiedadId, imageId, nuevoOrden);
            return ResponseEntity.ok(ImagenMapper.toPropiedadDTO(actualizada));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @PutMapping("/{id}/portada")
    public ResponseEntity<?> actualizarPortada(
            @PathVariable Long id,
            @RequestParam("imagenId") Long imagenId,
            Authentication authentication) {

        Propiedad propiedad = propiedadService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));

        Usuario actor = authorizationService.requireCurrentUser(authentication);
        if (!authorizationService.canManageProperty(actor, propiedad)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso sobre esta propiedad");
        }

        ImagenPropiedad imagen = imagenService.findById(imagenId);
        if (imagen.getPropiedad() == null || !id.equals(imagen.getPropiedad().getId())) {
            return ResponseEntity.badRequest().body("La imagen no pertenece a la propiedad indicada");
        }

        propiedad.setImagenPortada(imagen);
        propiedadService.save(propiedad);

        return ResponseEntity.ok("Imagen de portada actualizada");
    }

}
