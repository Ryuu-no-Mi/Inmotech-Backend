package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.entities.Consulta;
import com.ryuunomi.inmotech.services.consulta.IConsultaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inquiry")
public class ConsultaController {

    @Autowired
    private IConsultaService consultaService;

    @GetMapping
    public List<Consulta> listarConsultas() {
        return consultaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerConsulta(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaService.obtenerPorId(id);

        if (consulta.isEmpty()){
            return ResponseEntity.notFound().build();
            //return ResponseEntity.badRequest().body("Debe indicar el campo 'orden' en el JSON.");
        }

        return ResponseEntity.ok(consultaService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Consulta> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return consultaService.obtenerPorUsuario(idUsuario);
    }

    @GetMapping("/propiedad/{idPropiedad}")
    public List<Consulta> obtenerPorPropiedad(@PathVariable Long idPropiedad) {
        return consultaService.obtenerPorPropiedad(idPropiedad);
    }

    @PostMapping
    public ResponseEntity<Consulta> crearConsulta(@RequestBody Consulta consulta) {
        return ResponseEntity.ok(consultaService.guardarConsulta(consulta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarConsulta(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaService.obtenerPorId(id);
        if (consulta.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        consultaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> actualizarConsulta(@PathVariable Long id, @RequestBody Consulta consulta) {
        try {
            Consulta actualizada = consultaService.actualizarConsulta(id, consulta);
            return ResponseEntity.ok(actualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
