package com.co.eatupapi.controllers.inventory.transfer;

import com.co.eatupapi.dto.inventory.transfer.TransferRequestDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferResponseDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferStatusUpdateDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferObservacionUpdateDTO;
import com.co.eatupapi.services.inventory.transfer.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/api/v1/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransfer(@Valid @RequestBody TransferRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferResponseDTO> updateTransfer(@PathVariable Long id,
                                                              @Valid @RequestBody TransferRequestDTO request) {
        return ResponseEntity.ok(transferService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TransferResponseDTO> updateTransferStatus(@PathVariable Long id,
                                                                     @Valid @RequestBody TransferStatusUpdateDTO statusUpdate) {
        return ResponseEntity.ok(transferService.updateStatus(id, statusUpdate));
    }

    @PatchMapping("/{id}/observaciones")
    public ResponseEntity<TransferResponseDTO> updateTransferObservaciones(@PathVariable Long id,
                                                                             @RequestBody TransferObservacionUpdateDTO observacionUpdate) {
        return ResponseEntity.ok(transferService.updateObservaciones(id, observacionUpdate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponseDTO> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TransferResponseDTO>> getAllTransfers() {
        return ResponseEntity.ok(transferService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        transferService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

