package com.co.eatupapi.services.inventory.categories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.co.eatupapi.domain.inventory.categories.CategoryDomain;
import com.co.eatupapi.domain.inventory.categories.CategoryStatus;
import com.co.eatupapi.dto.inventory.categories.CategoryDTO;
import com.co.eatupapi.repositories.inventory.categories.CategoryRepository;
import com.co.eatupapi.utils.inventory.categories.exceptions.BusinessException;
import com.co.eatupapi.utils.inventory.categories.exceptions.ValidationException;
import com.co.eatupapi.utils.inventory.categories.mapper.CategoryMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategoryIgnoresClientIdAndPersistsServerManagedFields() {
        CategoryDTO request = new CategoryDTO();
        request.setId("not-used-client-id");
        request.setCns(999L);
        request.setType("FOOD");
        request.setName("Snacks");
        request.setEntryDate(LocalDate.of(2026, 3, 28));
        request.setStatus(CategoryStatus.INACTIVE);

        CategoryDomain newEntity = new CategoryDomain();
        newEntity.setType(request.getType());
        newEntity.setName(request.getName());
        newEntity.setEntryDate(request.getEntryDate());

        CategoryDomain savedEntity = new CategoryDomain();
        UUID generatedId = UUID.randomUUID();
        savedEntity.setId(generatedId);
        savedEntity.setCns(1L);
        savedEntity.setType(request.getType());
        savedEntity.setName(request.getName());
        savedEntity.setEntryDate(request.getEntryDate());
        savedEntity.setStatus(CategoryStatus.ACTIVE);
        savedEntity.setCreatedDate(LocalDateTime.now());
        savedEntity.setModifiedDate(savedEntity.getCreatedDate());

        CategoryDTO expected = new CategoryDTO();
        expected.setId(generatedId.toString());

        when(categoryRepository.findByName("Snacks")).thenReturn(Optional.empty());
        when(categoryRepository.findTopByOrderByCnsDesc()).thenReturn(Optional.empty());
        when(categoryMapper.toNewEntity(request)).thenReturn(newEntity);
        when(categoryRepository.saveAndFlush(any(CategoryDomain.class))).thenReturn(savedEntity);
        when(categoryMapper.toDto(savedEntity)).thenReturn(expected);

        CategoryDTO result = categoryService.createCategory(request);

        assertEquals(expected, result);

        ArgumentCaptor<CategoryDomain> captor = ArgumentCaptor.forClass(CategoryDomain.class);
        verify(categoryRepository).saveAndFlush(captor.capture());
        CategoryDomain persisted = captor.getValue();
        assertEquals("FOOD", persisted.getType());
        assertEquals("Snacks", persisted.getName());
        assertEquals(LocalDate.of(2026, 3, 28), persisted.getEntryDate());
        assertEquals(1L, persisted.getCns());
        assertEquals(CategoryStatus.ACTIVE, persisted.getStatus());
        assertTrue(persisted.getId() != null);
        assertTrue(persisted.getCreatedDate() != null);
        assertTrue(persisted.getModifiedDate() != null);
    }

    @Test
    void createCategoryReturnsBusinessErrorWhenDatabaseRejectsDuplicateName() {
        CategoryDTO request = new CategoryDTO();
        request.setType("FOOD");
        request.setName("Snacks");
        request.setEntryDate(LocalDate.of(2026, 3, 28));

        CategoryDomain newEntity = new CategoryDomain();
        CategoryDomain duplicate = new CategoryDomain();
        when(categoryRepository.findByName("Snacks")).thenReturn(Optional.empty(), Optional.of(duplicate));
        when(categoryRepository.findTopByOrderByCnsDesc()).thenReturn(Optional.empty());
        when(categoryMapper.toNewEntity(request)).thenReturn(newEntity);
        when(categoryRepository.saveAndFlush(any(CategoryDomain.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate key"));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> categoryService.createCategory(request)
        );

        assertEquals("A category with this name already exists", exception.getMessage());
    }

    @Test
    void createCategoryRejectsNullPayload() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> categoryService.createCategory(null)
        );

        assertEquals("Field 'request' is required and cannot be empty", exception.getMessage());
        verify(categoryRepository, never()).findByName(any());
    }
}
