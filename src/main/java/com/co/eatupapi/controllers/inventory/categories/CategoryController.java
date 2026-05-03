package com.co.eatupapi.controllers.inventory.categories;

import com.co.eatupapi.dto.inventory.categories.CategoryDTO;
import com.co.eatupapi.dto.inventory.categories.CategoryStatusUpdateDTO;
import com.co.eatupapi.services.inventory.categories.CategoryService;
import com.co.eatupapi.utils.inventory.categories.exceptions.ValidationException;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory/api/v1/categories")
@Tag(name="Categorías", description="Endpoints para gestionar las categorías de productos en el inventario")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Operation (summary = "Crear una nueva categoría",
                description = "Permite crear una nueva categoría de productos en el inventario. " +
                        "Requiere un objeto JSON con los detalles de la categoría a crear.")

    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, por ejemplo, si falta un campo requerido o el formato es incorrecto")
    @ApiResponse(responseCode = "409", description = "Conflicto, por ejemplo, si ya existe una categoría con el mismo nombre")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO request) {
        CategoryDTO saved = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Obtener categoría por ID",
               description = "Permite obtener los detalles de una categoría específica utilizando su ID. " +
                       "El ID debe ser un UUID válido.")

    @ApiResponse(responseCode = "200", description = "Categoría encontrada y devuelta exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, por ejemplo, si el ID de la categoría no es un UUID válido")
    @ApiResponse(responseCode = "404", description = "No se encontró una categoría con el ID proporcionado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String categoryId) {
        CategoryDTO category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Obtener lista de categorías",
               description = "Permite obtener una lista de todas las categorías de productos en el inventario. " +
                       "Se puede filtrar por estado utilizando el parámetro de consulta 'status' (ACTIVE o INACTIVE).")

    @ApiResponse(responseCode = "200", description = "Lista de categorías devuelta exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, por ejemplo, si el valor del parámetro de estado no es válido")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(@RequestParam(required = false) String status) {
        List<CategoryDTO> categories = categoryService.getCategories(status);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Actualizar el estado de una categoría",
               description = "Permite actualizar el estado (ACTIVE o INACTIVE) de una categoría específica utilizando su ID. " +
                       "Requiere un objeto JSON con el nuevo estado.")

    @ApiResponse(responseCode = "200", description = "Estado de la categoría actualizado exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, por ejemplo, si el ID de la categoría no es un UUID válido o si el nuevo estado no es válido")
    @ApiResponse(responseCode = "404", description = "No se encontró una categoría con el ID proporcionado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    @PatchMapping("/{categoryId}/status")
    public ResponseEntity<CategoryDTO> updateStatus(
            @PathVariable String categoryId,
            @RequestBody CategoryStatusUpdateDTO request
    ) {
        if (request == null) {
            throw new ValidationException("Request body is required");
        }

        CategoryDTO updated = categoryService.updateStatus(categoryId, request.getStatus());
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Buscar categorías por nombre",
               description = "Permite buscar categorías de productos en el inventario que coincidan con un nombre específico. " +
                       "El nombre se proporciona como parte de la ruta y se realiza una búsqueda de coincidencia parcial.")

    @ApiResponse(responseCode = "200", description = "Lista de categorías que coinciden con el nombre devuelta exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, por ejemplo, si el nombre de búsqueda está vacío o es nulo")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    @GetMapping("/name/{name}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByName(@PathVariable String name) {
        List<CategoryDTO> categories = categoryService.getCategoriesByName(name);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Buscar categorías por tipo",
               description = "Permite buscar categorías de productos en el inventario que coincidan con un tipo específico. " +
                       "El tipo se proporciona como parte de la ruta y se realiza una búsqueda de coincidencia parcial.")

    @ApiResponse(responseCode = "200", description = "Lista de categorías que coinciden con el tipo devuelta exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, por ejemplo, si el tipo de búsqueda está vacío o es nulo")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByType(@PathVariable String type) {
        List<CategoryDTO> categories = categoryService.getCategoriesByType(type);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Buscar categorias por subtipo",
               description = "Permite buscar categorias segun su subtipo, por ejemplo productos o descuentos.")

    @ApiResponse(responseCode = "200", description = "Lista de categorias que coinciden con el subtipo devuelta exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud invalida, por ejemplo, si el subtipo de busqueda esta vacio o es nulo")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    @GetMapping("/subtype/{subtype}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesBySubtype(@PathVariable String subtype) {
        List<CategoryDTO> categories = categoryService.getCategoriesBySubtype(subtype);
        return ResponseEntity.ok(categories);
    }
}
