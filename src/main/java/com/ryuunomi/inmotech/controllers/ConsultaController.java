package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.ConsultaDTO;
import com.ryuunomi.inmotech.entities.Consulta;
import com.ryuunomi.inmotech.mapper.ConsultaMapper;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.security.AuthorizationService;
import com.ryuunomi.inmotech.services.consulta.IConsultaService;
import com.ryuunomi.inmotech.services.propiedad.IPropiedadService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inquiry")
public class ConsultaController {

    @Autowired
    private IConsultaService consultaService;

    @Autowired
    private IPropiedadService propiedadService;

    @Autowired
    private AuthorizationService authorizationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ConsultaDTO> list() {
        List<Consulta> consultas = consultaService.list();
        List<ConsultaDTO> dtos = new ArrayList<>();
        for (Consulta consulta : consultas) {
            dtos.add(ConsultaMapper.toDTO(consulta));
        }
        return dtos;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findById(@PathVariable Long id, Authentication authentication) {
        Optional<Consulta> consulta = consultaService.findById(id);
        if (consulta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        if (!authorizationService.canAccessConsulta(actor, consulta.get())) {
            return ResponseEntity.status(403).body("No tienes permiso para consultar esta consulta");
        }
        return ResponseEntity.ok(ConsultaMapper.toDTO(consulta.get()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> findByUser(@PathVariable Long idUsuario, Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        if (!authorizationService.canManageUser(actor, idUsuario)) {
            return ResponseEntity.status(403).body("No tienes permiso para consultar estas consultas");
        }
        List<Consulta> consultas = consultaService.findByUser(idUsuario);
        List<ConsultaDTO> consultaDTO = new ArrayList<>();
        for (Consulta consulta : consultas) {
            consultaDTO.add(ConsultaMapper.toDTO(consulta));
        }
        return ResponseEntity.ok(consultaDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/propiedad/{idPropiedad}")
    public ResponseEntity<?> findByProperty(@PathVariable Long idPropiedad, Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        var propiedad = propiedadService.findById(idPropiedad)
                .orElseThrow(() -> new com.ryuunomi.inmotech.exceptions.ResourceNotFoundException("Propiedad no encontrada"));
        if (!authorizationService.canManageProperty(actor, propiedad)) {
            return ResponseEntity.status(403).body("No tienes permiso para consultar estas consultas");
        }
        List<Consulta> consultas = consultaService.findByProperty(idPropiedad);
        List<ConsultaDTO> consultaDTO = new ArrayList<>();
        for (Consulta consulta : consultas) {
            consultaDTO.add(ConsultaMapper.toDTO(consulta));
        }
        return ResponseEntity.ok(consultaDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ConsultaDTO> create(@RequestBody ConsultaDTO consultaDTO,
                                               Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        Consulta consulta = ConsultaMapper.fromDTO(consultaDTO);
        consulta.setUsuario(actor);
        Consulta guardada = consultaService.save(consulta);
        return ResponseEntity.ok(ConsultaMapper.toDTO(guardada));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        Optional<Consulta> consulta = consultaService.findById(id);
        if (consulta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        if (!authorizationService.canModifyConsulta(actor, consulta.get())) {
            return ResponseEntity.status(403).body("No tienes permiso para borrar esta consulta");
        }
        consultaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ConsultaDTO dto,
                                    Authentication authentication) {
        try {
            Consulta existente = consultaService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Consulta no encontrada"));
            Usuario actor = authorizationService.requireCurrentUser(authentication);
            if (!authorizationService.canModifyConsulta(actor, existente)) {
                return ResponseEntity.status(403).body("No tienes permiso para modificar esta consulta");
            }
            Consulta consulta = ConsultaMapper.fromDTO(dto);
            Consulta actualizada = consultaService.update(id, consulta);
            return ResponseEntity.ok(ConsultaMapper.toDTO(actualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
