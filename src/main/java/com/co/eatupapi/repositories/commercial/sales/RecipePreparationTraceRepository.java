package com.co.eatupapi.repositories.commercial.sales;

import com.co.eatupapi.domain.commercial.sales.RecipePreparationTraceDomain;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipePreparationTraceRepository extends JpaRepository<RecipePreparationTraceDomain, UUID> {
    List<RecipePreparationTraceDomain> findBySale_Id(UUID saleId);
    void deleteBySale_Id(UUID saleId);
    List<RecipePreparationTraceDomain> findBySaleDetail_Id(UUID saleDetailId);
}
