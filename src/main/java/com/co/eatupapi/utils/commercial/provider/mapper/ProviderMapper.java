package com.co.eatupapi.utils.commercial.provider.mapper;
import com.co.eatupapi.domain.commercial.provider.ProviderDomain;
import com.co.eatupapi.dto.commercial.provider.ProviderDTO;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {

    public ProviderDTO toDto(ProviderDomain provider) {
        ProviderDTO dto = new ProviderDTO();
        dto.setId(provider.getId() == null ? null : provider.getId().toString());
        dto.setBusinessName(provider.getBusinessName());
        dto.setDocumentTypeId(provider.getDocumentTypeId());
        dto.setDocumentNumber(provider.getDocumentNumber());
        dto.setTaxRegimeId(provider.getTaxRegimeId());
        dto.setResponsibleFirstName(provider.getResponsibleFirstName());
        dto.setResponsibleLastName(provider.getResponsibleLastName());
        dto.setPhone(provider.getPhone());
        dto.setEmail(provider.getEmail());
        dto.setDepartmentId(provider.getDepartmentId());
        dto.setCityId(provider.getCityId());
        dto.setAddress(provider.getAddress());
        dto.setBranchId(provider.getBranchId());
        dto.setStatus(provider.getStatus());
        dto.setCreatedDate(provider.getCreatedAt());
        dto.setModifiedDate(provider.getModifiedAt());
        return dto;
    }

    public ProviderDomain toDomain(ProviderDTO dto) {
        ProviderDomain provider = new ProviderDomain();
        updateDomain(provider, dto);
        provider.setStatus(dto.getStatus());
        provider.setCreatedAt(dto.getCreatedDate());
        provider.setModifiedAt(dto.getModifiedDate());
        return provider;
    }

    public void updateDomain(ProviderDomain provider, ProviderDTO dto) {
        provider.setBusinessName(dto.getBusinessName());
        provider.setDocumentTypeId(dto.getDocumentTypeId());
        provider.setDocumentNumber(dto.getDocumentNumber());
        provider.setTaxRegimeId(dto.getTaxRegimeId());
        provider.setResponsibleFirstName(dto.getResponsibleFirstName());
        provider.setResponsibleLastName(dto.getResponsibleLastName());
        provider.setPhone(dto.getPhone());
        provider.setEmail(dto.getEmail());
        provider.setDepartmentId(dto.getDepartmentId());
        provider.setCityId(dto.getCityId());
        provider.setAddress(dto.getAddress());
        provider.setBranchId(dto.getBranchId());
    }
}
