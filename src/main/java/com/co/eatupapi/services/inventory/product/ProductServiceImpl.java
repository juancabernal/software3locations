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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCTO_NO_ENCONTRADO = "Producto no encontrado con id: ";
    private static final BigDecimal MAX_VALUE = new BigDecimal("99999999.999");

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Page<ProductDTO> findAll(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        if (name != null && !name.isBlank()) {
            return productRepository
                    .findByNameContainingIgnoreCase(name, pageable)
                    .map(productMapper::toDto);
        }
        return productRepository.findAll(pageable)
                .map(productMapper::toDto);
    }

    @Override
    public ProductDTO findById(UUID id) {
        validateId(id);
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        PRODUCTO_NO_ENCONTRADO + id));
    }

    @Override
    @Transactional
    public ProductDTO create(ProductRequestDTO request) {
        validateRequest(request);

        if (productRepository.countByNameAndLocation(
                request.getName().trim(), request.getLocationId()) > 0) {
            throw new BusinessException(
                    "Ya existe el producto '" + request.getName() + "' en esta sede");
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
                        PRODUCTO_NO_ENCONTRADO + id));

        if (productRepository.countByNameAndLocationAndIdNot(
                request.getName().trim(), request.getLocationId(), id) > 0) {
            throw new BusinessException(
                    "Ya existe el producto '" + request.getName() + "' en esta sede");
        }

        product.setName(request.getName());
        product.setCategoryId(request.getCategoryId());
        product.setLocationId(request.getLocationId());
        product.setUnitOfMeasure(request.getUnitOfMeasure());
        product.setSalePrice(request.getSalePrice());
        product.setStock(request.getStock());
        product.setStartDate(request.getStartDate());

        return productMapper.toDto(productRepository.save(product));
    }


    @Override
    public Page<ProductDTO> findByLocation(UUID locationId, int page, int size, String name) {
        if (locationId == null) {
            throw new ValidationException("El id de la sede es obligatorio");
        }

        Pageable pageable = PageRequest.of(page, size);

        if (name != null && !name.isBlank()) {
            return productRepository
                    .findByLocationIdAndNameContainingIgnoreCase(locationId, name, pageable)
                    .map(productMapper::toDto);
        }

        return productRepository
                .findByLocationId(locationId, pageable)
                .map(productMapper::toDto);
    }
    @Override
    @Transactional
    public ProductDTO patch(UUID id, ProductPatchDTO request) {
        validateId(id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        PRODUCTO_NO_ENCONTRADO + id));

        validatePatchDuplicado(id, request, product);
        validatePatchName(request, product);
        validatePatchPrice(request, product);
        validatePatchStock(request, product);
        validatePatchDate(request, product);

        if (request.getCategoryId() != null) {
            product.setCategoryId(request.getCategoryId());
        }
        if (request.getLocationId() != null) {
            product.setLocationId(request.getLocationId());
        }
        if (request.getUnitOfMeasure() != null) {
            product.setUnitOfMeasure(request.getUnitOfMeasure());
        }

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        validateId(id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        PRODUCTO_NO_ENCONTRADO + id));

        if (product.getStock().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException(
                    "No se puede eliminar el producto '" + product.getName() +
                            "' porque tiene " + product.getStock() + " unidades en stock");
        }

        productRepository.deleteById(id);
    }

    // ── Validaciones patch ────────────────────────────────

    private void validatePatchDuplicado(UUID id, ProductPatchDTO request, Product product) {
        if (request.getName() == null && request.getLocationId() == null) {
            return;
        }
        String nombre = request.getName() != null ? request.getName().trim() : product.getName();
        UUID sede = request.getLocationId() != null ? request.getLocationId() : product.getLocationId();

        if (productRepository.countByNameAndLocationAndIdNot(nombre, sede, id) > 0) {
            throw new BusinessException("Ya existe el producto '" + nombre + "' en esta sede");
        }
    }

    private void validatePatchName(ProductPatchDTO request, Product product) {
        if (request.getName() == null || request.getName().isBlank()) {
            return;
        }
        if (request.getName().length() < 2) {
            throw new ValidationException("El nombre debe tener mínimo 2 caracteres");
        }
        if (request.getName().length() > 100) {
            throw new ValidationException("El nombre no puede superar 100 caracteres");
        }
        product.setName(request.getName());
    }

    private void validatePatchPrice(ProductPatchDTO request, Product product) {
        if (request.getSalePrice() == null) {
            return;
        }
        if (request.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El precio de venta debe ser mayor a cero");
        }
        if (request.getSalePrice().compareTo(MAX_VALUE) > 0) {
            throw new ValidationException("El precio no puede superar 99,999,999.999");
        }
        product.setSalePrice(request.getSalePrice());
    }

    private void validatePatchStock(ProductPatchDTO request, Product product) {
        if (request.getStock() == null) {
            return;
        }
        if (request.getStock().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("El stock no puede ser negativo");
        }
        if (request.getStock().compareTo(MAX_VALUE) > 0) {
            throw new ValidationException("El stock no puede superar 99,999,999.999");
        }
        product.setStock(request.getStock());
    }

    private void validatePatchDate(ProductPatchDTO request, Product product) {
        if (request.getStartDate() == null) {
            return;
        }
        if (request.getStartDate().isAfter(LocalDate.now())) {
            throw new ValidationException("La fecha de inicio no puede ser una fecha futura");
        }
        product.setStartDate(request.getStartDate());
    }

    // ── Validaciones generales ────────────────────────────

    private void validateId(UUID id) {
        if (id == null) {
            throw new ValidationException("El id del producto es obligatorio");
        }
    }

    private void validateRequest(ProductRequestDTO request) {
        if (request == null) {
            throw new ValidationException("La solicitud no puede estar vacía");
        }
        validateRequestName(request);
        validateRequestFields(request);
        validatePriceRange(request.getSalePrice());
        validateStockRange(request.getStock());
        validateRequestDate(request);
    }

    private void validateRequestName(ProductRequestDTO request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("El nombre del producto es obligatorio");
        }
        if (request.getName().length() < 2) {
            throw new ValidationException("El nombre del producto debe tener mínimo 2 caracteres");
        }
        if (request.getName().length() > 100) {
            throw new ValidationException("El nombre del producto no puede superar los 100 caracteres");
        }
    }

    private void validateRequestFields(ProductRequestDTO request) {
        if (request.getCategoryId() == null) {
            throw new ValidationException("El id de la categoría es obligatorio");
        }
        if (request.getLocationId() == null) {
            throw new ValidationException("El id de la sede es obligatorio");
        }
        if (request.getUnitOfMeasure() == null) {
            throw new ValidationException("La unidad de medida es obligatoria");
        }
        if (request.getSalePrice() == null) {
            throw new ValidationException("El precio de venta es obligatorio");
        }
        if (request.getStock() == null) {
            throw new ValidationException("El stock es obligatorio");
        }
    }

    private void validatePriceRange(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("El precio de venta debe ser mayor a cero");
        }
        if (price.compareTo(MAX_VALUE) > 0) {
            throw new ValidationException("El precio de venta no puede superar 99,999,999.999");
        }
    }

    private void validateStockRange(BigDecimal stock) {
        if (stock.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("El stock no puede ser negativo");
        }
        if (stock.compareTo(MAX_VALUE) > 0) {
            throw new ValidationException("El stock no puede superar 99,999,999.999");
        }
    }

    private void validateRequestDate(ProductRequestDTO request) {
        if (request.getStartDate() == null) {
            throw new ValidationException("La fecha de inicio es obligatoria");
        }
        if (request.getStartDate().isAfter(LocalDate.now())) {
            throw new ValidationException("La fecha de inicio no puede ser una fecha futura");
        }
    }
}