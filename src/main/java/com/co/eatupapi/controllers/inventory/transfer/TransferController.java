package com.co.eatupapi.controllers.inventory.transfer;

import com.co.eatupapi.domain.inventory.transfer.Transfer;
import com.co.eatupapi.services.inventory.transfer.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory/api/v1/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    /**
     * Crear una nueva transferencia
     */
    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@RequestBody Transfer transfer) {
        Transfer savedTransfer = transferService.createTransfer(transfer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransfer);
    }

    /**
     * Actualizar una transferencia existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transfer> updateTransfer(@PathVariable Long id, @RequestBody Transfer transfer) {
        Transfer updatedTransfer = transferService.updateTransfer(id, transfer);
        if (updatedTransfer != null) {
            return ResponseEntity.ok(updatedTransfer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtener una transferencia por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long id) {
        Optional<Transfer> transfer = transferService.getTransferById(id);
        if (transfer.isPresent()) {
            return ResponseEntity.ok(transfer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtener todas las transferencias
     */
    @GetMapping
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        List<Transfer> transfers = transferService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }

    /**
     * Eliminar una transferencia
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        Optional<Transfer> transfer = transferService.getTransferById(id);
        if (transfer.isPresent()) {
            transferService.deleteTransfer(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

