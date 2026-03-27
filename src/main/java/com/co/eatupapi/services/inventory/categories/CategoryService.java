package com.co.eatupapi.services.inventory.categories;

import com.co.eatupapi.domain.inventory.categories.CategoryDomain;
import com.co.eatupapi.domain.inventory.categories.CategoryStatus;
import com.co.eatupapi.dto.inventory.categories.CategoryDTO;
import com.co.eatupapi.repositories.inventory.categories.CategoryRepository;
import com.co.eatupapi.utils.inventory.categories.exceptions.BusinessException;
import com.co.eatupapi.utils.inventory.categories.exceptions.ResourceNotFoundException;
import com.co.eatupapi.utils.inventory.categories.exceptions.ValidationException;
import com.co.eatupapi.utils.inventory.categories.mapper.CategoryMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryDTO createCategory(CategoryDTO request, String username) {
        validateCategoryPayload(request);
        validateCategoryNameDoesNotExist(request.getName());

        CategoryDomain categoryDomain = categoryMapper.toDomain(request);
        categoryDomain.setId(UUID.randomUUID());
        categoryDomain.setCreatedBy(username);
        categoryDomain.setStatus(CategoryStatus.ACTIVE);
        categoryDomain.setCreatedDate(LocalDateTime.now());
        categoryDomain.setModifiedDate(LocalDateTime.now());

        CategoryDomain savedCategory = categoryRepository.save(categoryDomain);
        return categoryMapper.toDto(savedCategory);
    }

    public CategoryDTO getCategoryById(String categoryId) {
        CategoryDomain category = categoryRepository.findById(parseUuid(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        return categoryMapper.toDto(category);
    }

    public List<CategoryDTO> getCategories(String status) {
        CategoryStatus parsedStatus = parseStatus(status);

        return categoryRepository.findAll().stream()
                .filter(category -> parsedStatus == null || category.getStatus() == parsedStatus)
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDTO updateCategory(String categoryId, CategoryDTO request) {
        validateCategoryPayload(request);

        CategoryDomain existing = categoryRepository.findById(parseUuid(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        validateCategoryNameForUpdate(existing, request.getName());

        existing.setType(request.getType());
        existing.setName(request.getName());
        existing.setBranchId(request.getBranchId());
        existing.setEntryDate(request.getEntryDate());
        existing.setModifiedDate(LocalDateTime.now());

        CategoryDomain updatedCategory = categoryRepository.save(existing);
        return categoryMapper.toDto(updatedCategory);
    }

    public CategoryDTO updateStatus(String categoryId, String status) {
        CategoryStatus newStatus = parseRequiredStatus(status);

        CategoryDomain existing = categoryRepository.findById(parseUuid(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());

        CategoryDomain updatedCategory = categoryRepository.save(existing);
        return categoryMapper.toDto(updatedCategory);
    }

    private UUID parseUuid(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid category id format");
        }
    }

    private CategoryStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return CategoryStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid category status value");
        }
    }

    private CategoryStatus parseRequiredStatus(String status) {
        CategoryStatus parsedStatus = parseStatus(status);
        if (parsedStatus == null) {
            throw new ValidationException("Invalid category status value");
        }
        return parsedStatus;
    }

    private void validateCategoryPayload(CategoryDTO request) {
        validateRequiredText(request.getType(), "type");
        validateRequiredText(request.getName(), "name");
        validateRequiredObject(request.getEntryDate(), "entryDate");
    }

    private void validateRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateRequiredObject(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateCategoryNameDoesNotExist(String name) {
        categoryRepository.findByName(name)
                .ifPresent(category -> {
                    throw new BusinessException("A category with this name already exists");
                });
    }

    private void validateCategoryNameForUpdate(CategoryDomain existing, String requestedName) {
        categoryRepository.findByName(requestedName)
                .ifPresent(category -> {
                    if (!category.getId().equals(existing.getId())) {
                        throw new BusinessException("A category with this name already exists");
                    }
                });
    }
}
