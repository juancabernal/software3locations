package com.co.eatupapi.services.inventory.product;

import com.co.eatupapi.dto.inventory.product.ProductDTO;
import com.co.eatupapi.dto.inventory.product.ProductPatchDTO;
import com.co.eatupapi.dto.inventory.product.ProductRequestDTO;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface ProductService {
    Page<ProductDTO> findAll(int page, int size, String name);
    Page<ProductDTO> findByLocation(UUID locationId, int page, int size, String name);
    ProductDTO findById(UUID id);
    ProductDTO create(ProductRequestDTO request);
    ProductDTO update(UUID id, ProductRequestDTO request);
    ProductDTO patch(UUID id, ProductPatchDTO request);
    void delete(UUID id);
}