package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
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
        var recipe = repo.findByName(name)
                .orElseThrow(() -> new RecipeNotFoundException(
                        String.format(RECIPE_WITH_NAME_NOT_FOUND, name)
                ));
        return mapper.toResponse(recipe);
    }
}
