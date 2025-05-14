package com.ryuunomi.inmotech.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuunomi.inmotech.enums.CapacidadUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
     *   id INT AUTO_INCREMENT PRIMARY KEY,
     *   nombre VARCHAR(255),
     *   apellido VARCHAR(255),
     *   email VARCHAR(255) UNIQUE NOT NULL,
     *   contraseña VARCHAR(255) NOT NULL,
     *   telefono VARCHAR(255),
     *   fecha_registro TIMESTAMP DEFAULT NOW(),
     *   id_agencia INT
     */

@Entity
@Table(name="usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String contrasenia;

    private String telefono;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @CreationTimestamp
    @Column(name = "fecha_registro", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agencia")
    private Agencia agencia;

    /**
     * Un Set en Java es una colección que no permite elementos duplicados.
     * - No hay orden garantizado.
     * - No puede tener elementos repetidos.
     * - Es útil cuando quieres saber si algo existe, no cuántas veces.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_capacidades", joinColumns = @JoinColumn(name = "id_usuario"))
    @Enumerated(EnumType.STRING)
    @Column(name = "capacidad")
    private Set<CapacidadUsuario> capacidades = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorito> favoritos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consulta> consultas = new ArrayList<>();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private ImagenUsuario imagen;


    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String email, String contrasenia, String telefono, LocalDate fechaNacimiento, LocalDate fechaRegistro, Agencia agencia, Set<CapacidadUsuario> capacidades, List<Favorito> favoritos, List<Consulta> consultas, ImagenUsuario imagen) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.agencia = agencia;
        this.capacidades = capacidades;
        this.favoritos = favoritos;
        this.consultas = consultas;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank String getApellido() {
        return apellido;
    }

    public void setApellido(@NotBlank String apellido) {
        this.apellido = apellido;
    }

    public @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank String email) {
        this.email = email;
    }

    public @NotBlank String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(@NotBlank String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public Set<CapacidadUsuario> getCapacidades() {
        return capacidades;
    }

    public void setCapacidades(Set<CapacidadUsuario> capacidades) {
        this.capacidades = capacidades;
    }

    public List<Favorito> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Favorito> favoritos) {
        this.favoritos = favoritos;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
