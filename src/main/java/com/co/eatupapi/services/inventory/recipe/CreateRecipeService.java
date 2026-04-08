package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.dto.inventory.recipe.RecipeRequest;
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import com.co.eatupapi.utils.inventory.recipe.exceptions.RecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateRecipeService {
    public static final String RECIPE_WITH_NAME_EXISTS = "La receta con el nombre %s ya existe.";
    private final RecipeRepository repo;
    private final RecipeMapper mapper;

    public CreateRecipeService(RecipeRepository repo, RecipeMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional
    public void run(RecipeRequest recipe) {
        this.validatePreviousExistence(recipe.getName());
        repo.save(mapper.toDomain(recipe, null));
    }

    private void validatePreviousExistence(String name) {
        if (repo.existsByName(name)) {
            throw new RuntimeException(String.format(RECIPE_WITH_NAME_EXISTS, name));
        }
    }
}
