package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
import com.co.eatupapi.dto.inventory.recipe.RecipeRequest;
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import com.co.eatupapi.utils.inventory.recipe.exceptions.RecipeNotFoundException;
import com.co.eatupapi.utils.inventory.recipe.mapper.RecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateRecipeService {

    public static final String RECIPE_NOT_FOUND = "La receta con el nombre %s no fue encontrada.";

    private final RecipeRepository repo;
    private final RecipeMapper mapper;
    private final RecipeValidatorService recipeValidator;

    public UpdateRecipeService(
            RecipeRepository repo,
            RecipeMapper mapper,
            RecipeValidatorService recipeValidator
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.recipeValidator = recipeValidator;
    }

    @Transactional
    public void run(RecipeRequest request) {
        RecipeDomain existingRecipe = getExistingRecipe(request.getName());
        mapper.toUpdatedDomain(request, existingRecipe);
        recipeValidator.validate(existingRecipe);
        repo.save(existingRecipe);
    }

    private RecipeDomain getExistingRecipe(String name) {
        return repo.findByName(name)
                .orElseThrow(() -> new RecipeNotFoundException(
                        String.format(RECIPE_NOT_FOUND, name)
                ));
    }
}