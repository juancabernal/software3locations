package com.co.eatupapi.dto.inventory.recipe;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RecipeRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    @Pattern(
            regexp = "^[a-zA-Z0-9ÁÉÍÓÚáéíóúñÑ ]+$",
            message = "El nombre contiene caracteres inválidos"
    )
    private String name;

    @NotNull(message = "El categoryId es obligatorio")
    private UUID categoryId;

    @NotNull(message = "El locationId es obligatorio")
    private UUID locationId;

    @NotNull(message = "Debe enviar al menos un producto")
    @Size(min = 1, message = "Debe haber al menos un producto")
    private List<@NotNull(message = "El productId no puede ser null") UUID> productIds;

    @NotNull(message = "Debe enviar al menos una subreceta")
    @Size(min = 1, message = "Debe haber al menos una subreceta")
    private List<@NotNull(message = "El subRecipeId no puede ser null") UUID> subRecipeIds;

    @NotNull(message = "El margen de ganancia es obligatorio")
    @PositiveOrZero(message = "El margen de ganancia no puede ser negativo")
    @Max(value = 100, message = "El margen de ganancia no puede ser mayor a 100")
    private Integer profitMargin;

    @NotNull(message = "Debe indicar si es visible en el menú")
    private Boolean visibleInMenu;

    @NotNull(message = "Debe indicar si la receta está activa")
    private Boolean active;
}