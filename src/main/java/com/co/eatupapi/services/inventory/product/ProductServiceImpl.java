package com.co.eatupapi.services.inventory.product;

import com.co.eatupapi.domain.inventory.product.Product;
import com.co.eatupapi.dto.inventory.product.ProductDTO;
import com.co.eatupapi.dto.inventory.product.ProductRequestDTO;
import com.co.eatupapi.utils.inventory.product.exceptions.BusinessException;
import com.co.eatupapi.utils.inventory.product.exceptions.ResourceNotFoundException;
import com.co.eatupapi.utils.inventory.product.exceptions.ValidationException;
import com.co.eatupapi.utils.inventory.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final List<Product> database = new ArrayList<>();
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
        database.add(createProduct("Tomate Fresco",    "Verduras",    "Sede Central", "Kg",     new BigDecimal("2.50"),  new BigDecimal("45.000"), LocalDate.of(2024, 1, 10)));
        database.add(createProduct("Pechuga de Pollo", "Carnes",      "Sede Norte",   "Kg",     new BigDecimal("8.99"),  new BigDecimal("8.500"),  LocalDate.of(2024, 1, 12)));
        database.add(createProduct("Leche Entera",     "Lácteos",     "Sede Central", "Lt",     new BigDecimal("1.20"),  new BigDecimal("0.000"),  LocalDate.of(2024, 1,  8)));
        database.add(createProduct("Aceite de Oliva",  "Condimentos", "Sede Sur",     "Lt",     new BigDecimal("12.50"), new BigDecimal("25.000"), LocalDate.of(2024, 2,  1)));
        database.add(createProduct("Cebolla Blanca",   "Verduras",    "Sede Central", "Kg",     new BigDecimal("1.80"),  new BigDecimal("5.500"),  LocalDate.of(2024, 1, 15)));
        database.add(createProduct("Queso Mozzarella", "Lácteos",     "Sede Norte",   "Kg",     new BigDecimal("7.50"),  new BigDecimal("0.000"),  LocalDate.of(2024, 1, 20)));
        database.add(createProduct("Agua Mineral",     "Bebidas",     "Sede Este",    "Unidad", new BigDecimal("0.80"),  new BigDecimal("120.000"),LocalDate.of(2024, 2,  5)));
        database.add(createProduct("Carne Molida",     "Carnes",      "Sede Central", "Kg",     new BigDecimal("6.50"),  new BigDecimal("30.000"), LocalDate.of(2024, 1, 18)));
        database.add(createProduct("Lechuga Romana",   "Verduras",    "Sede Sur",     "Unidad", new BigDecimal("1.50"),  new BigDecimal("22.000"), LocalDate.of(2024, 2, 10)));
        database.add(createProduct("Papa Amarilla",    "Verduras",    "Sede Central", "Kg",     new BigDecimal("1.20"),  new BigDecimal("65.000"), LocalDate.of(2024, 1, 25)));
    }

    @Override
    public List<ProductDTO> findAll() {
        return database.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO findById(UUID id) {
        validateId(id);
        return database.stream()
                .filter(p -> p.getId().equals(id))
                .map(productMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));
    }

    @Override
    public ProductDTO create(ProductRequestDTO request) {
        validateRequest(request);
        validateDuplicateName(request.getName(), null);

        Product product = productMapper.toDomain(request);
        product.setId(UUID.randomUUID());
        database.add(product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDTO update(UUID id, ProductRequestDTO request) {
        validateId(id);
        validateRequest(request);

        Product product = database.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));

        validateDuplicateName(request.getName(), id);

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setLocation(request.getLocation());
        product.setUnitOfMeasure(request.getUnitOfMeasure());
        product.setSalePrice(request.getSalePrice());
        product.setStock(request.getStock());
        product.setStartDate(request.getStartDate());

        return productMapper.toDto(product);
    }

    @Override
    public void delete(UUID id) {
        validateId(id);

        Product product = database.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));

        if (product.getStock().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException(
                    "No se puede eliminar el producto '" + product.getName() +
                            "' porque tiene " + product.getStock() + " unidades en stock");
        }

        database.removeIf(p -> p.getId().equals(id));
    }

    // ── Validaciones ─────────────────────────────────────

    private void validateId(UUID id) {
        if (id == null) {
            throw new ValidationException("El id del producto es obligatorio");
        }
    }

    private void validateRequest(ProductRequestDTO request) {
        if (request == null) {
            throw new ValidationException("La solicitud no puede estar vacía");
        }

        // Validaciones de nombre
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("El nombre del producto es obligatorio");
        }
        if (request.getName().length() < 2) {
            throw new ValidationException("El nombre del producto debe tener mínimo 2 caracteres");
        }
        if (request.getName().length() > 100) {
            throw new ValidationException("El nombre del producto no puede superar los 100 caracteres");
        }

        // Validaciones de categoría
        if (request.getCategory() == null || request.getCategory().isBlank()) {
            throw new ValidationException("La categoría del producto es obligatoria");
        }

        // Validaciones de sede
        if (request.getLocation() == null || request.getLocation().isBlank()) {
            throw new ValidationException("La sede del producto es obligatoria");
        }

        // Validaciones de unidad de medida
        if (request.getUnitOfMeasure() == null || request.getUnitOfMeasure().isBlank()) {
            throw new ValidationException("La unidad de medida es obligatoria");
        }

        // Validaciones de precio
        if (request.getSalePrice() == null) {
            throw new ValidationException("El precio de venta es obligatorio");
        }
        if (request.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El precio de venta debe ser mayor a cero");
        }
        if (request.getSalePrice().compareTo(new BigDecimal("99999999.999")) > 0) {
            throw new ValidationException("El precio de venta no puede superar 99,999,999.999");
        }

        // Validaciones de stock
        if (request.getStock() == null) {
            throw new ValidationException("El stock es obligatorio");
        }
        if (request.getStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("El stock no puede ser negativo");
        }
        if (request.getStock().compareTo(new BigDecimal("99999999.999")) > 0) {
            throw new ValidationException("El stock no puede superar 99,999,999.999");
        }

        // Validaciones de fecha
        if (request.getStartDate() == null) {
            throw new ValidationException("La fecha de inicio es obligatoria");
        }
        if (request.getStartDate().isAfter(LocalDate.now())) {
            throw new ValidationException("La fecha de inicio no puede ser una fecha futura");
        }
    }

    private void validateDuplicateName(String name, UUID excludeId) {
        boolean exists = database.stream()
                .filter(p -> excludeId == null || !p.getId().equals(excludeId))
                .anyMatch(p -> p.getName().equalsIgnoreCase(name.trim()));
        if (exists) {
            throw new BusinessException(
                    "Ya existe un producto con el nombre '" + name + "'");
        }
    }

    // ── Helper datos quemados ─────────────────────────────

    private Product createProduct(String name, String category, String location,
                                  String unitOfMeasure, BigDecimal salePrice,
                                  BigDecimal stock, LocalDate startDate) {
        Product p = new Product();
        p.setId(UUID.randomUUID());
        p.setName(name);
        p.setCategory(category);
        p.setLocation(location);
        p.setUnitOfMeasure(unitOfMeasure);
        p.setSalePrice(salePrice);
        p.setStock(stock);
        p.setStartDate(startDate);
        return p;
    }
}