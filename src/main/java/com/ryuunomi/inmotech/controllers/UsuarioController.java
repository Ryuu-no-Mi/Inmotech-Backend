package com.ryuunomi.inmotech.controllers;

import com.ryuunomi.inmotech.dto.UsuarioDTO;
import com.ryuunomi.inmotech.dto.UsuarioRegistroDTO;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.mapper.UsuarioMapper;
import com.ryuunomi.inmotech.mapper.UsuarioRegistroMapper;
import com.ryuunomi.inmotech.security.util.JwtUtils;
import com.ryuunomi.inmotech.services.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173") // dirección del frontend react
public class UsuarioController {

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
    public ResponseEntity<UsuarioDTO> register(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        //Usuario usuarioEntidad = UsuarioRegistroMapper.fromRegisterDTO(usuarioRegistroDTO);
        Usuario usuarioCreado = usuarioService.registerNewUser(usuarioRegistroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDTO(usuarioCreado));
    }

    // Crear usuario (un admin, crea un agente
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UsuarioDTO> create(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        Usuario usuarioCreado = usuarioService.createUserByAdmin(usuarioRegistroDTO); // Asigna roles AGENTE/ADMIN
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDTO(usuarioCreado));
    }

    // Actualizar un usuario existente
    @PreAuthorize("hasAnyRole('USUARIO','ADMIN','AGENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        if (usuarioService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario entidad = UsuarioRegistroMapper.fromRegisterDTO(usuarioRegistroDTO);
        entidad.setId(id);
        entidad.setImagen(null);
        System.err.println("password codeada::: " + entidad.getContrasenia());
        Usuario guardado = usuarioService.updateUser(id, entidad);
        UsuarioDTO responseDto = UsuarioMapper.toDTO(guardado);
        return ResponseEntity.ok(responseDto);
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

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = JwtUtils.getEmailFromToken(token); // Decodifica el token
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(UsuarioMapper.toDTO(usuario));
    }

}


