package com.co.eatupapi.services.inventory.recipe;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculateSubRecipesCostService {

    private final GetRecipeByIdService getRecipeByIdService;

    public CalculateSubRecipesCostService(
            GetRecipeByIdService getRecipeByIdService
    ) {
        this.getRecipeByIdService = getRecipeByIdService;
    }

    public BigDecimal run(
            com.co.eatupapi.dto.inventory.recipe.RecipeRequest request
    ) {

        return request.getSubRecipeIds()
                .stream()
                .map(
                        id ->
                                getRecipeByIdService
                                        .run(id)
                                        .getBaseCost()
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}