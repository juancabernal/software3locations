package com.co.eatupapi.domain.inventory.transfer;

import jakarta.persistence.*;
import java.time.LocalDateTime; 

@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_traslado")
    private Long idTraslado;

    @Column(name = "sede_origen", nullable = false)
    private Long sedeOrigen;

    @Column(name = "sede_destino", nullable = false)
    private Long sedeDestino;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "responsable", nullable = false)
    private String responsable;

    @Column(name = "producto", nullable = false)
    private Long producto;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "estado", nullable = false)
    private String estado = "PENDIENTE";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Transfer() {
    }

    // Getters y Setters
    public Long getIdTraslado() {
        return idTraslado;
    }

    public void setIdTraslado(Long idTraslado) {
        this.idTraslado = idTraslado;
    }

    public Long getSedeOrigen() {
        return sedeOrigen;
    }

    public void setSedeOrigen(Long sedeOrigen) {
        this.sedeOrigen = sedeOrigen;
    }

    public Long getSedeDestino() {
        return sedeDestino;
    }

    public void setSedeDestino(Long sedeDestino) {
        this.sedeDestino = sedeDestino;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Long getProducto() {
        return producto;
    }

    public void setProducto(Long producto) {
        this.producto = producto;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
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
