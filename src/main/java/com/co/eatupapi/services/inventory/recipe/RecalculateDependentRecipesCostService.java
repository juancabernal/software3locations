package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class RecalculateDependentRecipesCostService {

    private final RecipeRepository repo;
    private final CalculateRecipeSellingPriceService sellingPriceService;

    public RecalculateDependentRecipesCostService(
            RecipeRepository repo,
            CalculateRecipeSellingPriceService sellingPriceService
    ) {
        this.repo = repo;
        this.sellingPriceService = sellingPriceService;
    }

    @Transactional
    public void run(UUID recipeId) {
        process(recipeId);
    }

    private void process(UUID recipeId) {

        List<RecipeDomain> parentRecipes =
                repo.findBySubRecipeIdsContains(recipeId);

        for (RecipeDomain recipe : parentRecipes) {
            recalculateRecipe(recipe);
            process(recipe.getId());
        }
    }

    private void recalculateRecipe(RecipeDomain recipe) {

        BigDecimal baseCost = calculateSubRecipesCost(recipe);

        BigDecimal sellingPrice = sellingPriceService.run(
                baseCost,
                recipe.getProfitMargin()
        );

        recipe.setBaseCost(baseCost);
        recipe.setSellingPrice(sellingPrice);

        repo.save(recipe);
    }

    private BigDecimal calculateSubRecipesCost(RecipeDomain recipe) {

        if (recipe.getSubRecipeIds() == null || recipe.getSubRecipeIds().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return recipe.getSubRecipeIds()
                .stream()
                .map(id -> repo.findById(id)
                        .orElseThrow()
                        .getBaseCost())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}