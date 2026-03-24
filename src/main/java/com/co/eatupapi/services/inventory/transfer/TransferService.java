package com.co.eatupapi.services.inventory.transfer;

import com.co.eatupapi.domain.inventory.transfer.Transfer;
import com.co.eatupapi.repositories.inventory.transfer.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    /**
     * Crear una nueva transferencia
     */
    public Transfer createTransfer(Transfer transfer) {
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setUpdatedAt(LocalDateTime.now());
        if (transfer.getEstado() == null) {
            transfer.setEstado("PENDIENTE");
        }
        return transferRepository.save(transfer);
    }

    /**
     * Actualizar una transferencia existente
     */
    public Transfer updateTransfer(Long id, Transfer transferDetails) {
        Optional<Transfer> transferOptional = transferRepository.findById(id);
        if (transferOptional.isPresent()) {
            Transfer transfer = transferOptional.get();
            
            if (transferDetails.getSedeOrigen() != null) {
                transfer.setSedeOrigen(transferDetails.getSedeOrigen());
            }
            if (transferDetails.getSedeDestino() != null) {
                transfer.setSedeDestino(transferDetails.getSedeDestino());
            }
            if (transferDetails.getFecha() != null) {
                transfer.setFecha(transferDetails.getFecha());
            }
            if (transferDetails.getResponsable() != null) {
                transfer.setResponsable(transferDetails.getResponsable());
            }
            if (transferDetails.getProducto() != null) {
                transfer.setProducto(transferDetails.getProducto());
            }
            if (transferDetails.getStock() != null) {
                transfer.setStock(transferDetails.getStock());
            }
            if (transferDetails.getCantidad() != null) {
                transfer.setCantidad(transferDetails.getCantidad());
            }
            if (transferDetails.getObservaciones() != null) {
                transfer.setObservaciones(transferDetails.getObservaciones());
            }
            if (transferDetails.getEstado() != null) {
                transfer.setEstado(transferDetails.getEstado());
            }
            
            transfer.setUpdatedAt(LocalDateTime.now());
            return transferRepository.save(transfer);
        }
        return null;
    }

    /**
     * Obtener una transferencia por ID
     */
    public Optional<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id);
    }

    /**
     * Obtener todas las transferencias
     */
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    /**
     * Eliminar una transferencia
     */
    public void deleteTransfer(Long id) {
        transferRepository.deleteById(id);
    }
}

