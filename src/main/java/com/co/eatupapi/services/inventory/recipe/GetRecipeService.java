package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.dto.inventory.recipe.RecipeResponse;
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import com.co.eatupapi.utils.inventory.recipe.exceptions.RecipeNotFoundException;
import com.co.eatupapi.utils.inventory.recipe.mapper.RecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetRecipeService {
    private static final String RECIPE_WITH_NAME_NOT_FOUND = "La receta con el nombre %s no existe.";

    private final RecipeRepository repo;
    private final RecipeMapper mapper;

    public GetRecipeService(RecipeRepository repo, RecipeMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public RecipeResponse run(String name) {
        validateName(name);
        var recipe = repo.findByName(name)
                .orElseThrow(() -> new RecipeNotFoundException(
                        String.format(RECIPE_WITH_NAME_NOT_FOUND, name)
                ));
        return mapper.toResponse(recipe);
    }

    private void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("RECIPE_NAME_REQUIRED");
        }

        if (name.length() < 3 || name.length() > 150) {
            throw new IllegalArgumentException("RECIPE_NAME_INVALID_LENGTH");
        }

        if (!name.matches("^[a-zA-Z0-9ÁÉÍÓÚáéíóúñÑ ]+$")) {
            throw new IllegalArgumentException("RECIPE_NAME_INVALID_FORMAT");
        }
    }
}
