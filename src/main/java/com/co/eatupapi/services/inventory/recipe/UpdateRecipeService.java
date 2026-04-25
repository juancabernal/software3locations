package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
import com.co.eatupapi.dto.inventory.recipe.RecipeRequest;
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import com.co.eatupapi.utils.inventory.recipe.exceptions.RecipeNotFoundException;
import com.co.eatupapi.utils.inventory.recipe.mapper.RecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class UpdateRecipeService {

    public static final String RECIPE_NOT_FOUND = "La receta con el nombre %s no fue encontrada.";

    private final RecipeRepository repo;
    private final RecipeMapper mapper;
    private final RecipeValidatorService recipeValidator;
    private final RecipeExistenceValidatorService existenceValidator;
    private final CalculateRecipeCostService costService;
    private final CalculateRecipeSellingPriceService sellingPriceService;
    private final RecalculateDependentRecipesCostService recalculateDependentRecipesCostService;

    public UpdateRecipeService(
            RecipeRepository repo,
            RecipeMapper mapper,
            RecipeValidatorService recipeValidator,
            RecipeExistenceValidatorService existenceValidator,
            CalculateRecipeCostService costService,
            CalculateRecipeSellingPriceService sellingPriceService, RecalculateDependentRecipesCostService recalculateDependentRecipesCostService
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.recipeValidator = recipeValidator;
        this.existenceValidator = existenceValidator;
        this.costService = costService;
        this.sellingPriceService = sellingPriceService;
        this.recalculateDependentRecipesCostService = recalculateDependentRecipesCostService;
    }

    @Transactional
    public void run(RecipeRequest request) {

        validateSubRecipesIfPresent(request.getSubRecipeIds());

        RecipeDomain existingRecipe = getExistingRecipe(request.getName());

        mapper.toUpdatedDomain(request, existingRecipe);

        if (existingRecipe.getSubRecipeIds() == null) {
            existingRecipe.setSubRecipeIds(List.of());
        }

        BigDecimal baseCost = costService.run(request);

        BigDecimal sellingPrice = sellingPriceService.run(
                baseCost,
                request.getProfitMargin()
        );

        existingRecipe.setBaseCost(baseCost);
        existingRecipe.setSellingPrice(sellingPrice);

        recipeValidator.validate(existingRecipe);

        repo.save(existingRecipe);
        recalculateDependentRecipesCostService.run(
                existingRecipe.getId()
        );
    }

    private void validateSubRecipesIfPresent(List<UUID> subRecipeIds) {

        if (subRecipeIds == null || subRecipeIds.isEmpty()) {
            return;
        }

        existenceValidator.run(subRecipeIds);
    }

    private RecipeDomain getExistingRecipe(String name) {

        return repo.findByName(name)
                .orElseThrow(() -> new RecipeNotFoundException(
                        String.format(RECIPE_NOT_FOUND, name)
                ));
    }
}