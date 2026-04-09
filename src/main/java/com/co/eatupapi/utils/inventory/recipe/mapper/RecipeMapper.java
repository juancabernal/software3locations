package com.co.eatupapi.utils.inventory.recipe.mapper;

import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
import com.co.eatupapi.dto.inventory.recipe.RecipeRequest;
import com.co.eatupapi.dto.inventory.recipe.RecipeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    RecipeResponse toResponse(RecipeDomain recipe);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "locationId", expression = "java(java.util.UUID.fromString(\"11111111-1111-1111-1111-111111111111\"))")
    @Mapping(target = "baseCost", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "sellingPrice", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    RecipeDomain toNewDomain(RecipeRequest request, UUID id);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "baseCost", ignore = true)
    @Mapping(target = "sellingPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void toUpdatedDomain(RecipeRequest request, @MappingTarget RecipeDomain existingRecipe);
}