package com.co.eatupapi.services.inventory.product;

import com.co.eatupapi.dto.inventory.product.ProductDTO;
import com.co.eatupapi.dto.inventory.product.ProductPatchDTO;
import com.co.eatupapi.dto.inventory.product.ProductRequestDTO;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductDTO> findAll();
    ProductDTO findById(UUID id);
    ProductDTO create(ProductRequestDTO request);
    ProductDTO update(UUID id, ProductRequestDTO request);
    ProductDTO patch(UUID id, ProductPatchDTO request);
    void delete(UUID id);
}