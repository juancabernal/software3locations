package com.co.eatupapi.utils.inventory.recipe.exceptions;

import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
import com.co.eatupapi.dto.inventory.recipe.RecipeRequest;
import com.co.eatupapi.dto.inventory.recipe.RecipeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    RecipeResponse toResponse(RecipeDomain recipe);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "baseCost", ignore = true)
    @Mapping(target = "sellingPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    RecipeDomain toDomain(RecipeRequest request, @MappingTarget RecipeDomain existingRecipe);
}
