package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 1. Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> list() {
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> list(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }
        return  ResponseEntity.notFound().build();
    }


    // 3. Registro público de usuario (sin autenticación previa)
    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario nuevo) {
        Usuario creado = usuarioService.createUser(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }


    // 4. Crear usuario (ej. por un admin, crea un agente)
    @PostMapping("/create")
    public ResponseEntity<Usuario> crear(@RequestBody Usuario nuevo) {
        Usuario creado = usuarioService.createUser(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }


    // 5. Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario cambios) {
        Usuario actualizado = usuarioService.updateUser(id, cambios);
        return ResponseEntity.ok(actualizado);
    }


    // 6. Eliminar un usuario por email
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> eliminarPorEmail(@PathVariable String email) {
        if (!usuarioService.existsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }


    // 7. Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        if (usuarioService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}


