package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.dto.inventory.recipe.RecipeRequest;
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import com.co.eatupapi.utils.inventory.recipe.exceptions.ErrorCode;
import com.co.eatupapi.utils.inventory.recipe.exceptions.RecipeBusinessException;
import com.co.eatupapi.utils.inventory.recipe.mapper.RecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CreateRecipeService {

    public static final String RECIPE_WITH_NAME_EXISTS = "La receta con el nombre %s ya existe.";

    private final RecipeRepository repo;
    private final RecipeMapper mapper;
    private final GenerateRecipeIdService idService;

    public CreateRecipeService(
            RecipeRepository repo,
            RecipeMapper mapper,
            GenerateRecipeIdService idService
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.idService = idService;
    }

    @Transactional
    public void run(RecipeRequest recipe) {
        this.validatePreviousExistence(recipe.getName());
        UUID id = idService.run();
        repo.save(mapper.toNewDomain(recipe, id));
    }

    private void validatePreviousExistence(String name) {
        if (repo.existsByName(name)) {
            throw new RecipeBusinessException(
                    ErrorCode.RECIPE_ALREADY_EXISTS,
                    String.format(RECIPE_WITH_NAME_EXISTS, name)
            );
        }
    }
}