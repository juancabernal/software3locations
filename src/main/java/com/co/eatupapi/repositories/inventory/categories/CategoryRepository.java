package com.co.eatupapi.repositories.inventory.categories;

import com.co.eatupapi.domain.inventory.categories.CategoryDomain;
import com.co.eatupapi.domain.inventory.categories.CategoryStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDomain, UUID> {

    Optional<CategoryDomain> findByName(String name);

    Optional<CategoryDomain> findTopByOrderByCnsDesc();

    List<CategoryDomain> findByStatus(CategoryStatus status);
}
