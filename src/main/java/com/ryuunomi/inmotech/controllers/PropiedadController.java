package com.ryuunomi.inmotech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuunomi.inmotech.dto.BusquedaDTO;
import com.ryuunomi.inmotech.dto.FacetaDTO;
import com.ryuunomi.inmotech.dto.ImagenPropiedadDTO;
import com.ryuunomi.inmotech.dto.PageResponse;
import com.ryuunomi.inmotech.dto.PropiedadDTO;
import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.enums.CapacidadUsuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.mapper.ImagenMapper;
import com.ryuunomi.inmotech.mapper.PropiedadMapper;
import com.ryuunomi.inmotech.services.imagenpropiedad.IImagenPropiedadService;
import com.ryuunomi.inmotech.services.propiedad.IPropiedadService;
import com.ryuunomi.inmotech.services.suscripcion.ISuscripcionService;
import com.ryuunomi.inmotech.services.suscripcion.SuscripcionLimitsDTO;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import com.ryuunomi.inmotech.services.agencia.IAgenciaService;
import com.ryuunomi.inmotech.security.AuthorizationService;
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

    @Autowired
    private IAgenciaService agenciaService;

    @Autowired
    private AuthorizationService authorizationService;

    // cualquier usuario pued eacceder este o no autenticado
    @GetMapping
    public PageResponse<PropiedadDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        org.springframework.data.domain.Page<Propiedad> pageResult =
            propiedadService.findAllActivas(org.springframework.data.domain.PageRequest.of(page, size));
        List<PropiedadDTO> dtos = pageResult.getContent().stream()
            .map(PropiedadMapper::toDTO)
            .toList();
        return new PageResponse<>(dtos, pageResult.getNumber(), pageResult.getSize(),
            pageResult.getTotalElements(), pageResult.getTotalPages(),
            pageResult.isFirst(), pageResult.isLast());
    }

    @GetMapping("/buscar")
    public PageResponse<PropiedadDTO> buscar(
            @RequestParam(required = false) String operacion,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) List<String> tipos,
            @RequestParam(required = false) String precioMin,
            @RequestParam(required = false) String precioMax,
            @RequestParam(required = false) String superficieMin,
            @RequestParam(required = false) String superficieMax,
            @RequestParam(required = false) String distrito,
            @RequestParam(required = false) String barrio,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        BusquedaDTO dto = new BusquedaDTO(operacion, texto, ciudad, provincia, tipos, precioMin, precioMax, superficieMin, superficieMax, distrito, barrio);
        org.springframework.data.domain.Page<Propiedad> pageResult =
            propiedadService.buscarConFiltros(dto, org.springframework.data.domain.PageRequest.of(page, size));
        List<PropiedadDTO> dtos = pageResult.getContent().stream()
            .map(PropiedadMapper::toDTO)
            .toList();
        return new PageResponse<>(dtos, pageResult.getNumber(), pageResult.getSize(),
            pageResult.getTotalElements(), pageResult.getTotalPages(),
            pageResult.isFirst(), pageResult.isLast());
    }

    @GetMapping("/facetas")
    public FacetaDTO facetas(
            @RequestParam(required = false) String operacion,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) List<String> tipos,
            @RequestParam(required = false) String precioMin,
            @RequestParam(required = false) String precioMax,
            @RequestParam(required = false) String superficieMin,
            @RequestParam(required = false) String superficieMax,
            @RequestParam(required = false) String distrito,
            @RequestParam(required = false) String barrio
    ) {
        BusquedaDTO dto = new BusquedaDTO(operacion, null, ciudad, provincia, tipos, precioMin, precioMax, superficieMin, superficieMax, distrito, barrio);
        return propiedadService.getFacetas(dto);
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
    public ResponseEntity<?> create(@jakarta.validation.Valid @RequestBody PropiedadDTO dto,
                                    Authentication authentication) {
        try {
            Usuario usuario = authorizationService.requireCurrentUser(authentication);

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
            propiedad.setUsuario(usuario);
            if (dto.idAgencia() != null) {
                Agencia agencia = agenciaService.findById(dto.idAgencia())
                        .orElseThrow(() -> new ResourceNotFoundException("Agencia no encontrada"));
                if (!authorizationService.isGlobalAdmin(usuario)
                        && (usuario.getAgencia() == null
                        || !usuario.getAgencia().getId().equals(agencia.getId()))) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("No tienes permiso para publicar en esta agencia");
                }
                propiedad.setAgencia(agencia);
            } else {
                propiedad.setAgencia(null);
            }
            Propiedad guardada = propiedadService.save(propiedad);
            return ResponseEntity.status(HttpStatus.CREATED).body(PropiedadMapper.toDTO(guardada));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al crear propiedad: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @PostMapping(value = "/{id}/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirImagenes(@PathVariable Long id,
                                           @RequestPart("files") MultipartFile[] files,
                                           Authentication authentication) {
        try {
            Usuario actor = authorizationService.requireCurrentUser(authentication);
            Propiedad propiedad = propiedadService.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
            if (!authorizationService.canManageProperty(actor, propiedad)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso sobre esta propiedad");
            }
            List<ImagenPropiedad> imagenes = imagenPropiedadService.subirImagenes(id, files);
            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir imágenes: " + e.getMessage());
        }
    }


    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @jakarta.validation.Valid @RequestBody PropiedadDTO propiedadDTO,
                                    Authentication authentication) {

        Usuario usuarioAutenticado = authorizationService.requireCurrentUser(authentication);

        Optional<Propiedad> propiedadOptional = propiedadService.findById(id);
        if (propiedadOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Propiedad propiedad = propiedadOptional.get();


        if (!authorizationService.canManageProperty(usuarioAutenticado, propiedad)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para modificar esta propiedad");
        }

        Propiedad cambios = PropiedadMapper.fromDTO(propiedadDTO);
        cambios.setUsuario(propiedad.getUsuario());
        if (propiedadDTO.idAgencia() != null) {
            Agencia agenciaDestino = agenciaService.findById(propiedadDTO.idAgencia())
                    .orElseThrow(() -> new ResourceNotFoundException("Agencia no encontrada"));
            if (!authorizationService.isGlobalAdmin(usuarioAutenticado)
                    && (usuarioAutenticado.getAgencia() == null
                    || !usuarioAutenticado.getAgencia().getId().equals(agenciaDestino.getId()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No tienes permiso para asignar esta agencia");
            }
            cambios.setAgencia(agenciaDestino);
        } else if (usuarioAutenticado.getCapacidades().contains(CapacidadUsuario.AGENTE)) {
            cambios.setAgencia(propiedad.getAgencia());
        }

        Propiedad actualizada = propiedadService.update(id, cambios);
        return ResponseEntity.ok(PropiedadMapper.toDTO(actualizada));
    }

    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {

        //verifico que el usuario atentificado se el creador de la propiedad
        Usuario usuarioAutenticado = authorizationService.requireCurrentUser(authentication);

        Optional<Propiedad> propiedadOptional = propiedadService.findById(id);
        if (propiedadOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Propiedad propiedad = propiedadOptional.get();

        if (authorizationService.canManageProperty(usuarioAutenticado, propiedad)) {
            propiedadService.deleteById(id);
            return ResponseEntity.ok("Propiedad eliminada");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para modificar esta propiedad");
    }


    @PreAuthorize("hasAnyRole('ADMIN','AGENTE')")
    @GetMapping("/user/{idUsuario}")
    public ResponseEntity<?> listByUser(@PathVariable Long idUsuario, Authentication authentication) {
            Usuario actor = authorizationService.requireCurrentUser(authentication);
            Usuario target = usuarioService.findById(idUsuario)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            boolean sameAgency = actor.getAgencia() != null && target.getAgencia() != null
                    && actor.getAgencia().getId().equals(target.getAgencia().getId());
            if (!authorizationService.isGlobalAdmin(actor)
                    && !(actor.getCapacidades().contains(CapacidadUsuario.AGENTE) && sameAgency)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para consultar estas propiedades");
            }
            List<Propiedad> propiedades = propiedadService.findByUsuarioId(idUsuario);
            List<PropiedadDTO> dtos = new ArrayList<>();
            for (Propiedad p : propiedades) {
                dtos.add(PropiedadMapper.toDTO(p));
            }
            return ResponseEntity.ok(dtos);
    }


    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @GetMapping("/agency/{idAgencia}")
    public ResponseEntity<?> listByAgency(@PathVariable Long idAgencia, Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        Agencia agencia = agenciaService.findById(idAgencia)
                .orElseThrow(() -> new ResourceNotFoundException("Agencia no encontrada"));
        boolean sameAgency = actor.getAgencia() != null
                && actor.getAgencia().getId().equals(agencia.getId());
        if (!authorizationService.isGlobalAdmin(actor)
                && !(actor.getCapacidades().contains(CapacidadUsuario.AGENTE) && sameAgency)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para consultar esta agencia");
        }
        //return propiedadService.findByAgenciaId(idAgencia);
        List<Propiedad> propiedades = propiedadService.findByAgenciaId(idAgencia);
        List<PropiedadDTO> dtos = new ArrayList<>();
        for (Propiedad p : propiedades) {
            dtos.add(PropiedadMapper.toDTO(p));
        }
        return ResponseEntity.ok(dtos);
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
