package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteRecipeService {

    public static final String RECIPE_NOT_FOUND = "La receta con el nombre %s no fue encontrada.";

    private final RecipeRepository repo;

    public DeleteRecipeService(RecipeRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void run(String name) {

        RecipeDomain recipe = repo.findByNameAndActiveTrue(name)
                .orElseThrow(() -> new RuntimeException(String.format(RECIPE_NOT_FOUND, name)));

        recipe.setActive(false);
        repo.save(recipe);
    }
}