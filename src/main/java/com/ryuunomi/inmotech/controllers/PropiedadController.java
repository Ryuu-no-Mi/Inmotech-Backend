package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.PropiedadDTO;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.mapper.PropiedadMapper;
import com.ryuunomi.inmotech.services.propiedad.IPropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173") // dirección del frontend react
@RestController
@RequestMapping("/api/property")
public class PropiedadController {

    @Autowired
    private IPropiedadService propiedadService;

    @GetMapping
    public List<PropiedadDTO> list() {
        List<Propiedad> entidades = propiedadService.findAll();
        List<PropiedadDTO> dtos = new ArrayList<>();

        for (Propiedad p : entidades) {
            dtos.add(PropiedadMapper.toDTO(p));
        }

        return dtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listByID(@PathVariable Long id) {
        Optional<Propiedad> propiedadOptional = propiedadService.findById(id);

        if (propiedadOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PropiedadDTO dto = PropiedadMapper.toDTO(propiedadOptional.get());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PropiedadDTO propiedadDTO) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(propiedadService.save(propiedad));
        try {
            Propiedad entidad = PropiedadMapper.fromDTO(propiedadDTO);
            Propiedad nueva = propiedadService.save(entidad);

            return ResponseEntity.status(HttpStatus.CREATED).body(PropiedadMapper.toDTO(nueva));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PropiedadDTO propiedadDTO) {
        Propiedad propiedadActualizada = propiedadService.update(id, PropiedadMapper.fromDTO(propiedadDTO));

        if (propiedadActualizada != null) {
            return ResponseEntity.ok(PropiedadMapper.toDTO(propiedadActualizada));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Propiedad> propiedadEliminada = propiedadService.deleteById(id);
        if (propiedadEliminada.isPresent()) {
            return ResponseEntity.ok(PropiedadMapper.toDTO(propiedadEliminada.get()));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * @return  Retorna una lista de propiedades asociadas al usuario con el ID especificado.
     */
    @GetMapping("/usuario/{idUsuario}")
    public List<PropiedadDTO> listByUser(@PathVariable Long idUsuario) {
            List<Propiedad> propiedades = propiedadService.findByUsuarioId(idUsuario);
            List<PropiedadDTO> dtos = new ArrayList<>();
            for (Propiedad p : propiedades) {
                dtos.add(PropiedadMapper.toDTO(p));
            }
            return dtos;
    }

    /**
     * @return Retorna una lista de propiedades asociadas a una agencia con el ID dado.
     */
    @GetMapping("/agencia/{idAgencia}")
    public List<PropiedadDTO> listByAgency(@PathVariable Long idAgencia) {
        //return propiedadService.findByAgenciaId(idAgencia);
        List<Propiedad> propiedades = propiedadService.findByAgenciaId(idAgencia);
        List<PropiedadDTO> dtos = new ArrayList<>();
        for (Propiedad p : propiedades) {
            dtos.add(PropiedadMapper.toDTO(p));
        }
        return dtos;
    }

}
