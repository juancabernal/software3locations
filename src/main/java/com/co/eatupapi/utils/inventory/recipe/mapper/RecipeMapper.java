package com.co.eatupapi.utils.inventory.recipe.mapper;

import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
import com.co.eatupapi.dto.inventory.recipe.RecipeProductRequest;
import com.co.eatupapi.dto.inventory.recipe.RecipeRequest;
import com.co.eatupapi.dto.inventory.recipe.RecipeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {
                UUID.class,
                LocalDateTime.class
        }
)
public interface RecipeMapper {

    RecipeResponse toResponse(RecipeDomain recipe);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productIds", source = "request.products")
    @Mapping(target = "subRecipeIds", source = "request.subRecipeIds")
    @Mapping(target = "locationId", expression = "java(UUID.fromString(\"11111111-1111-1111-1111-111111111111\"))")
    @Mapping(target = "baseCost", ignore = true)
    @Mapping(target = "sellingPrice", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    RecipeDomain toNewDomain(RecipeRequest request, UUID id);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "locationId", ignore = true)
    @Mapping(target = "productIds", source = "request.products")
    @Mapping(target = "subRecipeIds", source = "request.subRecipeIds")
    @Mapping(target = "baseCost", ignore = true)
    @Mapping(target = "sellingPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    void toUpdatedDomain(RecipeRequest request, @MappingTarget RecipeDomain existingRecipe);

    default List<UUID> mapProductsToProductIds(List<RecipeProductRequest> products) {

        if (products == null) {
            return List.of();
        }

        return products.stream()
                .map(RecipeProductRequest::getProductId)
                .toList();
    }
}