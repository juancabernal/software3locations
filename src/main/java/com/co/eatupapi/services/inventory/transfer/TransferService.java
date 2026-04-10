package com.co.eatupapi.services.inventory.transfer;

import com.co.eatupapi.dto.inventory.transfer.TransferRequestDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferResponseDTO;

import java.util.List;

public interface TransferService {
    TransferResponseDTO create(TransferRequestDTO request);

    TransferResponseDTO update(Long id, TransferRequestDTO request);

    TransferResponseDTO findById(Long id);

    List<TransferResponseDTO> findAll();

    void delete(Long id);
}

