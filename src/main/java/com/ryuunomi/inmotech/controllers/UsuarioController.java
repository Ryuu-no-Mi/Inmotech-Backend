package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.UsuarioDTO;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.mapper.UsuarioMapper;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {
    /**
     * ¿Que puede hacer un usuario?
     * -- Se puede crear una cuenta con un email
     * --Si el email existe, da error, hay que verificar si el email existe
     * -- Si el email no existe, cuenta creada
     * -- Puede actualizar datos de su cuenta (nombre, apellidos, edad, contraseña, telefono)
     * -- si quiere cambiar el email, debera de comprobarse que no exista el nuevo email
     * -- si quiere cambair la contraseña, debera de comprobarse el hash de la antigua contraseña primero
     * -- Podra buscar por agencias especificasa tarves de su nombre ???
     * -- Puede eliminar su cuenta
     */

    @Autowired
    private IUsuarioService usuarioService;

    // Listar todos los usuarios
    @GetMapping
    public List<UsuarioDTO> list() {
        /*
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
         */
        List<Usuario> usuarios = usuarioService.findAll();
        List<UsuarioDTO> dtos = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            dtos.add(UsuarioMapper.toDTO(usuario));
        }

        return dtos;
    }

    // Listar por id
    @GetMapping("/{id}")
    public ResponseEntity<?> listById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isEmpty()) {
            return  ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(UsuarioMapper.toDTO(usuario.get()));
    }

    // Registro público de usuario (sin autenticación previa)
    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> register(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioEntidad = UsuarioMapper.fromDTO(usuarioDTO);
        Usuario usuarioCreado = usuarioService.createUser(usuarioEntidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDTO(usuarioCreado));
    }

    // Crear usuario (un admin, crea un agente)
    @PostMapping("/create")
    public ResponseEntity<UsuarioDTO> create(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario nuevo = UsuarioMapper.fromDTO(usuarioDTO);
        Usuario creado = usuarioService.createUser(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDTO(creado));
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @RequestBody UsuarioDTO cambios) {
        if (usuarioService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario actualizado = UsuarioMapper.fromDTO(cambios);
        actualizado.setId(id);
        Usuario guardado = usuarioService.updateUser(id, actualizado);
        return ResponseEntity.ok(UsuarioMapper.toDTO(guardado));
    }

    // Eliminar un usuario por email
    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> deleteByEmail(@PathVariable String email) {
        if (!usuarioService.existsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (usuarioService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}


