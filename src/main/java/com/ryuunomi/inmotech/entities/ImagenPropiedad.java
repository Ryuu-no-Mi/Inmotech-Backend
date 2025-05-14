package com.ryuunomi.inmotech.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "imagenes_propiedades")
public class ImagenPropiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private Integer orden; // Para orden en la galería, si lo necesitas

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propiedad", nullable = false)
    private Propiedad propiedad;

    public ImagenPropiedad() {}

    public ImagenPropiedad(String url, Integer orden, Propiedad propiedad) {
        this.url = url;
        this.orden = orden;
        this.propiedad = propiedad;
    }

    // Getters y setters

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }
}
