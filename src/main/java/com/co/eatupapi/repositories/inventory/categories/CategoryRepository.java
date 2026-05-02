package com.co.eatupapi.repositories.inventory.categories;

import com.co.eatupapi.domain.inventory.categories.CategoryDomain;
import com.co.eatupapi.domain.inventory.categories.CategoryStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryDomain, UUID> {

    Optional<CategoryDomain> findByName(String name);

    List<CategoryDomain> findByStatus(CategoryStatus status);

    Optional<CategoryDomain> findTopByOrderByCnsDesc();

    @Query(value = "select pg_advisory_xact_lock(8202401)", nativeQuery = true)
    void lockCategoryCnsCounter();

    // Búsqueda flexible por nombre (LIKE %name%)
    List<CategoryDomain> findByNameContainingIgnoreCase(String name);

    // Búsqueda flexible por tipo (LIKE %type%) - CAMBIADO A CONTAINING
    List<CategoryDomain> findByTypeContainingIgnoreCase(String type);

    List<CategoryDomain> findBySubtypeIgnoreCase(String subtype);
}
