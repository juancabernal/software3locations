package com.co.eatupapi.dto.inventory.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RecipeRequest {

    @NotBlank
    private String name;

    @NotNull
    private UUID categoryId;

    @NotNull
    private UUID locationId;

    private List<UUID> productIds;

    private List<UUID> subRecipeIds;

    @NotNull
    @PositiveOrZero
    private Integer profitMargin;

    private Boolean visibleInMenu;

    private Boolean active;
}