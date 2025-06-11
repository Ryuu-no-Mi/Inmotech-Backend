package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.PropiedadDTO;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.CapacidadUsuario;
import com.ryuunomi.inmotech.mapper.PropiedadMapper;
import com.ryuunomi.inmotech.services.propiedad.IPropiedadService;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private IUsuarioService usuarioService;

    // cualquier usuario pued eacceder este o no autenticado
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
    public ResponseEntity<?> listById(@PathVariable Long id) {
        Optional<Propiedad> propiedadOptional = propiedadService.findById(id);

        if (propiedadOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PropiedadDTO dto = PropiedadMapper.toDTO(propiedadOptional.get());
        return ResponseEntity.ok(dto);
    }


    @PreAuthorize("hasAnyRole('USUARIO','ADMIN', 'AGENTE')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PropiedadDTO propiedadDTO) {
        //return ResponseEntity.status(HttpStatus.CREATED).body(propiedadService.save(propiedad));
        try {
            Propiedad entidad = PropiedadMapper.fromDTO(propiedadDTO);
            Propiedad nueva = propiedadService.save(entidad);

            return ResponseEntity.status(HttpStatus.CREATED).body(PropiedadMapper.toDTO(nueva));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMIN', 'AGENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PropiedadDTO propiedadDTO) {

        //verifico que el usuario atentificado se el creador de la propiedad
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(email);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuarioAutenticado = usuarioOptional.get();

        Optional<Propiedad> propiedadOptional = propiedadService.findById(id);
        if (propiedadOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Propiedad propiedad = propiedadOptional.get();

        // podria crear un metodo de verificacion
        //boolean esDuenio = propiedad.getUsuario().getEmail().equals(email);
        //boolean tieneAdmin = usuarioAutenticado.getCapacidades().contains(CapacidadUsuario.ADMIN);

        //El usuario solo puede modificar los pisos creados por el
        boolean esDuenio = propiedad.getUsuario() != null
                && propiedad.getUsuario().getEmail().equals(email);

        // IDs de agencia (pueden ser null)
        Long idAgenciaPropiedad = propiedad.getAgencia() != null
                ? propiedad.getAgencia().getId() : null;
        Long idAgenciaUsuario = usuarioAutenticado.getAgencia() != null
                ? usuarioAutenticado.getAgencia().getId() : null;

        /*
        boolean mismaAgencia = propiedad.getAgencia().getId().equals(
                usuarioAutenticado.getAgencia() != null ? usuarioAutenticado.getAgencia().getId() : null);
         */

        boolean esEnMiAgencia = idAgenciaPropiedad != null
                && idAgenciaPropiedad.equals(idAgenciaUsuario);

        boolean puedeModificar =
                (usuarioAutenticado.getCapacidades().contains(CapacidadUsuario.USUARIO) && esDuenio)
                        || ((usuarioAutenticado.getCapacidades().contains(CapacidadUsuario.AGENTE)
                        || usuarioAutenticado.getCapacidades().contains(CapacidadUsuario.ADMIN))
                        && esEnMiAgencia);

        if (!puedeModificar) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para modificar esta propiedad");
        }

        Propiedad actualizada = propiedadService.update(id, PropiedadMapper.fromDTO(propiedadDTO));
        return ResponseEntity.ok(PropiedadMapper.toDTO(actualizada));
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMIN', 'AGENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        //verifico que el usuario atentificado se el creador de la propiedad
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(email);
        if (usuarioOptional.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Usuario usuarioAutenticado = usuarioOptional.get();

        Optional<Propiedad> propiedadOptional = propiedadService.findById(id);
        if (propiedadOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Propiedad propiedad = propiedadOptional.get();

        // podria crear un metodo de verificacion
        boolean esDuenio = propiedad.getUsuario().getEmail().equals(email);
        boolean tieneAdmin = usuarioAutenticado.getCapacidades().contains(CapacidadUsuario.ADMIN);
        boolean mismaAgencia = propiedad.getAgencia().getId().equals(
                usuarioAutenticado.getAgencia() != null ? usuarioAutenticado.getAgencia().getId() : null);

        if ((usuarioAutenticado.getCapacidades().contains(CapacidadUsuario.USUARIO) ||
                usuarioAutenticado.getCapacidades().contains(CapacidadUsuario.AGENTE)) && esDuenio) {
            propiedadService.deleteById(id);
            return ResponseEntity.ok("Propiedad eliminada");
        } else if (tieneAdmin && mismaAgencia) {
            propiedadService.deleteById(id);
            return ResponseEntity.ok("Propiedad eliminada");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para modificar esta propiedad");
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    @GetMapping("/usuario/{idUsuario}")
    public List<PropiedadDTO> listByUser(@PathVariable Long idUsuario) {
            List<Propiedad> propiedades = propiedadService.findByUsuarioId(idUsuario);
            List<PropiedadDTO> dtos = new ArrayList<>();
            for (Propiedad p : propiedades) {
                dtos.add(PropiedadMapper.toDTO(p));
            }
            return dtos;
    }


    @PreAuthorize("hasAnyRole('USUARIO','ADMIN', 'AGENTE')")
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
