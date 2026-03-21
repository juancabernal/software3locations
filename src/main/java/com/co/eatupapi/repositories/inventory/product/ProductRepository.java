package com.co.eatupapi.repositories.inventory.product;

import com.co.eatupapi.domain.inventory.product.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(UUID id);
    Product save(Product product);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}