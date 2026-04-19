package com.co.eatupapi.services.inventory.transfer;

import com.co.eatupapi.dto.inventory.transfer.TransferRequestDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferResponseDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferStatusUpdateDTO;

import java.util.List;

public interface TransferService {
    TransferResponseDTO create(TransferRequestDTO request);

    TransferResponseDTO updateStatus(Long id, TransferStatusUpdateDTO statusUpdate);

    TransferResponseDTO findById(Long id);

    List<TransferResponseDTO> findAll();

    List<TransferResponseDTO> findAllInTransit();
}
