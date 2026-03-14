package com.co.eatupapi.utils.commercial.provider.mapper;

import com.co.eatupapi.domain.commercial.provider.ProviderDomain;
import com.co.eatupapi.dto.commercial.provider.ProviderDTO;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {

    public ProviderDTO toDto(ProviderDomain providerDomain) {
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setId(providerDomain.getId());
        providerDTO.setBusinessName(providerDomain.getBusinessName());
        providerDTO.setDocumentTypeId(providerDomain.getDocumentTypeId());
        providerDTO.setDocumentNumber(providerDomain.getDocumentNumber());
        providerDTO.setTaxRegimeId(providerDomain.getTaxRegimeId());
        providerDTO.setResponsibleFirstName(providerDomain.getResponsibleFirstName());
        providerDTO.setResponsibleLastName(providerDomain.getResponsibleLastName());
        providerDTO.setPhone(providerDomain.getPhone());
        providerDTO.setEmail(providerDomain.getEmail());
        providerDTO.setDepartmentId(providerDomain.getDepartmentId());
        providerDTO.setCityId(providerDomain.getCityId());
        providerDTO.setAddress(providerDomain.getAddress());
        providerDTO.setBranchId(providerDomain.getBranchId());
        providerDTO.setStatus(providerDomain.getStatus());
        providerDTO.setCreatedDate(providerDomain.getCreatedDate());
        providerDTO.setModifiedDate(providerDomain.getModifiedDate());
        return providerDTO;
    }

    public ProviderDomain toDomain(ProviderDTO dto) {
        ProviderDomain providerDomain = new ProviderDomain();
        providerDomain.setId(dto.getId());
        providerDomain.setBusinessName(dto.getBusinessName());
        providerDomain.setDocumentTypeId(dto.getDocumentTypeId());
        providerDomain.setDocumentNumber(dto.getDocumentNumber());
        providerDomain.setTaxRegimeId(dto.getTaxRegimeId());
        providerDomain.setResponsibleFirstName(dto.getResponsibleFirstName());
        providerDomain.setResponsibleLastName(dto.getResponsibleLastName());
        providerDomain.setPhone(dto.getPhone());
        providerDomain.setEmail(dto.getEmail());
        providerDomain.setDepartmentId(dto.getDepartmentId());
        providerDomain.setCityId(dto.getCityId());
        providerDomain.setAddress(dto.getAddress());
        providerDomain.setBranchId(dto.getBranchId());
        providerDomain.setStatus(dto.getStatus());
        providerDomain.setCreatedDate(dto.getCreatedDate());
        providerDomain.setModifiedDate(dto.getModifiedDate());
        return providerDomain;
    }
}

