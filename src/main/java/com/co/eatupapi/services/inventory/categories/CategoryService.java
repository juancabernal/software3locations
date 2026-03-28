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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryDTO createCategory(CategoryDTO request) {
        validateCategoryPayload(request);
        validateCategoryNameDoesNotExist(request.getName());

        CategoryDomain categoryDomain = categoryMapper.toNewEntity(request);
        categoryDomain.setId(UUID.randomUUID());
        categoryDomain.setStatus(CategoryStatus.ACTIVE);
        categoryDomain.setCreatedDate(LocalDateTime.now());
        categoryDomain.setModifiedDate(LocalDateTime.now());

        CategoryDomain savedCategory = persistNewCategory(categoryDomain, request.getName());
        return categoryMapper.toDto(savedCategory);
    }

    public CategoryDTO getCategoryById(String categoryId) {
        CategoryDomain category = categoryRepository.findById(parseUuid(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        return categoryMapper.toDto(category);
    }

    public List<CategoryDTO> getCategories(String status) {
        CategoryStatus parsedStatus = parseStatus(status);

        List<CategoryDomain> rows = parsedStatus == null
                ? categoryRepository.findAll()
                : categoryRepository.findByStatus(parsedStatus);
        return rows.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
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
        validateRequiredObject(request, "request");
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

    private Long nextCns() {
        return categoryRepository.findTopByOrderByCnsDesc()
                .map(CategoryDomain::getCns)
                .map(current -> current + 1)
                .orElse(1L);
    }

    private CategoryDomain persistNewCategory(CategoryDomain categoryDomain, String categoryName) {
        for (int attempt = 0; attempt < 3; attempt++) {
            categoryDomain.setCns(nextCns());
            try {
                return categoryRepository.saveAndFlush(categoryDomain);
            } catch (DataIntegrityViolationException ex) {
                if (categoryRepository.findByName(categoryName).isPresent()) {
                    throw new BusinessException("A category with this name already exists");
                }
            }
        }

        throw new BusinessException("The category could not be created due to a concurrent save conflict");
    }
}
