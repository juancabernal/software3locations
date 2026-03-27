package com.co.eatupapi.utils.inventory.categories.mapper;

import com.co.eatupapi.domain.inventory.categories.CategoryDomain;
import com.co.eatupapi.dto.inventory.categories.CategoryDTO;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(CategoryDomain categoryDomain) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryDomain.getId() != null ? categoryDomain.getId().toString() : null);
        categoryDTO.setType(categoryDomain.getType());
        categoryDTO.setName(categoryDomain.getName());
        categoryDTO.setBranchId(categoryDomain.getBranchId());
        categoryDTO.setCreatedBy(categoryDomain.getCreatedBy());
        categoryDTO.setEntryDate(categoryDomain.getEntryDate());
        categoryDTO.setStatus(categoryDomain.getStatus());
        categoryDTO.setCreatedDate(categoryDomain.getCreatedDate());
        categoryDTO.setModifiedDate(categoryDomain.getModifiedDate());
        return categoryDTO;
    }

    public CategoryDomain toDomain(CategoryDTO dto) {
        CategoryDomain categoryDomain = new CategoryDomain();

        if (dto.getId() != null && !dto.getId().isBlank()) {
            categoryDomain.setId(UUID.fromString(dto.getId()));
        }

        categoryDomain.setType(dto.getType());
        categoryDomain.setName(dto.getName());
        categoryDomain.setBranchId(dto.getBranchId());
        categoryDomain.setCreatedBy(dto.getCreatedBy());
        categoryDomain.setEntryDate(dto.getEntryDate());
        categoryDomain.setStatus(dto.getStatus());
        categoryDomain.setCreatedDate(dto.getCreatedDate());
        categoryDomain.setModifiedDate(dto.getModifiedDate());
        return categoryDomain;
    }
}
