package com.co.eatupapi.services.inventory.product;

import com.co.eatupapi.domain.inventory.product.Product;
import com.co.eatupapi.dto.inventory.product.ProductDTO;
import com.co.eatupapi.dto.inventory.product.ProductPatchDTO;
import com.co.eatupapi.dto.inventory.product.ProductRequestDTO;
import com.co.eatupapi.repositories.inventory.product.ProductRepository;
import com.co.eatupapi.utils.inventory.product.exceptions.BusinessException;
import com.co.eatupapi.utils.inventory.product.exceptions.ResourceNotFoundException;
import com.co.eatupapi.utils.inventory.product.exceptions.ValidationException;
import com.co.eatupapi.utils.inventory.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO findById(UUID id) {
        validateId(id);
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public ProductDTO create(ProductRequestDTO request) {
        validateRequest(request);

        if (productRepository.countByNameAndLocation(
                request.getName().trim(), request.getLocation().trim()) > 0) {
            throw new BusinessException(
                    "Ya existe el producto '" + request.getName() +
                            "' en " + request.getLocation());
        }

        Product product = productMapper.toDomain(request);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO update(UUID id, ProductRequestDTO request) {
        validateId(id);
        validateRequest(request);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));

        if (productRepository.countByNameAndLocationAndIdNot(
                request.getName().trim(), request.getLocation().trim(), id) > 0) {
            throw new BusinessException(
                    "Ya existe el producto '" + request.getName() +
                            "' en " + request.getLocation());
        }

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setLocation(request.getLocation());
        product.setUnitOfMeasure(request.getUnitOfMeasure());
        product.setSalePrice(request.getSalePrice());
        product.setStock(request.getStock());
        product.setStartDate(request.getStartDate());

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO patch(UUID id, ProductPatchDTO request) {
        validateId(id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));

        if (request.getName() != null || request.getLocation() != null) {
            String nombreActual = request.getName() != null ?
                    request.getName().trim() : product.getName();
            String sedeActual = request.getLocation() != null ?
                    request.getLocation().trim() : product.getLocation();

            if (productRepository.countByNameAndLocationAndIdNot(
                    nombreActual, sedeActual, id) > 0) {
                throw new BusinessException(
                        "Ya existe el producto '" + nombreActual +
                                "' en " + sedeActual);
            }
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            if (request.getName().length() < 2) {
                throw new ValidationException("El nombre debe tener mínimo 2 caracteres");
            }
            if (request.getName().length() > 100) {
                throw new ValidationException("El nombre no puede superar 100 caracteres");
            }
            product.setName(request.getName());
        }

        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            product.setCategory(request.getCategory());
        }

        if (request.getLocation() != null && !request.getLocation().isBlank()) {
            product.setLocation(request.getLocation());
        }

        if (request.getUnitOfMeasure() != null && !request.getUnitOfMeasure().isBlank()) {
            product.setUnitOfMeasure(request.getUnitOfMeasure());
        }

        if (request.getSalePrice() != null) {
            if (request.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("El precio de venta debe ser mayor a cero");
            }
            if (request.getSalePrice().compareTo(new BigDecimal("99999999.999")) > 0) {
                throw new ValidationException("El precio no puede superar 99,999,999.999");
            }
            product.setSalePrice(request.getSalePrice());
        }

        if (request.getStock() != null) {
            if (request.getStock().compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("El stock no puede ser negativo");
            }
            if (request.getStock().compareTo(new BigDecimal("99999999.999")) > 0) {
                throw new ValidationException("El stock no puede superar 99,999,999.999");
            }
            product.setStock(request.getStock());
        }

        if (request.getStartDate() != null) {
            if (request.getStartDate().isAfter(LocalDate.now())) {
                throw new ValidationException("La fecha de inicio no puede ser una fecha futura");
            }
            product.setStartDate(request.getStartDate());
        }

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        validateId(id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));

        if (product.getStock().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException(
                    "No se puede eliminar el producto '" + product.getName() +
                            "' porque tiene " + product.getStock() + " unidades en stock");
        }

        productRepository.deleteById(id);
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

        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("El nombre del producto es obligatorio");
        }
        if (request.getName().length() < 2) {
            throw new ValidationException("El nombre del producto debe tener mínimo 2 caracteres");
        }
        if (request.getName().length() > 100) {
            throw new ValidationException("El nombre del producto no puede superar los 100 caracteres");
        }

        if (request.getCategory() == null || request.getCategory().isBlank()) {
            throw new ValidationException("La categoría del producto es obligatoria");
        }

        if (request.getLocation() == null || request.getLocation().isBlank()) {
            throw new ValidationException("La sede del producto es obligatoria");
        }

        if (request.getUnitOfMeasure() == null || request.getUnitOfMeasure().isBlank()) {
            throw new ValidationException("La unidad de medida es obligatoria");
        }

        if (request.getSalePrice() == null) {
            throw new ValidationException("El precio de venta es obligatorio");
        }
        if (request.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El precio de venta debe ser mayor a cero");
        }
        if (request.getSalePrice().compareTo(new BigDecimal("99999999.999")) > 0) {
            throw new ValidationException("El precio de venta no puede superar 99,999,999.999");
        }

        if (request.getStock() == null) {
            throw new ValidationException("El stock es obligatorio");
        }
        if (request.getStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("El stock no puede ser negativo");
        }
        if (request.getStock().compareTo(new BigDecimal("99999999.999")) > 0) {
            throw new ValidationException("El stock no puede superar 99,999,999.999");
        }

        if (request.getStartDate() == null) {
            throw new ValidationException("La fecha de inicio es obligatoria");
        }
        if (request.getStartDate().isAfter(LocalDate.now())) {
            throw new ValidationException("La fecha de inicio no puede ser una fecha futura");
        }
    }
}