package com.co.eatupapi.repositories.inventory.product;

import com.co.eatupapi.domain.inventory.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) = LOWER(:name) AND p.locationId = :locationId")
    Long countByNameAndLocation(@Param("name") String name, @Param("locationId") UUID locationId);

    @Query("SELECT COUNT(p) FROM Product p WHERE LOWER(p.name) = LOWER(:name) AND p.locationId = :locationId AND p.id <> :id")
    Long countByNameAndLocationAndIdNot(@Param("name") String name, @Param("locationId") UUID locationId, @Param("id") UUID id);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByLocationId(UUID locationId, Pageable pageable);

    Page<Product> findByLocationIdAndNameContainingIgnoreCase(UUID locationId, String name, Pageable pageable);
}