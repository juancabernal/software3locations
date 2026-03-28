package com.co.eatupapi.controllers.inventory.categories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.co.eatupapi.dto.inventory.categories.CategoryDTO;
import com.co.eatupapi.services.inventory.categories.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void createCategoryDelegatesCreationWithoutPrincipal() {
        CategoryDTO request = new CategoryDTO();
        CategoryDTO responseDto = new CategoryDTO();
        when(categoryService.createCategory(request)).thenReturn(responseDto);

        ResponseEntity<CategoryDTO> response = categoryController.createCategory(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(categoryService).createCategory(request);
    }
}
