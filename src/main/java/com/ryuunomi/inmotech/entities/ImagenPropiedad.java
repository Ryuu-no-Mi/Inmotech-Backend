package com.ryuunomi.inmotech.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "imagenes_propiedades")
public class ImagenPropiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String url;

    private Integer orden; // Orden de la galeria o carrusel

    // relacion con la tabla ImagenPropiedad
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propiedad", nullable = false,
            foreignKey = @ForeignKey(name = "imagenes_propiedades_ibfk_1"))
    @JsonIgnoreProperties("imagenes")          // evita el bucle al serializar
    @JsonBackReference
    private Propiedad propiedad;

    public ImagenPropiedad() {}

    public ImagenPropiedad(String url, Integer orden, Propiedad propiedad) {
        this.url = url;
        this.orden = orden;
        this.propiedad = propiedad;
    }

//    public ImagenPropiedad(String url, Integer orden, Long idPropiedad) {
//        this.url = url;
//        this.orden = orden;
//        this.idPropiedad = idPropiedad;
//    }

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

//    public Long getIdPropiedad() {
//        return idPropiedad;
//    }
//
//    public void setIdPropiedad(Long idPropiedad) {
//        this.idPropiedad = idPropiedad;
//    }
}
