package com.co.eatupapi.repositories.inventory.categories;

import java.util.Optional;
import java.util.UUID;

import com.co.eatupapi.domain.inventory.categories.CategoryDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDomain, UUID> {

    Optional<CategoryDomain> findByName(String name);
}
