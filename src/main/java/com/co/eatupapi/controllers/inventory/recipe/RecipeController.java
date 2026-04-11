package com.co.eatupapi.controllers.inventory.recipe;

import com.co.eatupapi.dto.inventory.recipe.RecipeRequest;
import com.co.eatupapi.dto.inventory.recipe.RecipeResponse;
import com.co.eatupapi.services.inventory.recipe.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

    private final CreateRecipeService createService;
    private final GetRecipeService getService;
    private final UpdateRecipeService updateService;
    private final DeleteRecipeService deleteService;
    private final ListRecipesService listService;

    public RecipeController(CreateRecipeService createService, GetRecipeService getService,
                            UpdateRecipeService updateService, DeleteRecipeService deleteService, ListRecipesService listService) {
        this.createService = createService;
        this.getService = getService;
        this.updateService = updateService;
        this.deleteService = deleteService;
        this.listService = listService;
    }

    @Operation(
            summary = "Crear receta",
            description = "Crea una nueva receta. El ID es generado automáticamente por el sistema."
    )
    @ApiResponse(responseCode = "201", description = "Receta creada exitosamente.")
    @PostMapping
    public ResponseEntity<Void> createRecipe(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeRequest.class),
                            examples = @ExampleObject(value = """
                {
                  "name": "Pizza Margarita",
                  "categoryId": "22222222-2222-2222-2222-222222222222",
                  "locationId": "11111111-1111-1111-1111-111111111111",
                  "productIds": [
                    "33333333-3333-3333-3333-333333333333",
                    "44444444-4444-4444-4444-444444444444"
                  ],
                  "subRecipeIds": [
                    "55555555-5555-5555-5555-555555555555"
                  ],
                  "profitMargin": 30,
                  "visibleInMenu": true,
                  "active": true
                }
                """)
                    )
            )
            @RequestBody RecipeRequest request
    ) {
        createService.run(request);
        return ResponseEntity.status(201).build();
    }

    @Operation(
            summary = "Obtener receta por nombre",
            description = "Retorna la información completa de una receta dado su nombre."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Receta encontrada",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecipeResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Receta no encontrada",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                {
                  "error": "RECIPE_NOT_FOUND",
                  "message": "La receta con el nombre Pizza no fue encontrada.",
                  "timestamp": "2026-04-09T12:00:00"
                }
            """)
            )
    )
    @GetMapping("/{name}")
    public ResponseEntity<RecipeResponse> getRecipe(
            @Parameter(
                    description = "Nombre único de la receta",
                    example = "Pizza Margarita",
                    required = true
            )
            @PathVariable String name
    ) {
        return ResponseEntity.ok(getService.run(name));
    }

    @Operation(
            summary = "Actualizar receta",
            description = "Actualiza los datos de una receta existente."
    )
    @ApiResponse(responseCode = "200", description = "Receta actualizada exitosamente.")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada.")
    @PutMapping
    public ResponseEntity<Void> updateRecipe(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeRequest.class)
                    )
            )
            @RequestBody RecipeRequest request
    ) {
        updateService.run(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Inactivar receta",
            description = "Inactiva una receta existente por su nombre (soft delete)."
    )
    @ApiResponse(responseCode = "200", description = "Receta inactivada exitosamente.")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada o ya inactiva.")
    @PatchMapping("/{name}")
    public ResponseEntity<Void> deleteRecipe(
            @Parameter(
                    description = "Nombre de la receta a inactivar",
                    example = "Pizza Margarita",
                    required = true
            )
            @PathVariable String name
    ) {
        deleteService.run(name);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Listar recetas",
            description = "Retorna una lista de todas las recetas activas del sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Listado de recetas obtenido exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RecipeResponse.class),
                    examples = @ExampleObject(value = """
            [
              {
                "id": "a1b2c3d4-1111-2222-3333-abcdefabcdef",
                "name": "Pizza Margarita",
                "categoryId": "11111111-1111-1111-1111-111111111111",
                "locationId": "11111111-1111-1111-1111-111111111111",
                "productIds": [],
                "subRecipeIds": [],
                "baseCost": 15000,
                "profitMargin": 30,
                "sellingPrice": 19500,
                "visibleInMenu": true,
                "active": true,
                "createdAt": "2026-04-09T12:00:00",
                "updatedAt": "2026-04-09T12:00:00"
              }
            ]
        """)
            )
    )
    @GetMapping
    public ResponseEntity<List<RecipeResponse>> listRecipes() {
        return ResponseEntity.ok(listService.run());
    }
}