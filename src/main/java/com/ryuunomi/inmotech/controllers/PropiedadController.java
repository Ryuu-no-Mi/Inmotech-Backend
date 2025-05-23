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
@RequestMapping("/api/propiedad")
public class PropiedadController {

    @Autowired
    private IPropiedadService propiedadService;

    @GetMapping
    public List<Propiedad> list() {
        return propiedadService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewById(@PathVariable Long id) {
        Optional<Propiedad> productOptional = propiedadService.findById(id);
        if(productOptional.isPresent()){
            return ResponseEntity.ok(propiedadService.findById(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Propiedad propiedad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propiedadService.save(propiedad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Propiedad propiedad) {
        Optional<Propiedad> productOPtional = propiedadService.update(id, propiedad);
        if (productOPtional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(productOPtional.get());
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Propiedad> productOptional = propiedadService.deleteById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     *
     * @param idUsuario
     * @return  Retorna una lista de propiedades asociadas al usuario con el ID especificado.
     */
    @GetMapping("/usuario/{idUsuario}")
    public List<Propiedad> listarPorUsuario(@PathVariable Long idUsuario) {
        return propiedadService.findByUsuarioId(idUsuario);
    }

    /**
     *
     * @param idAgencia
     * @return Retorna una lista de propiedades asociadas a una agencia con el ID dado.
     */
    @GetMapping("/agencia/{idAgencia}")
    public List<Propiedad> listarPorAgencia(@PathVariable Long idAgencia) {
        return propiedadService.findByAgenciaId(idAgencia);
    }




}
