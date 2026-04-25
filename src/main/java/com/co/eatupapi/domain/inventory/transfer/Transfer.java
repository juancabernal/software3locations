package com.co.eatupapi.domain.inventory.transfer;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_traslado")
    private Long idTraslado;

    @Column(name = "sede_origen", nullable = false)
    private String sedeOrigen;

    @Column(name = "sede_destino", nullable = false)
    private String sedeDestino;

    @Column(name = "fecha envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha llegada", nullable = false)
    private LocalDateTime fechaLlegada;

    @Column(name = "responsable", nullable = false)
    private String responsable;

    @Column(name = "producto", nullable = false)
    private String producto;

    @Column(name = "stock", nullable = false, precision = 15, scale = 3)
    private BigDecimal stock;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "observaciones")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private TransferStatus estado;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Transfer() {
        // Required by JPA.
    }


    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = TransferStatus.EN_PROCESO;
        }
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getIdTraslado() {
        return idTraslado;
    }

    public void setIdTraslado(Long idTraslado) {
        this.idTraslado = idTraslado;
    }

    public String getSedeOrigen() {
        return sedeOrigen;
    }

    public void setSedeOrigen(String sedeOrigen) {
        this.sedeOrigen = sedeOrigen;
    }

    public String getSedeDestino() {
        return sedeDestino;
    }

    public void setSedeDestino(String sedeDestino) {
        this.sedeDestino = sedeDestino;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDateTime getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(LocalDateTime fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TransferStatus getEstado() {
        return estado;
    }

    public void setEstado(TransferStatus estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
