package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.ConsultaDTO;
import com.ryuunomi.inmotech.entities.Consulta;
import com.ryuunomi.inmotech.mapper.ConsultaMapper;
import com.ryuunomi.inmotech.services.consulta.IConsultaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inquiry")
public class ConsultaController {

    @Autowired
    private IConsultaService consultaService;

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
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaService.findById(id);
        if (consulta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ConsultaMapper.toDTO(consulta.get()));
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<ConsultaDTO> findByUser(@PathVariable Long idUsuario) {
        List<Consulta> consultas = consultaService.findByUser(idUsuario);
        List<ConsultaDTO> consultaDTO = new ArrayList<>();
        for (Consulta consulta : consultas) {
            consultaDTO.add(ConsultaMapper.toDTO(consulta));
        }
        return consultaDTO;
    }

    @GetMapping("/propiedad/{idPropiedad}")
    public List<ConsultaDTO> findByProperty(@PathVariable Long idPropiedad) {
        List<Consulta> consultas = consultaService.findByProperty(idPropiedad);
        List<ConsultaDTO> consultaDTO = new ArrayList<>();
        for (Consulta consulta : consultas) {
            consultaDTO.add(ConsultaMapper.toDTO(consulta));
        }
        return consultaDTO;
    }

    @PostMapping
    public ResponseEntity<ConsultaDTO> create(@RequestBody ConsultaDTO consultaDTO) {
        Consulta consulta = ConsultaMapper.fromDTO(consultaDTO);
        Consulta guardada = consultaService.save(consulta);
        return ResponseEntity.ok(ConsultaMapper.toDTO(guardada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaService.findById(id);
        if (consulta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        consultaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDTO> update(@PathVariable Long id, @RequestBody ConsultaDTO dto) {
        try {
            Consulta consulta = ConsultaMapper.fromDTO(dto);
            Consulta actualizada = consultaService.update(id, consulta);
            return ResponseEntity.ok(ConsultaMapper.toDTO(actualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
