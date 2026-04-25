package com.co.eatupapi.utils.inventory.transfer.mapper;

import com.co.eatupapi.domain.inventory.transfer.Transfer;
import com.co.eatupapi.dto.inventory.transfer.TransferRequestDTO;
import com.co.eatupapi.dto.inventory.transfer.TransferResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    @Mapping(target = "idTraslado", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Transfer toDomain(TransferRequestDTO request);

    TransferResponseDTO toResponse(Transfer transfer);

    @Mapping(target = "idTraslado", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(TransferRequestDTO request, @MappingTarget Transfer transfer);
}
