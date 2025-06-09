package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.CapacidadUsuario;
import com.ryuunomi.inmotech.services.agencia.IAgenciaService;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public List<Agencia> listAll() {
        return agenciaService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agencia> findById(@PathVariable Long id) {
        Optional<Agencia> agencia = agenciaService.findById(id);
        return agencia.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Agencia agencia) {
        Optional<Usuario> optionalUsuario = usuarioService.findById(agencia.getIdUsuarioAdmin());

        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario administrador no encontrado.");
        }

        Usuario usuario = optionalUsuario.get();

        // ✅ Validar que no tenga ya una agencia distinta asignada
        System.out.println("Error:" + usuario.getAgencia() + " aaaaaaaaaaaaaa" );

        if (usuario.getAgencia() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El usuario ya está asignado a otra agencia.");
        }

        // ✅ Guardar la agencia solo después de validar
        Agencia agenciaGuardada = agenciaService.save(agencia);

        // Asignar la agencia al usuario
        usuario.setAgencia(agenciaGuardada);

        if (!usuario.getCapacidades().contains(CapacidadUsuario.ADMIN)) {
            usuario.getCapacidades().add(CapacidadUsuario.ADMIN);
        }

        usuarioService.updateUser(usuario.getId(), usuario);

        return ResponseEntity.ok(agenciaGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Agencia agenciaActualizada) {
        Optional<Agencia> agenciaOptional = agenciaService.findById(id);

        if (agenciaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agencia no encontrada");
        }

        Agencia agenciaExistente = agenciaOptional.get();

        Optional<Usuario> nuevoAdminOpt = usuarioService.findById(agenciaActualizada.getIdUsuarioAdmin());
        if (nuevoAdminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nuevo administrador no encontrado.");
        }

        Long idAdminAnterior = agenciaExistente.getIdUsuarioAdmin();
        Long idNuevoAdmin = agenciaActualizada.getIdUsuarioAdmin();

        boolean adminCambiado = !Objects.equals(idAdminAnterior, idNuevoAdmin);

        // CAMBIA EL ADMIIN
        if (adminCambiado) {
            Usuario nuevoAdmin = nuevoAdminOpt.get();

            if (nuevoAdmin.getAgencia() != null && !nuevoAdmin.getAgencia().getId().equals(agenciaExistente.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("El usuario ya administra otra agencia.");
            }
        }

        // Actualizar y guardar la agencia (después de validar)
        agenciaExistente.setNombre(agenciaActualizada.getNombre());
        agenciaExistente.setDescripcion(agenciaActualizada.getDescripcion());
        agenciaExistente.setIdUsuarioAdmin(idNuevoAdmin);
        Agencia agenciaGuardada = agenciaService.save(agenciaExistente);

        // Asignar nueva agencia al nuevo admin
        if (adminCambiado) {
            Usuario nuevoAdmin = nuevoAdminOpt.get();
            nuevoAdmin.setAgencia(agenciaGuardada);

            if (!nuevoAdmin.getCapacidades().contains(CapacidadUsuario.ADMIN)) {
                nuevoAdmin.getCapacidades().add(CapacidadUsuario.ADMIN);
            }

            usuarioService.updateUser(nuevoAdmin.getId(), nuevoAdmin);

            // Limpiar datos del viejo admin
            Optional<Usuario> viejoAdminOpt = usuarioService.findById(idAdminAnterior);
            if (viejoAdminOpt.isPresent()) {
                Usuario viejoAdmin = viejoAdminOpt.get();
                viejoAdmin.setAgencia(null);
                viejoAdmin.getCapacidades().remove(CapacidadUsuario.ADMIN);
                usuarioService.updateUser(viejoAdmin.getId(), viejoAdmin);
            }
        }

        return ResponseEntity.ok(agenciaGuardada);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (agenciaService.findById(id).isPresent()) {
            agenciaService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
