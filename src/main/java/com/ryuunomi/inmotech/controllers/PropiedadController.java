package com.ryuunomi.inmotech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuunomi.inmotech.dto.ImagenPropiedadDTO;
import com.ryuunomi.inmotech.dto.PropiedadDTO;
import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.CapacidadUsuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.mapper.ImagenMapper;
import com.ryuunomi.inmotech.mapper.PropiedadMapper;
import com.ryuunomi.inmotech.services.imagenpropiedad.IImagenPropiedadService;
import com.ryuunomi.inmotech.services.propiedad.IPropiedadService;
import com.ryuunomi.inmotech.services.suscripcion.ISuscripcionService;
import com.ryuunomi.inmotech.services.suscripcion.SuscripcionLimitsDTO;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173") // dirección del frontend react
@RestController
@RequestMapping("/api/property")
public class PropiedadController {

    @Autowired
    private IPropiedadService propiedadService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IImagenPropiedadService imagenPropiedadService;

    @Autowired
    private ISuscripcionService suscripcionService;

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


    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody PropiedadDTO dto) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario usuario = usuarioService.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            if (!suscripcionService.puedePublicar(usuario)) {
                SuscripcionLimitsDTO limites = suscripcionService.obtenerLimites(usuario);
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                        .body(Map.of(
                            "error", "Has alcanzado el limite de propiedades de tu plan",
                            "limite", limites.limiteMaximo(),
                            "actuales", limites.propiedadesActuales(),
                            "plan", limites.planNombre(),
                            "mensaje", "Actualiza tu plan en /planes para publicar mas propiedades"
                        ));
            }

            Propiedad propiedad = PropiedadMapper.fromDTO(dto);
            Propiedad guardada = propiedadService.save(propiedad);
            return ResponseEntity.status(HttpStatus.CREATED).body(PropiedadMapper.toDTO(guardada));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al crear propiedad: " + e.getMessage());
        }
    }

    //@PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @PostMapping(value = "/{id}/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirImagenes(@PathVariable Long id, @RequestPart("files") MultipartFile[] files) {
        try {
            List<ImagenPropiedad> imagenes = imagenPropiedadService.subirImagenes(id, files);
            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir imágenes: " + e.getMessage());
        }
    }


    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
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


        //El usuario solo puede modificar los pisos creados por el
        boolean esDuenio = propiedad.getUsuario() != null
                && propiedad.getUsuario().getEmail().equals(email);

        // los de idAgencia (pueden ser null)
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

    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
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


    @PreAuthorize("hasAnyRole('ADMIN','AGENTE')")
    @GetMapping("/user/{idUsuario}")
    public List<PropiedadDTO> listByUser(@PathVariable Long idUsuario) {
            List<Propiedad> propiedades = propiedadService.findByUsuarioId(idUsuario);
            List<PropiedadDTO> dtos = new ArrayList<>();
            for (Propiedad p : propiedades) {
                dtos.add(PropiedadMapper.toDTO(p));
            }
            return dtos;
    }


    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @GetMapping("/agency/{idAgencia}")
    public List<PropiedadDTO> listByAgency(@PathVariable Long idAgencia) {
        //return propiedadService.findByAgenciaId(idAgencia);
        List<Propiedad> propiedades = propiedadService.findByAgenciaId(idAgencia);
        List<PropiedadDTO> dtos = new ArrayList<>();
        for (Propiedad p : propiedades) {
            dtos.add(PropiedadMapper.toDTO(p));
        }
        return dtos;
    }

    @GetMapping("/myProperties")
    @PreAuthorize("isAuthenticated()")
    public List<PropiedadDTO> misPropiedades(Authentication auth) {
        String email = auth.getName(); // email viene del token
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Propiedad> propiedades = propiedadService.findByUsuarioId(usuario.getId());
        return propiedades.stream().map(PropiedadMapper::toDTO).collect(Collectors.toList());
    }


}
