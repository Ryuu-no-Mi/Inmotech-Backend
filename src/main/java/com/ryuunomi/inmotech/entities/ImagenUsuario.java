package com.ryuunomi.inmotech.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "imagen_usuario")
public class ImagenUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url; // o ruta local si no usas almacenamiento en la nube

    private String nombreArchivo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    public ImagenUsuario() {}

    public ImagenUsuario(String url, String nombreArchivo, Usuario usuario) {
        this.url = url;
        this.nombreArchivo = nombreArchivo;
        this.usuario = usuario;
    }

    // Getters y setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
