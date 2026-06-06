package com.ryuunomi.inmotech.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "plan")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(name = "max_propiedades_usuario", nullable = false)
    private int maxPropiedadesUsuario = 2;

    @Column(name = "max_propiedades_agencia", nullable = false)
    private int maxPropiedadesAgencia = 4;

    @Column(name = "precio_mensual")
    private BigDecimal precioMensual;

    @Column(name = "stripe_price_id")
    private String stripePriceId;

    public Plan() {}

    public Plan(String nombre, int maxPropiedadesUsuario, int maxPropiedadesAgencia, BigDecimal precioMensual) {
        this.nombre = nombre;
        this.maxPropiedadesUsuario = maxPropiedadesUsuario;
        this.maxPropiedadesAgencia = maxPropiedadesAgencia;
        this.precioMensual = precioMensual;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getMaxPropiedadesUsuario() { return maxPropiedadesUsuario; }
    public void setMaxPropiedadesUsuario(int maxPropiedadesUsuario) { this.maxPropiedadesUsuario = maxPropiedadesUsuario; }

    public int getMaxPropiedadesAgencia() { return maxPropiedadesAgencia; }
    public void setMaxPropiedadesAgencia(int maxPropiedadesAgencia) { this.maxPropiedadesAgencia = maxPropiedadesAgencia; }

    public BigDecimal getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(BigDecimal precioMensual) { this.precioMensual = precioMensual; }

    public String getStripePriceId() { return stripePriceId; }
    public void setStripePriceId(String stripePriceId) { this.stripePriceId = stripePriceId; }
}
