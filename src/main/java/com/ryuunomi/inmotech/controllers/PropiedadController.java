package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.services.propiedad.IPropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/property")
public class PropiedadController {

    @Autowired
    private IPropiedadService propiedadService;

    @GetMapping
    public List<Propiedad> list() {
        return propiedadService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewById(@PathVariable Long id) {
        Optional<Propiedad> propiedadOptional = propiedadService.findById(id);
        if (propiedadOptional.isPresent()) {
            return ResponseEntity.ok(propiedadOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Propiedad propiedad) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(propiedadService.save(propiedad));
        try {
            Propiedad nueva = propiedadService.save(propiedad);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Propiedad propiedad) {
        Propiedad propiedadActualizada = propiedadService.update(id, propiedad);
        if (propiedadActualizada != null) {
            return ResponseEntity.ok(propiedadActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Propiedad> propiedadEliminada = propiedadService.deleteById(id);
        if (propiedadEliminada.isPresent()) {
            return ResponseEntity.ok(propiedadEliminada.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * @return  Retorna una lista de propiedades asociadas al usuario con el ID especificado.
     */
    @GetMapping("/usuario/{idUsuario}")
    public List<Propiedad> listByUser(@PathVariable Long idUsuario) {
        return propiedadService.findByUsuarioId(idUsuario);
    }

    /**
     * @return Retorna una lista de propiedades asociadas a una agencia con el ID dado.
     */
    @GetMapping("/agencia/{idAgencia}")
    public List<Propiedad> listByAgency(@PathVariable Long idAgencia) {
        return propiedadService.findByAgenciaId(idAgencia);
    }

}
