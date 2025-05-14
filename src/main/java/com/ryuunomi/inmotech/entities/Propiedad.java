package com.ryuunomi.inmotech.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *   `id` INT AUTO_INCREMENT PRIMARY KEY,
 *   `titulo` VARCHAR(255) NOT NULL,
 *   `descripcion` TEXT,
 *   `precio` DECIMAL(12,2) NOT NULL,
 *   `superficie` INT NOT NULL,
 *
 *   `direccion` VARCHAR(255),
 *   `ciudad` VARCHAR(255),
 *   `provincia` VARCHAR(255),
 *
 *   `codigo_postal` VARCHAR(255),
 *   //
 *   `latitud` DECIMAL(10,8),
 *   `longitud` DECIMAL(11,8),
 *   //
 *   `fecha_publicacion` TIMESTAMP DEFAULT NOW(),
 *   `id_usuario` INT,
 *   `id_agencia` INT
 */

@Entity
@Table(name="propiedad")
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titulo;

    @Lob
    private String descripcion;

    @NotNull
    @Column(nullable = false)
    private BigDecimal precio;

    @NotNull
    @Column(nullable = false)
    private BigDecimal superficie;
    //
    @NotBlank
    @Column(nullable = false)
    private String direccion;

    @NotBlank
    @Column(nullable = false)
    private String ciudad;

    @NotBlank
    @Column(nullable = false)
    private String provincia;
    //
    @Column(name = "codigo_postal")
    private String codigoPostal;
    //
    private Double latitud;
    private Double longitud;
    //

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "fecha_publicacion", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaPublicacion;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenPropiedad> imagenes = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "id_imagen_portada")
    private ImagenPropiedad imagenPortada;

    //
    // Relación con Usuario (quien publica)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Relación con Agencia (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agencia")
    private Agencia agencia;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorito> favoritos = new ArrayList<>();

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consulta> consultas = new ArrayList<>();


    public Propiedad() {
    }

    public Propiedad(String titulo, String descripcion, BigDecimal precio, BigDecimal superficie, String direccion, String ciudad, String provincia, String codigoPostal, Double latitud, Double longitud, LocalDateTime fechaPublicacion, List<ImagenPropiedad> imagenes, ImagenPropiedad imagenPortada, Usuario usuario, Agencia agencia, List<Favorito> favoritos, List<Consulta> consultas) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.superficie = superficie;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaPublicacion = fechaPublicacion;
        this.imagenes = imagenes;
        this.imagenPortada = imagenPortada;
        this.usuario = usuario;
        this.agencia = agencia;
        this.favoritos = favoritos;
        this.consultas = consultas;
    }

    public @NotBlank String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NotBlank String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public @NotNull BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(@NotNull BigDecimal precio) {
        this.precio = precio;
    }

    public @NotNull BigDecimal getSuperficie() {
        return superficie;
    }

    public void setSuperficie(@NotNull BigDecimal superficie) {
        this.superficie = superficie;
    }

    public @NotBlank String getDireccion() {
        return direccion;
    }

    public void setDireccion(@NotBlank String direccion) {
        this.direccion = direccion;
    }

    public @NotBlank String getCiudad() {
        return ciudad;
    }

    public void setCiudad(@NotBlank String ciudad) {
        this.ciudad = ciudad;
    }

    public @NotBlank String getProvincia() {
        return provincia;
    }

    public void setProvincia(@NotBlank String provincia) {
        this.provincia = provincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public List<ImagenPropiedad> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenPropiedad> imagenes) {
        this.imagenes = imagenes;
    }

    public ImagenPropiedad getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(ImagenPropiedad imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
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
