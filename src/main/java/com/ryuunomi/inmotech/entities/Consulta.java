package com.ryuunomi.inmotech.entities;

import com.ryuunomi.inmotech.enums.EstadoConsulta;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_propiedad", nullable = false)
    private Propiedad propiedad;

    @Lob
    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    /*
     @Column(nullable = false)
     private String estado = "pendiente";
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoConsulta estado = EstadoConsulta.PENDIENTE;

    public Consulta() {}

    public Consulta(Usuario usuario, Propiedad propiedad, String mensaje, LocalDateTime fecha, EstadoConsulta estado) {
        this.usuario = usuario;
        this.propiedad = propiedad;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoConsulta getEstado() {
        return estado;
    }

    public void setEstado(EstadoConsulta estado) {
        this.estado = estado;
    }
}
