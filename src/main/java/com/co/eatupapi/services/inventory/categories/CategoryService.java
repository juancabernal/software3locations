package com.co.eatupapi.services.inventory.categories;

import com.co.eatupapi.dto.inventory.categories.CategoryDTO;
import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO request);

    CategoryDTO getCategoryById(String categoryId);

    List<CategoryDTO> getCategories(String status);

    CategoryDTO updateStatus(String categoryId, String status);

    List<CategoryDTO> getCategoriesByName(String name);

    List<CategoryDTO> getCategoriesByType(String type);

    List<CategoryDTO> getCategoriesBySubtype(String subtype);
}
