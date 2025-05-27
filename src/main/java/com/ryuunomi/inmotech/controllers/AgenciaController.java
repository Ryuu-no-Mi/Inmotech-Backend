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
        // Necesitamo que hay un admin en la agencia
        Optional<Usuario> optionalUsuario = usuarioService.findById(agencia.getIdUsuarioAdmin());

        if (optionalUsuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario administrador no encontrado.");
        }

        Usuario usuario = optionalUsuario.get();

        // Verifica si el usuario ya tiene rol de admin
        boolean esAdmin = usuario.getCapacidades().contains(CapacidadUsuario.ADMIN);

        if (!esAdmin) {
            usuario.getCapacidades().add(CapacidadUsuario.ADMIN);
            usuarioService.updateUser(usuario.getId(), usuario); // Asegúrate de que guarda los cambios
        }

        // Comprobar que el usuario tenga rol de admin y sino agregarselo
        return ResponseEntity.ok(agenciaService.save(agencia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agencia> update(@PathVariable Long id, @RequestBody Agencia agenciaActualizada) {
        Optional<Agencia> agencia = agenciaService.findById(id);
        if (agencia.isPresent()) {
            Agencia a = agencia.get();
            a.setNombre(agenciaActualizada.getNombre());
            a.setDescripcion(agenciaActualizada.getDescripcion());
            a.setIdUsuarioAdmin(agenciaActualizada.getIdUsuarioAdmin());
            return ResponseEntity.ok(agenciaService.save(a));
        } else {
            return ResponseEntity.notFound().build();
        }
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
