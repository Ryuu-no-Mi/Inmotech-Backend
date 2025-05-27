package com.ryuunomi.inmotech.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="agencia")
public class Agencia {
    /**
     *   `id` INT AUTO_INCREMENT PRIMARY KEY,
     *   `nombre` VARCHAR(255) NOT NULL,
     *   `descripcion` TEXT,
     *   `id_usuario_admin` INT
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Lob
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name="id_usuario_admin")
    private Long idUsuarioAdmin;

    public Agencia() {
    }

    public Agencia(String nombre, String descripcion, Long idUsuarioAdmin) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idUsuarioAdmin = idUsuarioAdmin;
    }

    public @NotBlank String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NotBlank String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuarioAdmin() {
        return idUsuarioAdmin;
    }

    public void setIdUsuarioAdmin(Long idUsuarioAdmin) {
        this.idUsuarioAdmin = idUsuarioAdmin;
    }

    public @NotBlank String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
