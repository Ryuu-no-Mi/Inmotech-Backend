package com.ryuunomi.inmotech.entities;

import com.ryuunomi.inmotech.enums.TipoSuscripcion;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "suscripcion")
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSuscripcion tipo = TipoSuscripcion.GRATIS;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;

    public Suscripcion() {}

    public Suscripcion(TipoSuscripcion tipo) {
        this.tipo = tipo;
        this.fechaInicio = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoSuscripcion getTipo() { return tipo; }
    public void setTipo(TipoSuscripcion tipo) { this.tipo = tipo; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getStripeSubscriptionId() { return stripeSubscriptionId; }
    public void setStripeSubscriptionId(String stripeSubscriptionId) { this.stripeSubscriptionId = stripeSubscriptionId; }
}
