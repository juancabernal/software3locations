package com.co.eatupapi.dto.inventory.transfer;

import java.time.LocalDateTime;

public class TransferDTO {

    private Long idTraslado;
    private Long sedeOrigen;
    private Long sedeDestino;
    private LocalDateTime fecha;
    private String responsable;
    private Long producto;
    private Integer stock;
    private Integer cantidad;
    private String observaciones;
    private String estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public TransferDTO() {
    }

    // Constructor completo
    public TransferDTO(Long idTraslado, Long sedeOrigen, Long sedeDestino, LocalDateTime fecha,
                       String responsable, Long producto, Integer stock, Integer cantidad,
                       String observaciones, String estado, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idTraslado = idTraslado;
        this.sedeOrigen = sedeOrigen;
        this.sedeDestino = sedeDestino;
        this.fecha = fecha;
        this.responsable = responsable;
        this.producto = producto;
        this.stock = stock;
        this.cantidad = cantidad;
        this.observaciones = observaciones;
        this.estado = estado;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

