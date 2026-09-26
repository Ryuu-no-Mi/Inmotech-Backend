package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.AgenciaDTO;
import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.CapacidadUsuario;
import com.ryuunomi.inmotech.mapper.AgenciaMapper;
import com.ryuunomi.inmotech.security.AuthorizationService;
import com.ryuunomi.inmotech.services.agencia.IAgenciaService;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/agency")
public class AgenciaController {

    @Autowired
    private IAgenciaService agenciaService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping
    public List<AgenciaDTO> listAll() {
        List<Agencia> entidades = agenciaService.listAll();
        List<AgenciaDTO> dtos = new ArrayList<>();

        for (Agencia agencia : entidades) {
            dtos.add(AgenciaMapper.toDTO(agencia));
        }

        return dtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenciaDTO> findById(@PathVariable Long id) {
        Optional<Agencia> agencia = agenciaService.findById(id);
        return agencia.map(value -> ResponseEntity.ok(AgenciaMapper.toDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody AgenciaDTO dto, Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        if (!authorizationService.isGlobalAdmin(actor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo un administrador global puede crear agencias");
        }
        Optional<Usuario> optionalUsuario = usuarioService.findById(dto.idUsuarioAdmin());

        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario administrador no encontrado.");
        }

        Usuario usuario = optionalUsuario.get();

        if (usuario.getAgencia() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El usuario ya está asignado a otra agencia.");
        }

        Agencia entidad = AgenciaMapper.fromDTO(dto);
        Agencia agenciaGuardada = agenciaService.save(entidad);

        usuario.setAgencia(agenciaGuardada);

        if (!usuario.getCapacidades().contains(CapacidadUsuario.ADMIN)) {
            usuario.getCapacidades().add(CapacidadUsuario.ADMIN);
        }

        usuarioService.updateUser(usuario.getId(), usuario);

        return ResponseEntity.ok(AgenciaMapper.toDTO(agenciaGuardada));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AGENTE')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AgenciaDTO dto,
                                    Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        Optional<Agencia> agenciaOptional = agenciaService.findById(id);

        if (agenciaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agencia no encontrada");
        }

        Agencia agenciaExistente = agenciaOptional.get();

        if (!authorizationService.canManageAgency(actor, agenciaExistente)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para modificar esta agencia");
        }

        Optional<Usuario> nuevoAdminOpt = usuarioService.findById(dto.idUsuarioAdmin());
        if (nuevoAdminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nuevo administrador no encontrado.");
        }

        Long idAdminAnterior = agenciaExistente.getIdUsuarioAdmin();
        Long idNuevoAdmin = dto.idUsuarioAdmin();

        boolean adminCambiado = !Objects.equals(idAdminAnterior, idNuevoAdmin);

        if (adminCambiado && !authorizationService.isGlobalAdmin(actor)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Solo un administrador global puede cambiar el administrador de una agencia");
        }

        if (adminCambiado) {
            Usuario nuevoAdmin = nuevoAdminOpt.get();
            if (nuevoAdmin.getAgencia() != null && !nuevoAdmin.getAgencia().getId().equals(agenciaExistente.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("El usuario ya administra otra agencia.");
            }
        }

        agenciaExistente.setNombre(dto.nombre());
        agenciaExistente.setDescripcion(dto.descripcion());
        agenciaExistente.setIdUsuarioAdmin(idNuevoAdmin);
        Agencia agenciaGuardada = agenciaService.save(agenciaExistente);

        if (adminCambiado) {
            Usuario nuevoAdmin = nuevoAdminOpt.get();
            nuevoAdmin.setAgencia(agenciaGuardada);
            if (!nuevoAdmin.getCapacidades().contains(CapacidadUsuario.ADMIN)) {
                nuevoAdmin.getCapacidades().add(CapacidadUsuario.ADMIN);
            }
            usuarioService.updateUser(nuevoAdmin.getId(), nuevoAdmin);

            Optional<Usuario> viejoAdminOpt = usuarioService.findById(idAdminAnterior);
            if (viejoAdminOpt.isPresent()) {
                Usuario viejoAdmin = viejoAdminOpt.get();
                viejoAdmin.setAgencia(null);
                viejoAdmin.getCapacidades().remove(CapacidadUsuario.ADMIN);
                usuarioService.updateUser(viejoAdmin.getId(), viejoAdmin);
            }
        }

        return ResponseEntity.ok(AgenciaMapper.toDTO(agenciaGuardada));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AGENTE')")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        Usuario actor = authorizationService.requireCurrentUser(authentication);
        Optional<Agencia> agencia = agenciaService.findById(id);
        if (agencia.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!authorizationService.canManageAgency(actor, agencia.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        agenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
