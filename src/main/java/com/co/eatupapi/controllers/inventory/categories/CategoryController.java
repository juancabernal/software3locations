package com.co.eatupapi.controllers.inventory.categories;

import com.co.eatupapi.dto.inventory.categories.CategoryDTO;
import com.co.eatupapi.dto.inventory.categories.CategoryStatusUpdateDTO;
import com.co.eatupapi.services.inventory.categories.CategoryService;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestBody CategoryDTO request,
            Principal principal
    ) {
        String username = principal.getName();
        CategoryDTO saved = categoryService.createCategory(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String categoryId) {
        CategoryDTO category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(@RequestParam(required = false) String status) {
        List<CategoryDTO> categories = categoryService.getCategories(status);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoryDTO request
    ) {
        CategoryDTO updated = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{categoryId}/status")
    public ResponseEntity<CategoryDTO> updateStatus(
            @PathVariable String categoryId,
            @RequestBody CategoryStatusUpdateDTO request
    ) {
        CategoryDTO updated = categoryService.updateStatus(categoryId, request.getStatus());
        return ResponseEntity.ok(updated);
    }
}
