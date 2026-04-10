package com.co.eatupapi.services.inventory.transfer;

import com.co.eatupapi.domain.inventory.transfer.Transfer;
import com.co.eatupapi.domain.inventory.transfer.TransferStatus;
import com.co.eatupapi.dto.inventory.transfer.TransferRequestDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferResponseDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferStatusUpdateDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferObservacionUpdateDTO;
import com.co.eatupapi.repositories.inventory.transfer.TransferRepository;
import com.co.eatupapi.utils.inventory.transfer.exceptions.TransferBusinessException;
import com.co.eatupapi.utils.inventory.transfer.exceptions.TransferNotFoundException;
import com.co.eatupapi.utils.inventory.transfer.exceptions.TransferValidationException;
import com.co.eatupapi.utils.inventory.transfer.mapper.TransferMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;

    public TransferServiceImpl(TransferRepository transferRepository, TransferMapper transferMapper) {
        this.transferRepository = transferRepository;
        this.transferMapper = transferMapper;
    }

    @Override
    @Transactional
    public TransferResponseDTO create(TransferRequestDTO request) {
        validateRequest(request);
        Transfer transfer = transferMapper.toDomain(request);
        if (transfer.getEstado() == null) {
            transfer.setEstado(TransferStatus.PENDIENTE);
        }
        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    @Override
    @Transactional
    public TransferResponseDTO update(Long id, TransferRequestDTO request) {
        validateId(id);
        validateRequest(request);

        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("Transferencia no encontrada con id: " + id));

        transferMapper.updateFromRequest(request, transfer);
        if (transfer.getEstado() == null) {
            transfer.setEstado(TransferStatus.PENDIENTE);
        }

        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    @Override
    @Transactional
    public TransferResponseDTO updateStatus(Long id, TransferStatusUpdateDTO statusUpdate) {
        validateId(id);
        if (statusUpdate == null || statusUpdate.estado() == null) {
            throw new TransferValidationException("El estado es obligatorio");
        }

        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("Transferencia no encontrada con id: " + id));

        transfer.setEstado(statusUpdate.estado());
        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    @Override
    @Transactional
    public TransferResponseDTO updateObservaciones(Long id, TransferObservacionUpdateDTO observacionUpdate) {
        validateId(id);
        if (observacionUpdate == null) {
            throw new TransferValidationException("La solicitud de actualización es obligatoria");
        }

        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("Transferencia no encontrada con id: " + id));

        transfer.setObservaciones(observacionUpdate.observaciones());
        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    @Override
    public TransferResponseDTO findById(Long id) {
        validateId(id);
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("Transferencia no encontrada con id: " + id));
        return transferMapper.toResponse(transfer);
    }

    @Override
    public List<TransferResponseDTO> findAll() {
        return transferRepository.findAll()
                .stream()
                .map(transferMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        validateId(id);
        if (!transferRepository.existsById(id)) {
            throw new TransferNotFoundException("Transferencia no encontrada con id: " + id);
        }
        transferRepository.deleteById(id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new TransferValidationException("El id de la transferencia es obligatorio y debe ser mayor a cero");
        }
    }

    private void validateRequest(TransferRequestDTO request) {
        if (request == null) {
            throw new TransferValidationException("La solicitud no puede estar vacía");
        }
        validateLocations(request);
        validateCoreFields(request);
        validateStockAndQuantity(request);
    }

    private void validateLocations(TransferRequestDTO request) {
        if (request.sedeOrigen() == null || request.sedeOrigen() <= 0) {
            throw new TransferValidationException("La sede de origen es obligatoria y debe ser válida");
        }
        if (request.sedeDestino() == null || request.sedeDestino() <= 0) {
            throw new TransferValidationException("La sede de destino es obligatoria y debe ser válida");
        }
        if (request.sedeOrigen().equals(request.sedeDestino())) {
            throw new TransferBusinessException("La sede de origen no puede ser igual a la sede de destino");
        }
    }

    private void validateCoreFields(TransferRequestDTO request) {
        if (request.fecha() == null) {
            throw new TransferValidationException("La fecha de transferencia es obligatoria");
        }
        if (request.responsable() == null || request.responsable().isBlank()) {
            throw new TransferValidationException("El responsable es obligatorio");
        }
        if (request.producto() == null || request.producto() <= 0) {
            throw new TransferValidationException("El producto es obligatorio y debe ser válido");
        }
    }

    private void validateStockAndQuantity(TransferRequestDTO request) {
        if (request.stock() == null || request.stock() < 0) {
            throw new TransferValidationException("El stock no puede ser negativo");
        }
        if (request.cantidad() == null || request.cantidad() <= 0) {
            throw new TransferValidationException("La cantidad debe ser mayor a cero");
        }
        if (request.cantidad() > request.stock()) {
            throw new TransferBusinessException("La cantidad a transferir no puede superar el stock disponible");
        }
    }
}