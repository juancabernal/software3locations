package com.co.eatupapi.controllers.inventory.recipe;

import com.co.eatupapi.dto.inventory.recipe.RecipeRequest;
import com.co.eatupapi.dto.inventory.recipe.RecipeResponse;
import com.co.eatupapi.services.inventory.recipe.CreateRecipeService;
import com.co.eatupapi.services.inventory.recipe.DeleteRecipeService;
import com.co.eatupapi.services.inventory.recipe.GetRecipeService;
import com.co.eatupapi.services.inventory.recipe.UpdateRecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final CreateRecipeService createService;
    private final GetRecipeService getService;
    private final UpdateRecipeService updateService;
    private final DeleteRecipeService deleteService;

    public RecipeController(CreateRecipeService createService, GetRecipeService getService,
                            UpdateRecipeService updateService, DeleteRecipeService deleteService) {
        this.createService = createService;
        this.getService = getService;
        this.updateService = updateService;
        this.deleteService = deleteService;
    }

    @Operation(summary = "Crear una receta", description = "Crea una nueva receta en el sistema.",
            requestBody = @RequestBody(content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"name\":\"Receta Ejemplo\",\"description\":\"Descripción de ejemplo\"}"))))
    @ApiResponse(responseCode = "200", description = "Receta creada exitosamente.")
    @PostMapping
    public ResponseEntity<Void> createRecipe(@RequestBody RecipeRequest request) {
        createService.run(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener una receta", description = "Obtiene los detalles de una receta por su nombre.")
    @ApiResponse(responseCode = "200", description = "Detalles de la receta.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RecipeResponse.class)))
    @ApiResponse(responseCode = "404", description = "Receta no encontrada.")
    @GetMapping("/{name}")
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable String name) {
        RecipeResponse response = getService.run(name);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar una receta", description = "Actualiza los detalles de una receta existente.",
            requestBody = @RequestBody(content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"name\":\"Receta Actualizada\",\"description\":\"Nueva descripción\"}"))))
    @ApiResponse(responseCode = "200", description = "Receta actualizada exitosamente.")
    @PutMapping
    public ResponseEntity<Void> updateRecipe(@RequestBody RecipeRequest request) {
        updateService.run(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Inactivar una receta", description = "Inactiva una receta existente por su nombre.")
    @ApiResponse(responseCode = "200", description = "Receta inactivada exitosamente.")
    @ApiResponse(responseCode = "404", description = "Receta no encontrada o ya inactiva.")
    @PatchMapping("/{name}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String name) {
        deleteService.run(name);
        return ResponseEntity.ok().build();
    }
}
